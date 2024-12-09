package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smartherd.kini.R
import com.smartherd.kini.adapter.ProductAdapter
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.data.Product
import com.smartherd.kini.viewmodel.CartViewModel

class ProductListFragment : Fragment() {

    private lateinit var mainCategory: String
    private var productList: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mainCategory = it.getString(ARG_CATEGORY, "")
            productList = it.getParcelableArrayList(ARG_PRODUCTS) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Ensure that you're passing the correct Product type to the adapter
        recyclerView.adapter = ProductAdapter(productList) { product ->
            // Convert Product to CartProduct when adding to cart
            val cartProduct = CartProduct(
                id = product.id,
                name = product.name,
                price = product.price,
                quantity = 1, // default quantity
                imageUrl = product.imageUrl
            )
            addToCart(cartProduct)
        }

        return view
    }

    private fun addToCart(cartProduct: CartProduct) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show()
            return
        }

        val userCartRef = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(currentUser.uid)
            .child("cart")
            .child(cartProduct.id)

        userCartRef.setValue(cartProduct)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val ARG_CATEGORY = "category"
        private const val ARG_PRODUCTS = "products"

        fun newInstance(mainCategory: String, products: ArrayList<Product>): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, mainCategory)
                    putParcelableArrayList(ARG_PRODUCTS, products)
                }
            }
        }
    }
}


//class ProductListFragment : Fragment() {
//
//    private lateinit var mainCategory: String
//    private var productList: List<Product> = emptyList()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mainCategory = it.getString(ARG_CATEGORY, "")
//            productList = it.getParcelableArrayList(ARG_PRODUCTS) ?: emptyList()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = ProductAdapter(productList) { cartProduct ->
//            addToCart(cartProduct)
//        }
//
//        return view
//    }
//
//    private fun addToCart(cartProduct: CartProduct) {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser == null) {
//            Toast.makeText(requireContext(), "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userCartRef = FirebaseDatabase.getInstance()
//            .reference
//            .child("users")
//            .child(currentUser.uid)
//            .child("cart")
//            .child(cartProduct.id)
//
//        userCartRef.setValue(cartProduct)
//    }
//
//    companion object {
//        private const val ARG_CATEGORY = "category"
//        private const val ARG_PRODUCTS = "products"
//
//        fun newInstance(mainCategory: String, products: ArrayList<Product>): ProductListFragment {
//            return ProductListFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_CATEGORY, mainCategory)
//                    putParcelableArrayList(ARG_PRODUCTS, products)
//                }
//            }
//        }
//    }
//}
//
//


//class ProductListFragment : Fragment() {
//
//    private lateinit var mainCategory: String
//    private var productList: List<Product> = emptyList()
//    private lateinit var cartViewModel: CartViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mainCategory = it.getString(ARG_CATEGORY) ?: ""
//            productList = it.getParcelableArrayList(ARG_PRODUCTS) ?: emptyList()
//        }
//        // Initialize CartViewModel
//        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        // Set the adapter with CartViewModel
//        recyclerView.adapter = ProductAdapter(productList, cartViewModel)
//
//        return view
//    }
//
//    companion object {
//        private const val ARG_CATEGORY = "category"
//        private const val ARG_PRODUCTS = "products"
//
//        fun newInstance(category: String, products: ArrayList<Product>): ProductListFragment {
//            return ProductListFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_CATEGORY, category)
//                    putParcelableArrayList(ARG_PRODUCTS, products)
//                }
//            }
//        }
//    }
//}
//
