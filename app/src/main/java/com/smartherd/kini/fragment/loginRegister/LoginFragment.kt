package com.smartherd.kini.fragment.loginRegister

import android.content.Intent
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
import com.smartherd.kini.activity.ShoppingActivity
import com.smartherd.kini.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using the binding class
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Handle login button click
        binding.buttonLoginLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString().trim()
            val password = binding.edPasswordLogin.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Don't have an account? Register" click to navigate to RegisterFragment
        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Handle "Forgot password" click
        binding.tvForgotPasswordLogin.setOnClickListener {
            // Navigate to a reset password page if implemented, or you can add your own logic for password recovery here.
            Toast.makeText(requireContext(), "Forgot password functionality not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginFragment", "signInWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        // After successful login, fetch user data from Firestore
                        fetchUserData(it.uid)
                    }
                } else {
                    Log.w("LoginFragment", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed. Check your email and password.", Toast.LENGTH_SHORT).show()
                    updateUI()
                }
            }
    }

    private fun fetchUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User data exists, proceed to the main app
                    updateUI()
                } else {
                    // User data doesn't exist, initialize it with default values
                    initializeUserData(userId)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("LoginFragment", "Error getting user data: ", exception)
            }
    }

    private fun initializeUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        val defaultUserData = hashMapOf(
            "cart" to hashMapOf<String, Int>(),  // Empty cart initially
            "orders" to listOf<String>(),        // Empty orders list
        )

        userRef.set(defaultUserData)
            .addOnSuccessListener {
                Log.d("LoginFragment", "User data initialized")
                updateUI()
            }
            .addOnFailureListener { e ->
                Log.w("LoginFragment", "Error initializing user data", e)
                Toast.makeText(requireContext(), "Failed to initialize user data.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI() {

        val intent = Intent(requireActivity(), ShoppingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // This clears the backstack
        startActivity(intent)
        requireActivity().finish()

    }
}
