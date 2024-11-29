package com.smartherd.kini.fragment.catagory

import androidx.fragment.app.Fragment


class BaseCategoryFragment : Fragment() {

}



















//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: ProductAdapter
//    private val productList = mutableListOf<Product>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_base_category, container, false)
//
//        recyclerView = view.findViewById(R.id.rvOfferProducts)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = ProductAdapter(productList)
//        recyclerView.adapter = adapter
//
//        val categoryId = arguments?.getString("category_id")
//        if (categoryId != null) {
//            fetchProductsForCategory(categoryId)
//        }
//
//        return view
//    }
//
//    private fun fetchProductsForCategory(categoryId: String) {
//        val databaseReference = FirebaseDatabase.getInstance().getReference("categories/$categoryId/products")
//
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                productList.clear()
//
//                for (productSnapshot in snapshot.children) {
//                    val product = productSnapshot.getValue(Product::class.java)
//                    product?.let { productList.add(it) }
//                }
//
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
//    }
//
//    companion object {
//        fun newInstance(categoryId: String): BaseCategoryFragment {
//            val fragment = BaseCategoryFragment()
//            val args = Bundle()
//            args.putString("category_id", categoryId)
//            fragment.arguments = args
//            return fragment
//        }
//    }
