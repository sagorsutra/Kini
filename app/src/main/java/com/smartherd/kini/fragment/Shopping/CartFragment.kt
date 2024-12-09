package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.smartherd.kini.R
import com.smartherd.kini.adapter.CartAdapter
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseReference: DatabaseReference
    private val cartItems = mutableListOf<CartProduct>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Initialize Firebase database reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(currentUser!!.uid).child("cart")

        // Set up the RecyclerView
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        cartAdapter = CartAdapter(
            cartItems,
            onIncreaseQuantity = { cartProduct -> updateQuantity(cartProduct, 1) },
            onDecreaseQuantity = { cartProduct -> updateQuantity(cartProduct, -1) }
        )
        binding.rvCart.adapter = cartAdapter

        // Load cart data from Firebase
        loadCartDataFromFirebase()

        binding.buttonCheckout.setOnClickListener {
            val billingFragment = BillingFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, billingFragment) // Replace with your container ID
                .addToBackStack(null) // Optional: Add this transaction to the back stack
                .commit()
        }

        return binding.root
    }

    private fun loadCartDataFromFirebase() {
        binding.progressbarCart.visibility = View.VISIBLE

        databaseReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                cartItems.clear()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(CartProduct::class.java)
                    product?.let { cartItems.add(it) }
                }
                cartAdapter.notifyDataSetChanged()
                updateTotalPrice()
            } else {
                Toast.makeText(requireContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show()
            }
            binding.progressbarCart.visibility = View.GONE
        }
    }

    private fun updateQuantity(cartProduct: CartProduct, change: Int) {
        val newQuantity = cartProduct.quantity + change
        if (newQuantity <= 0) {
            removeFromCart(cartProduct)
        } else {
            cartProduct.quantity = newQuantity
            databaseReference.child(cartProduct.id).setValue(cartProduct).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cartAdapter.notifyDataSetChanged()
                    updateTotalPrice()
                }
            }
        }
    }

    private fun removeFromCart(cartProduct: CartProduct) {
        databaseReference.child(cartProduct.id).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cartItems.remove(cartProduct)
                cartAdapter.notifyDataSetChanged()
                updateTotalPrice()
                Toast.makeText(requireContext(), "${cartProduct.name} removed from cart.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItems.sumOf { it.price.toInt() * it.quantity }
        binding.tvTotalPrice.text = "$$totalPrice"
    }


}



//class CartFragment : Fragment() {
//
//    private lateinit var binding: FragmentCartBinding
//    private lateinit var cartAdapter: CartAdapter
//    private lateinit var databaseReference: DatabaseReference
//    private val cartItems = mutableListOf<CartProduct>()
//    private val currentUser by lazy { FirebaseAuth.getInstance().currentUser }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentCartBinding.inflate(inflater, container, false)
//
//        if (currentUser == null) {
//            Toast.makeText(requireContext(), "Please log in to view your cart.", Toast.LENGTH_SHORT).show()
//            return binding.root
//        }
//
//        // Debugging
//        Log.d("CartFragment", "Current user ID: ${currentUser?.uid}")
//
//        // Initialize Firebase Database Reference
//        databaseReference = FirebaseDatabase.getInstance().reference
//            .child("users")
//            .child(currentUser!!.uid)
//            .child("cart")
//
//        // Set up RecyclerView
//        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
//        cartAdapter = CartAdapter(
//            cartItems,
//            onIncreaseQuantity = { cartProduct -> updateQuantity(cartProduct, 1) },
//            onDecreaseQuantity = { cartProduct -> updateQuantity(cartProduct, -1) }
//        )
//        binding.rvCart.adapter = cartAdapter
//
//        // Load Cart Data
//        loadCartDataFromFirebase()
//
//        return binding.root
//    }
//
//    private fun loadCartDataFromFirebase() {
//        binding.progressbarCart.visibility = View.VISIBLE
//
//        databaseReference.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val snapshot = task.result
//                cartItems.clear()
//                if (snapshot.exists()) {
//                    for (productSnapshot in snapshot.children) {
//                        val product = productSnapshot.getValue<CartProduct>()
//                        product?.let { cartItems.add(it) }
//                    }
//                    cartAdapter.notifyDataSetChanged()
//                    updateTotalPrice()
//                } else {
//                    Log.d("CartFragment", "No items in cart.")
//                    showEmptyCart()
//                }
//            } else {
//                Log.e("CartFragment", "Failed to load cart data: ${task.exception}")
//                Toast.makeText(requireContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show()
//            }
//            binding.progressbarCart.visibility = View.GONE
//        }
//    }
//
//    private fun updateQuantity(cartProduct: CartProduct, change: Int) {
//        val newQuantity = cartProduct.quantity + change
//        if (newQuantity <= 0) {
//            removeFromCart(cartProduct)
//        } else {
//            cartProduct.quantity = newQuantity
//            databaseReference.child(cartProduct.id).setValue(cartProduct).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    cartAdapter.notifyDataSetChanged()
//                    updateTotalPrice()
//                }
//            }
//        }
//    }
//
//    private fun removeFromCart(cartProduct: CartProduct) {
//        databaseReference.child(cartProduct.id).removeValue().addOnCompleteListener {
//            if (it.isSuccessful) {
//                cartItems.remove(cartProduct)
//                cartAdapter.notifyDataSetChanged()
//                updateTotalPrice()
//                Toast.makeText(requireContext(), "${cartProduct.name} removed from cart.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun updateTotalPrice() {
//        val totalPrice = cartItems.sumOf { it.price.toInt() * it.quantity }
//        binding.tvTotalPrice.text = "$$totalPrice"
//    }
//
//    private fun showEmptyCart() {
//        binding.layoutCartEmpty.visibility = View.VISIBLE
//        binding.rvCart.visibility = View.GONE
//    }
//}


