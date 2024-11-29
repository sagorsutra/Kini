package com.smartherd.kini.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.smartherd.kini.R
import com.smartherd.kini.databinding.ActivityShoppingBinding
import com.smartherd.kini.fragment.PrehomeFragment
import com.smartherd.kini.fragment.Shopping.CartFragment
import com.smartherd.kini.fragment.Shopping.HomeFragment
import com.smartherd.kini.fragment.Shopping.ProfileFragment
import com.smartherd.kini.fragment.Shopping.SearchFragment


class ShoppingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        replacefragment(PrehomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment ->replacefragment(HomeFragment())
                R.id.searchFragment ->replacefragment(SearchFragment())
                R.id.cartFragment ->replacefragment(CartFragment())
                R.id.profileFragment ->replacefragment(ProfileFragment())

            }
            true
        }

        }

    private fun replacefragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}