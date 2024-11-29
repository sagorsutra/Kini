package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smartherd.kini.R
import com.smartherd.kini.adapter.ProductAdapter
import com.smartherd.kini.data.Product
class ProductListFragment : Fragment() {

    private lateinit var mainCategory: String
    private var productList: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mainCategory = it.getString(ARG_CATEGORY) ?: ""
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
        recyclerView.adapter = ProductAdapter(productList)

        return view
    }

    companion object {
        private const val ARG_CATEGORY = "category"
        private const val ARG_PRODUCTS = "products"

        fun newInstance(category: String, products: ArrayList<Product>): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                    putParcelableArrayList(ARG_PRODUCTS, products)
                }
            }
        }
    }
}
