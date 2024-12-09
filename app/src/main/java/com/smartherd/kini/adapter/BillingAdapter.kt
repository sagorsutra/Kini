package com.smartherd.kini.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartherd.kini.R
import com.smartherd.kini.data.CartProduct

// BillingAdapter.kt
class BillingAdapter : ListAdapter<CartProduct, BillingAdapter.BillingViewHolder>(CartProductDiffCallback()) {

    inner class BillingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvProductCartName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvProductCartPrice)
        val quantityTextView: TextView = itemView.findViewById(R.id.tvBillingProductQuantity)
        val productImageView: ImageView = itemView.findViewById(R.id.imageCartProduct)  // Add this line for the image

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.billing_products_rv_item, parent, false)
        return BillingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val product = getItem(position)
        holder.nameTextView.text = product.name
        holder.priceTextView.text = product.price
        holder.quantityTextView.text = "Quantity: ${product.quantity}"
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)  // Make sure 'imageUrl' is a valid URL or URI
            .into(holder.productImageView)
    }

    // This method is needed for `ListAdapter` to compare items efficiently.
    class CartProductDiffCallback : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }
}


//class BillingAdapter(private val cartItems: List<CartProduct>) :
//    RecyclerView.Adapter<BillingAdapter.BillingViewHolder>() {
//
//    inner class BillingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val productName: TextView = itemView.findViewById(R.id.tvProductCartName)
//        val productPrice: TextView = itemView.findViewById(R.id.tvProductCartPrice)
//        val productQuantity: TextView = itemView.findViewById(R.id.tvBillingProductQuantity)
//        val productImage: ImageView = itemView.findViewById(R.id.imageCartProduct)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.billing_products_rv_item, parent, false)
//        return BillingViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
//        val cartProduct = cartItems[position]
//        holder.productName.text = cartProduct.name
//        holder.productPrice.text = "$${cartProduct.price}"
//        holder.productQuantity.text = "Qty: ${cartProduct.quantity}"
//        Glide.with(holder.itemView.context).load(cartProduct.imageUrl).into(holder.productImage)
//    }
//
//    override fun getItemCount() = cartItems.size
//}
