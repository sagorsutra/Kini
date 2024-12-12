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
import com.google.firebase.database.*
import com.smartherd.kini.adapter.OrderAdapter
import com.smartherd.kini.data.Order
import com.smartherd.kini.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        setupRecyclerView()
        initializeFirebase()
        fetchOrders()

        binding.imageCloseOrders.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter()
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun initializeFirebase() {
        database = FirebaseDatabase.getInstance()
    }


    private fun fetchOrders() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val query = FirebaseDatabase.getInstance().getReference("orders")
            .orderByChild("userId")
            .equalTo(currentUserId)

        binding.progressbarAllOrders.visibility = View.VISIBLE
        binding.tvEmptyOrders.visibility = View.GONE

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ordersList = mutableListOf<Order>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.id = orderSnapshot.key ?: "" // Assign the key as the ID
                    if (order != null) {
                        ordersList.add(order)
                    }
                }

                if (ordersList.isEmpty()) {
                    binding.tvEmptyOrders.visibility = View.VISIBLE
                } else {
                    binding.tvEmptyOrders.visibility = View.GONE
                }

                orderAdapter.submitList(ordersList)
                binding.progressbarAllOrders.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load orders: ${error.message}", Toast.LENGTH_SHORT).show()
                binding.progressbarAllOrders.visibility = View.GONE
            }
        })
    }


//    private fun fetchOrders() {
//        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
//
//        if (currentUserId == null) {
//            Toast.makeText(requireContext(), "User is not logged in.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        binding.progressbarAllOrders.visibility = View.VISIBLE
//        binding.tvEmptyOrders.visibility = View.GONE
//
//        // Query for orders where `userId` matches the current user's ID
//        val query = database.getReference("orders").orderByChild("userId").equalTo(currentUserId)
//
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val ordersList = mutableListOf<Order>()
//                for (orderSnapshot in snapshot.children) {
//                    val order = orderSnapshot.getValue(Order::class.java)
//                    order?.let { ordersList.add(it) }
//                }
//
//                if (ordersList.isEmpty()) {
//                    binding.tvEmptyOrders.visibility = View.VISIBLE
//                } else {
//                    binding.tvEmptyOrders.visibility = View.GONE
//                }
//
//                orderAdapter.submitList(ordersList)
//                binding.progressbarAllOrders.visibility = View.GONE
//
//                // Debug log to verify fetched data
//                Log.d("OrderFragment", "Fetched orders: $ordersList")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(), "Failed to load orders: ${error.message}", Toast.LENGTH_SHORT).show()
//                binding.progressbarAllOrders.visibility = View.GONE
//            }
//        })
//    }
}




//package com.smartherd.kini.fragment.Shopping
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import com.smartherd.kini.adapter.OrderAdapter
//import com.smartherd.kini.data.Order
//import com.smartherd.kini.databinding.FragmentOrderBinding
//
//class OrderFragment : Fragment() {
//
//    private lateinit var binding: FragmentOrderBinding
//    private lateinit var orderAdapter: OrderAdapter
//    private var ordersRef: DatabaseReference? = null
//    private lateinit var ordersQuery: Query
//    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentOrderBinding.inflate(inflater, container, false)
//
//        setupRecyclerView()
//        initializeFirebase()
//        fetchOrders()
//
//        binding.imageCloseOrders.setOnClickListener {
//            requireActivity().onBackPressed()
//        }
//
//        return binding.root
//    }
//
//    private fun setupRecyclerView() {
//        orderAdapter = OrderAdapter()
//        binding.rvAllOrders.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = orderAdapter
//        }
//    }
//
//    private fun initializeFirebase() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser == null) {
//            Toast.makeText(requireContext(), "Please log in to view orders.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        ordersRef = FirebaseDatabase.getInstance()
//            .reference
//            .child("orders")
//    }
//
//
//    private fun fetchOrders() {
//        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
//        if (currentUserId == null) {
//            Toast.makeText(requireContext(), "User is not logged in.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        binding.progressbarAllOrders.visibility = View.VISIBLE
//        binding.tvEmptyOrders.visibility = View.GONE
//
//        // Query for orders where `userId` matches the current user's ID
//        val query = ordersRef?.orderByChild("userId")?.equalTo(currentUserId)
//
//        if (query != null) {
//            query.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val ordersList = mutableListOf<Order>()
//                    for (orderSnapshot in snapshot.children) {
//                        val order = orderSnapshot.getValue(Order::class.java)
//                        order?.let { ordersList.add(it) }
//                    }
//
//                    if (ordersList.isEmpty()) {
//                        binding.tvEmptyOrders.visibility = View.VISIBLE
//                    } else {
//                        binding.tvEmptyOrders.visibility = View.GONE
//                    }
//
//                    orderAdapter.submitList(ordersList)
//                    binding.progressbarAllOrders.visibility = View.GONE
//
//                    // Debug log to verify fetched data
//                    Log.d("OrderFragment", "Fetched orders: $ordersList")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(requireContext(), "Failed to load orders: ${error.message}", Toast.LENGTH_SHORT).show()
//                    binding.progressbarAllOrders.visibility = View.GONE
//                }
//            })
//        }
//    }
//}
//
//
//
//
//
