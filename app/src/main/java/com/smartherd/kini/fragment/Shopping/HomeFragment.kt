package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smartherd.kini.R
import com.smartherd.kini.adapter.CategoryPagerAdapter
import com.smartherd.kini.data.Product

class HomeFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private var categoryAdapter: CategoryPagerAdapter? = null
    private val categoryList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Initialize views
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewpagerHome)
        return view
    }
//    override fun onResume() {
//        super.onResume()
//        fetchCategoriesFromFirebase()
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Fetch categories once the view is created
        fetchCategoriesFromFirebase()
    }



    private fun fetchCategoriesFromFirebase() {
        val database = FirebaseDatabase.getInstance().reference.child("Products")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                val categoriesMap = mutableMapOf<String, MutableList<Product>>() // Map to hold main categories and their products

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.category?.let {
                        val mainCategory = it.split("/").first() // Extract the main category
                        categoriesMap.getOrPut(mainCategory) { mutableListOf() }.add(product) // Group products under the main category
                    }
                }
                categoryList.addAll(categoriesMap.keys) // Add main categories to the list
                categoryList.sort() // Optional: Sort alphabetically

                if (isAdded) {
                    setupTabLayoutAndViewPager(categoriesMap)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching categories: ${error.message}")
            }
        })
    }


    private fun setupTabLayoutAndViewPager(categoriesMap: Map<String, List<Product>>) {
        val fragments = categoryList.map { mainCategory ->
            ProductListFragment.newInstance(mainCategory, ArrayList(categoriesMap[mainCategory] ?: emptyList()))
        }
        categoryAdapter = CategoryPagerAdapter(fragments, requireActivity())
        viewPager.adapter = categoryAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = categoryList[position] // Set main category as tab title
        }.attach()
    }

}
