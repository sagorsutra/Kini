package com.smartherd.kini.fragment.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.kini.R
import com.smartherd.kini.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize Firebase Auth and Firestore
//        auth = FirebaseAuth.getInstance()
//        firestore = FirebaseFirestore.getInstance()
//
//        // Get current user ID
//        val userId = auth.currentUser?.uid
//
//
//        if (userId != null) {
//            // Retrieve user data from Firestore
//            firestore.collection("users").document(userId).get()
//                .addOnSuccessListener { document ->
//                    if (document != null) {
//                        val firstName = document.getString("firstName")
//                        val lastName = document.getString("lastName")
//                        val email = document.getString("email")
//
//                        // Update UI with user information
////                        binding.firstname.text = firstName
////                        binding.lastname.text = lastName
////                        binding.email.text = email
//                    } else {
//                        Log.d("HomeFragment", "No such document")
//                        Toast.makeText(requireContext(), "No user data found.", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("HomeFragment", "get failed with ", exception)
//                    Toast.makeText(requireContext(), "Error fetching user data.", Toast.LENGTH_SHORT).show()
//                }
//        } else {
//            Log.d("HomeFragment", "User not logged in")
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.btnLogout.setOnClickListener {
//            logoutUser()
//        }
//
//    }
//    private fun logoutUser() {
//        // Sign out the user
//        auth.signOut()
//
//        // Navigate back to login fragment
//        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
//
//        // Show logout success message
//        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
//    }
//
//
//}
