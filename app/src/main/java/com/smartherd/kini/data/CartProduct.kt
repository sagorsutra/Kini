package com.smartherd.kini.data

data class CartProduct(
    val id: String = "",          // ID won't change once set
    val name: String = "",        // Name remains constant
    val price: String = "",       // Price remains constant
    var quantity: Int = 0,        // Quantity can change as the user modifies the cart
    val imageUrl: String = ""     // Image URL stays constant
)

