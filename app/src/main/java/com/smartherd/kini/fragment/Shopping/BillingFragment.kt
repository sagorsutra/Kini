package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smartherd.kini.R
import com.smartherd.kini.adapter.BillingAdapter
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.databinding.FragmentBillingBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private lateinit var billingAdapter: BillingAdapter
    private lateinit var userAddressRef: DatabaseReference
    private lateinit var ordersRef: DatabaseReference


    private var shippingCost: Double = 5.0
    private var taxRate: Double = 0.10 // 10% tax
    private var promoDiscount: Double = 0.0
    private lateinit var currentAddress: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        initializeFirebase()
        fetchAndUpdateAddress()
        setupListeners()
        fetchCartData()

        binding.imageCloseBilling.setOnClickListener {
            // Navigate back to CartFragment
            requireActivity().onBackPressed()  // or use NavController to pop fragment
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        billingAdapter = BillingAdapter()
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = billingAdapter
        }
    }
    private fun initializeFirebase() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        userAddressRef = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(currentUser?.uid ?: "")
            .child("address")

        ordersRef = FirebaseDatabase.getInstance()
            .reference
            .child("orders")
    }
    private fun setupListeners() {
        binding.imageAddAddress.setOnClickListener {
            showAddressDialog()
        }

        binding.imageCloseBilling.setOnClickListener {
            // Navigate back to CartFragment
            requireActivity().onBackPressed()  // or use NavController to pop fragment
        }
        binding.btnApplyPromo.setOnClickListener {
            applyPromoCode(binding.etPromoCode.text.toString())
        }

        binding.buttonPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun fetchAndUpdateAddress() {
        userAddressRef.get().addOnSuccessListener { snapshot ->
            currentAddress = snapshot.getValue(String::class.java) ?: "No Address Available"
            binding.tvShoppingAddress.text = currentAddress
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to fetch address.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showAddressDialog() {
        // Inflate the custom address layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_address, null)
        val addressEditText: EditText = dialogView.findViewById(R.id.etAddress)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Shipping Address")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val address = addressEditText.text.toString()
                if (address.isNotEmpty()) {
                    saveShippingAddress(address)
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid address.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun saveShippingAddress(address: String) {
        userAddressRef.setValue(address).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Address saved successfully!", Toast.LENGTH_SHORT).show()
                currentAddress = address
                binding.tvShoppingAddress.text = address
            } else {
                Toast.makeText(requireContext(), "Failed to save address.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyPromoCode(promoCode: String) {
        val promoRef = FirebaseDatabase.getInstance()
            .reference
            .child("promoCodes")

        promoRef.child(promoCode).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                promoDiscount = snapshot.value.toString().toDouble()
                Toast.makeText(requireContext(), "Promo code applied!", Toast.LENGTH_SHORT).show()
                fetchCartData() // Recalculate prices with promo discount
            } else {
                promoDiscount = 0.0
                Toast.makeText(requireContext(), "Invalid promo code!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Error applying promo code.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCartData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userCartRef = FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(currentUser.uid)
                .child("cart")

            userCartRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cartProducts = mutableListOf<CartProduct>()
                    for (productSnapshot in snapshot.children) {
                        val cartProduct = productSnapshot.getValue(CartProduct::class.java)
                        cartProduct?.let { cartProducts.add(it) }
                    }

                    // Update RecyclerView with cart products
                    updateBilling(cartProducts)
                    updatePrices(cartProducts)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error fetching cart data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateBilling(cartProducts: List<CartProduct>) {
        // Update the list in the adapter
        billingAdapter.submitList(cartProducts)
    }

    private fun updatePrices(cartProducts: List<CartProduct>) {
        val subtotal = cartProducts.sumOf { it.price.toDouble() * it.quantity }
        val tax = subtotal * taxRate
        val totalPrice = subtotal + shippingCost + tax - promoDiscount

        binding.tvSubtotal.text = "Subtotal: $${String.format("%.2f", subtotal)}"
        binding.tvShippingCost.text = "Shipping: $${String.format("%.2f", shippingCost)}"
        binding.tvTax.text = "Tax: $${String.format("%.2f", tax)}"
        binding.tvTotalPrice.text = "Total: $${String.format("%.2f", totalPrice)}"
    }
    private fun placeOrder() {
        if (!::currentAddress.isInitialized || currentAddress.isEmpty()) {
            Toast.makeText(requireContext(), "Please provide a shipping address.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please log in to place an order.", Toast.LENGTH_SHORT).show()
            return
        }

        val cartProducts = billingAdapter.currentList
        if (cartProducts.isEmpty()) {
            Toast.makeText(requireContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val subtotal = cartProducts.sumOf { it.price.toDouble() * it.quantity }
        val tax = subtotal * taxRate
        val totalPrice = subtotal + shippingCost + tax

        val order = mapOf(
            "userId" to currentUser.uid,
            "address" to currentAddress,
            "products" to cartProducts.map { it.toMap() }, // Convert CartProduct to a serializable map
            "subtotal" to subtotal,
            "tax" to tax,
            "shippingCost" to shippingCost,
            "totalPrice" to totalPrice,
            "timestamp" to System.currentTimeMillis()
        )

        val ordersRef = FirebaseDatabase.getInstance()
            .reference
            .child("orders")
            .push()

        ordersRef.setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                clearCart()

                // Navigate to OrderFragment
                navigateToOrders()
                Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to place order.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userCartRef = FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(currentUser.uid)
                .child("cart")

            userCartRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Cart cleared successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to clear cart.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToOrders() {
        val orderFragment = OrderFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, orderFragment) // Replace with your container ID
            .addToBackStack(null) // Optional: Add this transaction to the back stack
            .commit()
    }



//    private fun placeOrder() {
//
//        if (currentAddress == null) {
//            Toast.makeText(requireContext(), "Please login to place an order.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val cartProducts = billingAdapter.currentList
//        val subtotal = cartProducts.sumOf { it.price.toDouble() * it.quantity }
//        val tax = subtotal * taxRate
//        val totalPrice = subtotal + shippingCost + tax - promoDiscount
//
//        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
//
//        val order = mapOf(
//            "userId" to currentUser.uid,
//            "address" to currentUser,
//            "products" to cartProducts,
//            "subtotal" to subtotal,
//            "shippingCost" to shippingCost,
//            "tax" to tax,
//            "totalPrice" to totalPrice,
//            "timestamp" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
//        )
//
//        ordersRef.push().setValue(order).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "Failed to place order.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}



//class BillingFragment : Fragment() {
//
//    private lateinit var binding: FragmentBillingBinding
//    private lateinit var billingAdapter: BillingAdapter
//    private val billingItems = mutableListOf<CartProduct>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentBillingBinding.inflate(inflater, container, false)
//
//        setupRecyclerView()
//        fetchCartData()
//
//        return binding.root
//    }
//
//    private fun setupRecyclerView() {
//        billingAdapter = BillingAdapter(billingItems)
//        binding.rvProducts.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = billingAdapter
//        }
//    }
//
//    // Assuming you're using Firebase Realtime Database or any other data source
//    private fun fetchCartData() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
//            val userCartRef = FirebaseDatabase.getInstance()
//                .reference
//                .child("users")
//                .child(currentUser.uid)
//                .child("cart")
//
//            userCartRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val cartProducts = mutableListOf<CartProduct>()
//                    for (productSnapshot in snapshot.children) {
//                        val cartProduct = productSnapshot.getValue(CartProduct::class.java)
//                        cartProduct?.let { cartProducts.add(it) }
//                    }
//
//                    // Pass the list of cart products to your billing section
//                    updateBilling(cartProducts)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle error
//                }
//            })
//        }
//    }
//
//    private fun updateBilling(cartProducts: List<CartProduct>) {
//        // Update your billing UI with the cart data
//        // For example, pass the cartProducts to the RecyclerView adapter in the billing section
//        billingAdapter.submitList(cartProducts)
//    }
//
//
//    private fun updateTotalPrice() {
//        val totalPrice = billingItems.sumOf { it.price.toDouble() * it.quantity }
//        binding.tvTotalPrice.text = "$$totalPrice"
//    }
//}