//----> working code

//class CartFragment : Fragment() {
//
//    private lateinit var binding: FragmentCartBinding
//    private lateinit var cartAdapter: CartAdapter
//    private lateinit var databaseReference: DatabaseReference
//    private val cartItems = mutableListOf<CartProduct>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentCartBinding.inflate(inflater, container, false)
//
//        // Initialize Firebase database reference
//        databaseReference = FirebaseDatabase.getInstance().reference.child("cart")
//
//        // Set up the RecyclerView
//        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
//        cartAdapter = CartAdapter(
//            cartItems,
//            onIncreaseQuantity = { cartProduct -> updateQuantity(cartProduct, 1) },
//            onDecreaseQuantity = { cartProduct -> updateQuantity(cartProduct, -1) }
//        )
//        binding.rvCart.adapter = cartAdapter
//
//        // Load cart data from Firebase
//        loadCartDataFromFirebase()
//
//        return binding.root
//    }
//
//    private fun loadCartDataFromFirebase() {
//        binding.progressbarCart.visibility = View.VISIBLE
//
//        databaseReference.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val snapshot = task.result
//                cartItems.clear()
//                for (productSnapshot in snapshot.children) {
//                    val product = productSnapshot.getValue(CartProduct::class.java)
//                    product?.let { cartItems.add(it) }
//                }
//                cartAdapter.notifyDataSetChanged()
//                updateTotalPrice()
//            } else {
//                Toast.makeText(requireContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show()
//            }
//            binding.progressbarCart.visibility = View.GONE
//        }
//    }
//
//    private fun updateQuantity(cartProduct: CartProduct, change: Int) {
//        val newQuantity = cartProduct.quantity + change
//        if (newQuantity <= 0) {
//            removeFromCart(cartProduct)
//        } else {
//            cartProduct.quantity = newQuantity
//            updateCartProduct(cartProduct)
//        }
//    }
//
//    private fun updateCartProduct(cartProduct: CartProduct) {
//        databaseReference.child(cartProduct.id).setValue(cartProduct).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                cartAdapter.notifyDataSetChanged()
//                updateTotalPrice()
//            }
//        }
//    }
//
//    private fun removeFromCart(cartProduct: CartProduct) {
//        databaseReference.child(cartProduct.id).removeValue().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                cartItems.remove(cartProduct)
//                cartAdapter.notifyDataSetChanged()
//                updateTotalPrice()
//                Toast.makeText(requireContext(), "${cartProduct.name} removed from cart.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun updateTotalPrice() {
//        val totalPrice = cartItems.sumOf { it.price.toInt() * it.quantity }
//        binding.tvTotalPrice.text = "$$totalPrice"
//    }
//}




