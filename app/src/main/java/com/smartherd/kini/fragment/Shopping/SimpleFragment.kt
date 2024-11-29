package com.smartherd.kini.fragment.Shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smartherd.kini.R

class SimpleFragment : Fragment() {

    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString(ARG_CATEGORY) ?: "Unknown"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simple, container, false)
        val textView: TextView = view.findViewById(R.id.textView)
        textView.text = "Category: $category"
        return view
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String) = SimpleFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
        }
    }
}
