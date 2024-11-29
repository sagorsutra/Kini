package com.smartherd.kini.fragment

import androidx.fragment.app.Fragment

class PrehomeFragment : Fragment() {

//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager: ViewPager2
//    private lateinit var categoryAdapter: CategoryPagerAdapter
//    private val categoryList = mutableListOf<String>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        tabLayout = view.findViewById(R.id.tabLayout)
//        viewPager = view.findViewById(R.id.viewpagerHome)
//
//        fetchCategoriesFromFirebase()
//
//        return view
//    }
//
//    private fun fetchCategoriesFromFirebase() {
//        val database = FirebaseDatabase.getInstance().getReference("Products")
//
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                categoryList.clear()
//                val categoriesSet = mutableSetOf<String>() // To store unique categories
//
//                for (productSnapshot in snapshot.children) {
//                    val product = productSnapshot.getValue(Product::class.java)
//                    product?.category?.let { categoriesSet.add(it) }
//                }
//
//                categoryList.addAll(categoriesSet.sorted()) // Sort categories alphabetically
//                setupTabLayoutAndViewPager()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseError", "Error fetching categories: ${error.message}")
//            }
//        })
//    }
//
//    private fun setupTabLayoutAndViewPager() {
//        val fragments = categoryList.map { category ->
//            SimpleFragment.newInstance(category)
//        }
//
//        categoryAdapter = CategoryPagerAdapter(fragments, requireActivity())
//        viewPager.adapter = categoryAdapter
//
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = categoryList[position]
//        }.attach()
//    }
}