//package com.smartherd.kini.fragment.Shopping
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.android.material.progressindicator.LinearProgressIndicator
//import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
//import com.smartherd.kini.adapter.CartAdapter
//import com.smartherd.kini.data.CartProduct
//import com.smartherd.kini.databinding.FragmentCartBinding
//import com.smartherd.kini.viewmodel.CartViewModel
//
//class CartFragment : Fragment() {
//
//    private lateinit var binding: FragmentCartBinding
//    private lateinit var cartAdapter: CartAdapter
//    private lateinit var progressBar: LinearProgressIndicator
//    private lateinit var checkoutButton: CircularProgressButton
//    private lateinit var databaseReference: DatabaseReference
//    private lateinit var cartViewModel: CartViewModel
//    private lateinit var recyclerView: RecyclerView
//
//    // Initialize cartItems as a mutable list to update dynamically
//    private val cartItems = mutableListOf<CartProduct>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentCartBinding.inflate(inflater, container, false)
//
//        // Initialize Firebase database reference
//        databaseReference = FirebaseDatabase.getInstance().reference.child("products")
//
//        // Initialize the ViewModel
//        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
//
//        // Set up the RecyclerView
//        recyclerView = binding.rvCart
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        // Initialize the CartAdapter with the mutable list
//        cartAdapter = CartAdapter(cartItems)
//        recyclerView.adapter = cartAdapter
//
//        // Set up progress bar and checkout button
//        progressBar = binding.progressbarCart
//        checkoutButton = binding.buttonCheckout
//
//        // Observe cart items from ViewModel
//        cartViewModel.cartItems.observe(viewLifecycleOwner, { cartItems ->
//            if (cartItems.isEmpty()) {
//                showEmptyCart()
//            } else {
//                showCartItems(cartItems)
//            }
//        })
//
//        // Load cart data from Firebase if necessary
//        loadCartDataFromFirebase()
//
//        // Set up checkout button click listener
//        checkoutButton.setOnClickListener {
//            checkout()
//        }
//
//        return binding.root
//    }
//
//    private fun loadCartDataFromFirebase() {
//        progressBar.visibility = View.VISIBLE
//
//        // Fetch products from Firebase to initialize cart
//        databaseReference.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val dataSnapshot = task.result
//                val fetchedCartItems = mutableListOf<CartProduct>()
//
//                // Loop through Firebase data and populate the cartItems list
//                for (productSnapshot in dataSnapshot.children) {
//                    val product = productSnapshot.getValue(CartProduct::class.java)
//                    product?.let { fetchedCartItems.add(it) }
//                }
//
//                // Store the fetched cart items in the ViewModel and update the UI
//                cartViewModel.setCartItems(fetchedCartItems)
//
//                // Update the cartAdapter with the fetched items
//                cartAdapter.submitList(fetchedCartItems)
//
//                progressBar.visibility = View.GONE
//            } else {
//                // Handle failure in fetching data
//                progressBar.visibility = View.GONE
//                Toast.makeText(requireContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun showEmptyCart() {
//        binding.layoutCartEmpty.visibility = View.VISIBLE
//        binding.rvCart.visibility = View.GONE
//        progressBar.visibility = View.GONE
//    }
//
//    private fun showCartItems(cartItems: List<CartProduct>) {
//        binding.layoutCartEmpty.visibility = View.GONE
//        binding.rvCart.visibility = View.VISIBLE
//        progressBar.visibility = View.GONE
//
//        // Update the RecyclerView with the fetched cart items
//        cartAdapter.submitList(cartItems)
//
//        // Update total price (this should be calculated based on your data)
//        val totalPrice = calculateTotalPrice(cartItems)
//        binding.tvTotalPrice.text = "$$totalPrice"
//    }
//
//    private fun calculateTotalPrice(cartItems: List<CartProduct>): Int {
//        var total = 0
//        for (item in cartItems) {
//            total += item.price.toInt() * item.quantity
//        }
//        return total
//    }
//
//    private fun checkout() {
//        checkoutButton.startAnimation()
//
//        // Simulate checkout process delay
//        // In a real app, you would process the checkout here (e.g., API call)
//        binding.root.postDelayed({
//            checkoutButton.revertAnimation()
//            // After successful checkout, show a confirmation or proceed further
//            showCheckoutSuccess()
//        }, 2000)
//    }
//
//    private fun showCheckoutSuccess() {
//        // Handle post-checkout success
//        // For example, navigate to a new screen or show a success message
//    }
//}
//
//
//
////class CartFragment : Fragment() {
////
////    private lateinit var binding: FragmentCartBinding
////    private lateinit var cartAdapter: CartAdapter
////    private lateinit var progressBar: LinearProgressIndicator
////    private lateinit var checkoutButton: CircularProgressButton
////    private lateinit var databaseReference: DatabaseReference
////    private lateinit var cartViewModel: CartViewModel
////    private lateinit var recyclerView: RecyclerView
////
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        binding = FragmentCartBinding.inflate(inflater, container, false)
////
////        // Initialize Firebase database reference
////        databaseReference = FirebaseDatabase.getInstance().reference.child("products")
////
////        // Initialize the ViewModel
////        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
////
////        // Set up the RecyclerView
////        recyclerView = binding.rvCart
////        recyclerView.layoutManager = LinearLayoutManager(requireContext())
////        cartAdapter = CartAdapter()
////        recyclerView.adapter = cartAdapter
////
////        // Set up progress bar and checkout button
////        progressBar = binding.progressbarCart
////        checkoutButton = binding.buttonCheckout
////
////        // Observe cart items from ViewModel
////        cartViewModel.cartItems.observe(viewLifecycleOwner, { cartItems ->
////            if (cartItems.isEmpty()) {
////                showEmptyCart()
////            } else {
////                showCartItems(cartItems)
////            }
////        })
////
////        // Load cart data from Firebase if necessary
////        loadCartDataFromFirebase()
////
////        // Set up checkout button click listener
////        checkoutButton.setOnClickListener {
////            checkout()
////        }
////
////        return binding.root
////    }
////
////    private fun loadCartDataFromFirebase() {
////        progressBar.visibility = View.VISIBLE
////
////        // Fetch products from Firebase to initialize cart
////        databaseReference.get().addOnCompleteListener { task ->
////            if (task.isSuccessful) {
////                val dataSnapshot = task.result
////                val cartItems = mutableListOf<CartProduct>()
////
////                // Loop through Firebase data and populate the cartItems list
////                for (productSnapshot in dataSnapshot.children) {
////                    val product = productSnapshot.getValue(CartProduct::class.java)
////                    product?.let { cartItems.add(it) }
////                }
////
////                // Store the fetched cart items in the ViewModel
////                cartViewModel.setCartItems(cartItems)
////
////                progressBar.visibility = View.GONE
////            } else {
////                // Handle failure in fetching data
////                progressBar.visibility = View.GONE
////                Toast.makeText(requireContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show()
////            }
////        }
////    }
////
////    private fun showEmptyCart() {
////        binding.layoutCartEmpty.visibility = View.VISIBLE
////        binding.rvCart.visibility = View.GONE
////        progressBar.visibility = View.GONE
////    }
////
////    private fun showCartItems(cartItems: List<CartProduct>) {
////        binding.layoutCartEmpty.visibility = View.GONE
////        binding.rvCart.visibility = View.VISIBLE
////        progressBar.visibility = View.GONE
////
////        // Update the RecyclerView with the fetched cart items
////        cartAdapter.submitList(cartItems)
////
////        // Update total price (this should be calculated based on your data)
////        val totalPrice = calculateTotalPrice(cartItems)
////        binding.tvTotalPrice.text = "$$totalPrice"
////    }
////
////    private fun calculateTotalPrice(cartItems: List<CartProduct>): Int {
////        var total = 0
////        for (item in cartItems) {
////            total += item.price.toInt() * item.quantity
////        }
////        return total
////    }
////
////    private fun checkout() {
////        checkoutButton.startAnimation()
////
////        // Simulate checkout process delay
////        // In a real app, you would process the checkout here (e.g., API call)
////        binding.root.postDelayed({
////            checkoutButton.revertAnimation()
////            // After successful checkout, show a confirmation or proceed further
////            showCheckoutSuccess()
////        }, 2000)
////    }
////
////    private fun showCheckoutSuccess() {
////        // Handle post-checkout success
////        // For example, navigate to a new screen or show a success message
////    }
////}
////class CartFragment : Fragment() {
////
////    private lateinit var binding: FragmentCartBinding
////    private lateinit var cartAdapter: CartAdapter
////    private var cartItems: MutableList<CartProduct> = mutableListOf()  // Initialize with an empty list
////    private lateinit var progressBar: LinearProgressIndicator
////    private lateinit var checkoutButton: CircularProgressButton
////    private lateinit var databaseReference: DatabaseReference
////    private lateinit var cartViewModel: CartViewModel
////    private lateinit var recyclerView: RecyclerView
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        binding = FragmentCartBinding.inflate(inflater, container, false)
////
////        progressBar = binding.progressbarCart
////        checkoutButton = binding.buttonCheckout
////        val recyclerView: RecyclerView = binding.rvCart
////
////        // Initialize Firebase database reference
////        databaseReference = FirebaseDatabase.getInstance().reference.child("products") // Update this path based on your Firebase structure
////
////        // Set up the RecyclerView
////        recyclerView.layoutManager = LinearLayoutManager(requireContext())
////        cartAdapter = CartAdapter(cartItems)
////        recyclerView.adapter = cartAdapter
////
////        // Load cart data from Firebase
////        loadCartDataFromFirebase()
////
////        // Set up checkout button click listener
////        checkoutButton.setOnClickListener {
////            checkout()
////        }
////
////        return binding.root
////    }
////
////    private fun loadCartDataFromFirebase() {
////        progressBar.visibility = View.VISIBLE
////
////        // Fetch products from Firebase
////        databaseReference.get().addOnCompleteListener { task ->
////            if (task.isSuccessful) {
////                // If the data is fetched successfully
////                val dataSnapshot = task.result
////                cartItems = mutableListOf()
////
////                // Loop through Firebase data and populate the cartItems list
////                for (productSnapshot in dataSnapshot.children) {
////                    val product = productSnapshot.getValue(CartProduct::class.java)
////                    product?.let { cartItems.add(it) }
////                }
////
////                // Check if cart is empty or not and update UI
////                if (cartItems.isEmpty()) {
////                    showEmptyCart()
////                } else {
////                    showCartItems()
////                }
////            } else {
////                // Handle failure in fetching data
////                progressBar.visibility = View.GONE
////            }
////        }
////    }
////
////    private fun showEmptyCart() {
////        binding.layoutCartEmpty.visibility = View.VISIBLE
////        binding.rvCart.visibility = View.GONE
////        progressBar.visibility = View.GONE
////    }
////
////    private fun showCartItems() {
////        binding.layoutCartEmpty.visibility = View.GONE
////        binding.rvCart.visibility = View.VISIBLE
////        progressBar.visibility = View.GONE
////
////        // Update the RecyclerView with the fetched cart items
////        cartAdapter.submitList(cartItems)
////
////        // Update total price (this should be calculated based on your data)
////        val totalPrice = calculateTotalPrice()
////        binding.tvTotalPrice.text = "$$totalPrice"
////    }
////
////    private fun calculateTotalPrice(): Int {
////        var total = 0
////        for (item in cartItems) {
////            total += item.price.toInt() * item.quantity
////        }
////        return total
////    }
////
////    private fun checkout() {
////        checkoutButton.startAnimation()
////
////        // Simulate checkout process delay
////        // In a real app, you would process the checkout here (e.g., API call)
////        binding.root.postDelayed({
////            checkoutButton.revertAnimation()
////            // After successful checkout, show a confirmation or proceed further
////            showCheckoutSuccess()
////        }, 2000)
////    }
////
////    private fun showCheckoutSuccess() {
////        // Handle post-checkout success
////        // For example, navigate to a new screen or show a success message
////    }
////}