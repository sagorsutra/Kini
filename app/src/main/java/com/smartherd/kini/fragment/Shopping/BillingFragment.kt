package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smartherd.kini.R
import com.smartherd.kini.adapter.BillingAdapter
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.databinding.FragmentBillingBinding

class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private lateinit var billingAdapter: BillingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater, container, false)

        setupRecyclerView()
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
                    updateTotalPrice(cartProducts)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun updateBilling(cartProducts: List<CartProduct>) {
        // Update the list in the adapter
        billingAdapter.submitList(cartProducts)
    }

    private fun updateTotalPrice(cartProducts: List<CartProduct>) {
        // Calculate total price
        val totalPrice = cartProducts.sumOf { it.price.toDouble() * it.quantity }
        binding.tvTotalPrice.text = "$$totalPrice"
    }
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
