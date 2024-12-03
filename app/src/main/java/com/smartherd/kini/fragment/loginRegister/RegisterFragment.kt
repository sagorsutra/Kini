package com.smartherd.kini.fragment.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.kini.R
import com.smartherd.kini.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using the binding class
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        firestore = FirebaseFirestore.getInstance()


        // Handle registration
        binding.buttonRegisterRegister.setOnClickListener {

            val firstName = binding.edFirstNameRegister.text.toString().trim()
            val lastName = binding.edLastNameRegister.text.toString().trim()
            val email = binding.edEmailRegister.text.toString().trim()
            val password = binding.edPasswordRegister.text.toString().trim()


            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(firstName, lastName, email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }

        }
        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Registration success, now store additional user data in Firestore
                    val user = auth.currentUser
                    val userId = user?.uid

                    if (userId != null) {
                        // Save user data to Firestore
                        saveUserToFirestore(userId, firstName, lastName, email)
                    }
                } else {
                    Log.w("RegisterFragment", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Registration failed. Please check your input.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirestore(userId: String, firstName: String, lastName: String, email: String) {
        // Create a user object with additional data
        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email
        )

        // Save user data to Firestore
        firestore.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("RegisterFragment", "User data saved to Firestore")

                // Initialize user data (cart and orders)
                initializeUserData(userId)

            }
            .addOnFailureListener { e ->
                Log.w("RegisterFragment", "Error saving user data", e)
                Toast.makeText(requireContext(), "Failed to save user data.", Toast.LENGTH_SHORT).show()
            }
    }
    private fun initializeUserData(userId: String) {
        // Initialize user data (empty cart and orders)
        val defaultUserData = hashMapOf(
            "cart" to hashMapOf<String, Int>(),  // Empty cart initially
            "orders" to listOf<String>(),        // Empty orders initially
        )

        // Save default user data to Firestore
        firestore.collection("users").document(userId)
            .update(defaultUserData)
            .addOnSuccessListener {
                Log.d("RegisterFragment", "User data initialized")
                updateUI()
            }
            .addOnFailureListener { e ->
                Log.w("RegisterFragment", "Error initializing user data", e)
                Toast.makeText(requireContext(), "Failed to initialize user data.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI() {

            // Navigate back to login fragment using NavController
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            // You can show a message to inform the user that registration was successful
            Toast.makeText(requireContext(), "Registration successful! Please login.", Toast.LENGTH_SHORT).show()
    }
}



//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//         }
//    }

//    private fun signup(){
//
//
//        val user = users(firstName,lastName,email)
//        val UserID = FirebaseAuth.getInstance().currentUser!!.uid
//
//        database.child("User").child(UserID).setValue(user)
//
//    }

//    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
//        // Create user with email and password
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // Registration success, now store additional user data in Firestore
//                    val user = auth.currentUser
//                    val userId = user?.uid
//
//                    if (userId != null) {
//                        saveUserToFirestore(userId, firstName, lastName, email)
//                    }
//                } else {
//                    Log.w("RegisterFragment", "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(requireContext(), "Registration failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//