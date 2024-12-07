package com.smartherd.kini.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartherd.kini.data.CartProduct
import com.smartherd.kini.data.Product

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<CartProduct>>()
    val cartItems: LiveData<MutableList<CartProduct>> get() = _cartItems

    init {
        _cartItems.value = mutableListOf()
    }

    fun addToCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        val cartProduct = CartProduct(
            id = product.id,
            name = product.name,
            price = product.price,
            quantity = 1, // You can modify this based on your requirement
            imageUrl = product.imageUrl
        )

        currentList.add(cartProduct)
        _cartItems.value = currentList // Update the LiveData
    }

    fun setCartItems(cartItems: List<CartProduct>) {
        _cartItems.value = cartItems.toMutableList()
    }
}


//class CartViewModel : ViewModel() {
//
//    private val _cartItems = MutableLiveData<List<CartProduct>>(emptyList())
//    val cartItems: LiveData<List<CartProduct>> get() = _cartItems
//
//    // Set cart items
//    fun setCartItems(items: List<CartProduct>) {
//        _cartItems.value = items
//    }
//
//    // Add item to cart
//    fun addToCart(item: CartProduct) {
//        val updatedList = _cartItems.value.orEmpty().toMutableList()
//        updatedList.add(item)
//        _cartItems.value = updatedList
//    }
//
//    // Remove item from cart
//    fun removeFromCart(item: CartProduct) {
//        val updatedList = _cartItems.value.orEmpty().toMutableList()
//        updatedList.remove(item)
//        _cartItems.value = updatedList
//    }
//}
