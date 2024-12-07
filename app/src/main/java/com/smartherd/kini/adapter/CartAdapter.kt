package com.smartherd.kini.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartherd.kini.R
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.databinding.CartProductItemBinding

class CartAdapter(
    private val cartItems: MutableList<CartProduct>,
    private val onIncreaseQuantity: (CartProduct) -> Unit,
    private val onDecreaseQuantity: (CartProduct) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvProductCartName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvProductCartPrice)
        val quantityTextView: TextView = itemView.findViewById(R.id.tvCartProductQuantity)
        val imageView: ImageView = itemView.findViewById(R.id.imageCartProduct)
        val increaseButton: ImageView = itemView.findViewById(R.id.imagePlus)
        val decreaseButton: ImageView = itemView.findViewById(R.id.imageMinus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_product_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartProduct = cartItems[position]

        holder.nameTextView.text = cartProduct.name
        holder.priceTextView.text = cartProduct.price
        holder.quantityTextView.text = cartProduct.quantity.toString()
        Glide.with(holder.itemView.context).load(cartProduct.imageUrl).into(holder.imageView)

        holder.increaseButton.setOnClickListener { onIncreaseQuantity(cartProduct) }
        holder.decreaseButton.setOnClickListener { onDecreaseQuantity(cartProduct) }
    }

    override fun getItemCount() = cartItems.size
}


//class CartAdapter(
//    private val cartItems: List<CartProduct>,
//    private val onIncreaseQuantity: (CartProduct) -> Unit,
//    private val onDecreaseQuantity: (CartProduct) -> Unit
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val productName: TextView = itemView.findViewById(R.id.tvProductCartName)
//        val productPrice: TextView = itemView.findViewById(R.id.tvProductCartPrice)
//        val productQuantity: TextView = itemView.findViewById(R.id.tvCartProductQuantity)
//        val productImage: ImageView = itemView.findViewById(R.id.imageCartProduct)
//        val increaseQuantity: ImageView = itemView.findViewById(R.id.imagePlus)
//        val decreaseQuantity: ImageView = itemView.findViewById(R.id.imageMinus)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
//        return CartViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        val cartProduct = cartItems[position]
//        holder.productName.text = cartProduct.name
//        holder.productPrice.text = "$${cartProduct.price}"
//        holder.productQuantity.text = cartProduct.quantity.toString()
//        Glide.with(holder.itemView.context).load(cartProduct.imageUrl).into(holder.productImage)
//
//        holder.increaseQuantity.setOnClickListener { onIncreaseQuantity(cartProduct) }
//        holder.decreaseQuantity.setOnClickListener { onDecreaseQuantity(cartProduct) }
//    }
//
//    override fun getItemCount(): Int = cartItems.size
//}


//class CartAdapter(
//    private val cartItems: MutableList<CartProduct>,
//    private val onQuantityChanged: (CartProduct) -> Unit,
//    private val onRemoveItem: (CartProduct) -> Unit
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    inner class CartViewHolder(private val binding: CartProductItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(cartProduct: CartProduct) {
//            // Set product name, price, and quantity
//            binding.tvProductCartName.text = cartProduct.name
//            binding.tvProductCartPrice.text = "$${cartProduct.price}"
//            binding.tvCartProductQuantity.text = cartProduct.quantity.toString()
//
//            // Load the product image using Glide
//            Glide.with(binding.root.context)
//                .load(cartProduct.imageUrl)
//                .into(binding.imageCartProduct)
//
//            // Handle quantity increase
//            binding.imagePlus.setOnClickListener {
//                cartProduct.quantity++
//                binding.tvCartProductQuantity.text = cartProduct.quantity.toString()
//                onQuantityChanged(cartProduct)
//            }
//
//            // Handle quantity decrease
//            binding.imageMinus.setOnClickListener {
//                if (cartProduct.quantity > 1) {
//                    cartProduct.quantity--
//                    binding.tvCartProductQuantity.text = cartProduct.quantity.toString()
//                    onQuantityChanged(cartProduct)
//                } else {
//                    onRemoveItem(cartProduct)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val binding = CartProductItemBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return CartViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        holder.bind(cartItems[position])
//    }
//
//    override fun getItemCount(): Int = cartItems.size
//
//    // Method to update the list of cart items
//    fun updateCartItems(newCartItems: List<CartProduct>) {
//        cartItems.clear()
//        cartItems.addAll(newCartItems)
//        notifyDataSetChanged()
//    }
//}