//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayout
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.smartherd.kini.R
//import com.smartherd.kini.adapter.CategoryPagerAdapter
//import com.smartherd.kini.data.Product
//
//class HomeFragment : Fragment() {
//
//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager: ViewPager2
//    private lateinit var database: DatabaseReference
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        Log.d("FragmentLifecycle", "onCreateView called")
//
//        // Inflate layout
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        // Initialize Firebase Database
//        database = FirebaseDatabase.getInstance().reference.child("Products")
//
//        // Initialize TabLayout and ViewPager
//        tabLayout = view.findViewById(R.id.tabLayout)
//        viewPager = view.findViewById(R.id.viewpagerHome)
//
//        setupTabLayoutAndViewPager()
//
//        return view
//    }
//
//    private fun setupTabLayoutAndViewPager() {
//        val categoryList = listOf("All", "Best Deals", "Special", "Trending")
//
//        // Attach fragments dynamically for each category
//        val fragments = categoryList.map { category ->
//            MainCategoryFragment.newInstance(category)
//        }
//
//        val adapter = CategoryPagerAdapter(
//            fragments,
//            requireActivity() as FragmentActivity,
//            categoryList
//        )
//        viewPager.adapter = adapter
//
//        // Sync TabLayout with ViewPager2
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                tab?.let {
//                    viewPager.currentItem = it.position
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
//
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                tabLayout.selectTab(tabLayout.getTabAt(position))
//            }
//        })
//
//        // Add tabs to TabLayout
//        categoryList.forEach { category ->
//            tabLayout.addTab(tabLayout.newTab().setText(category))
//        }
//    }
//}




//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayoutMediator
//import com.google.firebase.database.*
//import com.smartherd.kini.R
//import com.smartherd.kini.adapter.CategoryPagerAdapter
//import com.smartherd.kini.adapter.ProductAdapter
//import com.smartherd.kini.data.Product
//class HomeFragment : Fragment() {
//
//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager: ViewPager2
//    private lateinit var database: DatabaseReference
//    private val categories = mutableListOf<String>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        Log.d("FragmentLifecycle", "onCreateView called")
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        tabLayout = view.findViewById(R.id.tabLayout)
//        viewPager = view.findViewById(R.id.viewpagerHome)
//
//        // Initialize Firebase Database
//        database = FirebaseDatabase.getInstance().getReference("categories")
//
//        // Fetch categories from Firebase
//        fetchCategories()
//
//        return view
//    }
//
//    private fun fetchCategories() {
//        Log.d("FirebaseDebug", "Fetching categories from Firebase")
//
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                categories.clear()
//                categories.add("Home") // Add "Home" for all products
//                for (categorySnapshot in snapshot.children) {
//                    val category = categorySnapshot.getValue(String::class.java)
//                    if (category != null) {
//                        categories.add(category)
//                    }
//                }
//                setupViewPagerAndTabs()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseDebug", "Error fetching categories: ${error.message}")
//            }
//        })
//    }
//
//    private fun setupViewPagerAndTabs() {
//        val adapter = CategoryPagerAdapter(this, categories)
//        viewPager.adapter = adapter
//
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = categories[position]
//        }.attach()
//    }
//}

//class HomeFragment : Fragment() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var productAdapter: ProductAdapter
//    private val productList = mutableListOf<Product>()
//    private lateinit var database: DatabaseReference
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        Log.d("FragmentLifecycle", "onCreateView called")
//
//        // Inflate layout
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        // Initialize Firebase Database
//        database = FirebaseDatabase.getInstance().reference.child("Products")
//
//        // Initialize RecyclerView
//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.setHasFixedSize(true)
//
//        // Initialize Adapter and attach it to RecyclerView
//        productAdapter = ProductAdapter(productList)
//        recyclerView.adapter = productAdapter
//        Log.d("RecyclerViewDebug", "Adapter attached")
//
//        // Fetch data from Firebase
//        fetchDataFromFirebase()
//
//        return view
//    }
//
//    private fun fetchDataFromFirebase() {
//        Log.d("FirebaseDebug", "Fetching products from Firebase")
//
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                productList.clear() // Clear the list to avoid duplicates
//                if (snapshot.exists()) {
//                    for (productSnapshot in snapshot.children) {
//                        val product = productSnapshot.getValue(Product::class.java)
//                        if (product != null) {
//                            productList.add(product)
//                        } else {
//                            Log.e("FirebaseDebug", "Failed to map product")
//                        }
//                    }
//                    productAdapter.notifyDataSetChanged()
//                    Log.d("FirebaseDebug", "Total products loaded: ${productList.size}")
//                } else {
//                    Log.d("FirebaseDebug", "No products found")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseDebug", "Error fetching data: ${error.message}")
//            }
//        })
//    }
//}


//class HomeFragment : Fragment() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var productAdapter: ProductAdapter
//    private val productList = mutableListOf<Product>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        Log.d("FragmentLifecycle", "onCreateView called")
//
//        // Inflate layout
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        // Initialize RecyclerView
//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.setHasFixedSize(true)
//
//        // Initialize Adapter and attach it to RecyclerView
//        productAdapter = ProductAdapter(productList)
//        recyclerView.adapter = productAdapter
//        Log.d("RecyclerViewDebug", "Adapter attached")
//
//        // Load static data
//        loadStaticData()
//
//        return view
//    }
//
//    private fun loadStaticData() {
//        Log.d("RecyclerViewDebug", "Loading static data")
//
//        // Clear existing data and add static products
//        productList.clear()
//        productList.addAll(
//            listOf(
//                Product(
//                    name = "Test Product 1",
//                    price = "100",
//                    category = "Category A",
//                    imageUrl = "https://via.placeholder.com/150"
//                ),
//                Product(
//                    name = "Test Product 2",
//                    price = "200",
//                    category = "Category B",
//                    imageUrl = "https://via.placeholder.com/150"
//                ),
//                Product(
//                    name = "Test Product 3",
//                    price = "300",
//                    category = "Category C",
//                    imageUrl = "https://via.placeholder.com/150"
//                )
//            )
//        )
//
//        Log.d("RecyclerViewDebug", "Data size: ${productList.size}") // Add this log
//        productAdapter.notifyDataSetChanged()
//        Log.d("RecyclerViewDebug", "Static data loaded and adapter notified")
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d("FragmentLifecycle", "onCreate called")
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.d("FragmentLifecycle", "onViewCreated called")
//    }
//}
