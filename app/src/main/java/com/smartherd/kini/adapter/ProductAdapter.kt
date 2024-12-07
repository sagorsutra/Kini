package com.smartherd.kini.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.smartherd.kini.R
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.data.Product
import com.smartherd.kini.viewmodel.CartViewModel

class ProductAdapter(
    private val productList: List<Product>,
    private val onAddToCart: (CartProduct) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvSpecialProductName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvSpecialPrdouctPrice)
        val imageView: ImageView = itemView.findViewById(R.id.imageSpecialRvItem)
        val addToCartButton: Button = itemView.findViewById(R.id.btn_add_to_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.nameTextView.text = product.name
        holder.priceTextView.text = product.price
        Glide.with(holder.itemView.context).load(product.imageUrl).into(holder.imageView)

        holder.addToCartButton.setOnClickListener {
            val cartProduct = CartProduct(
                id = product.id,
                name = product.name,
                price = product.price,
                quantity = 1,
                imageUrl = product.imageUrl
            )

            // Pass the product to the cart logic
            onAddToCart(cartProduct)

            // Show a Toast confirmation
            Toast.makeText(
                holder.itemView.context,
                "${product.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount() = productList.size
}



//class ProductAdapter(
//    private val productList: List<Product>,
//    private val cartViewModel: CartViewModel
//) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
//
//    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val nameTextView: TextView = itemView.findViewById(R.id.tvSpecialProductName)
//        val priceTextView: TextView = itemView.findViewById(R.id.tvSpecialPrdouctPrice)
//        val imageView: ImageView = itemView.findViewById(R.id.imageSpecialRvItem)
//        val addToCartButton: Button = itemView.findViewById(R.id.btn_add_to_cart)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_product, parent, false)
//        return ProductViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = productList[position]
//        holder.nameTextView.text = product.name
//        holder.priceTextView.text = product.price
//        Glide.with(holder.itemView.context).load(product.imageUrl).into(holder.imageView)
//
//        holder.addToCartButton.setOnClickListener {
//            val databaseReference = FirebaseDatabase.getInstance().reference.child("cart")
//            val cartProduct = CartProduct(
//                id = product.id,
//                name = product.name,
//                price = product.price,
//                quantity = 1,
//                imageUrl = product.imageUrl
//            )
//
//            databaseReference.child(cartProduct.id).setValue(cartProduct)
//                .addOnSuccessListener {
//                    Toast.makeText(holder.itemView.context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(holder.itemView.context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//
//    override fun getItemCount() = productList.size
//}
//