package com.smartherd.kini.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smartherd.kini.fragment.Shopping.ProductListFragment

class CategoryPagerAdapter(
    private val fragments: List<Fragment>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}


//class CategoryPagerAdapter(
//    private val fragments: List<Fragment>,
//    fragmentActivity: FragmentActivity
//) : FragmentStateAdapter(fragmentActivity) {
//
//    override fun getItemCount(): Int = fragments.size
//
//    override fun createFragment(position: Int): Fragment = fragments[position]
//}

