package com.smartherd.kini.data

data class CartProduct(
    var id: String = "",       // Default empty string for Firebase
    var name: String = "",     // Default empty string for Firebase
    var price: String = "",    // Default empty string for Firebase
    var quantity: Int = 0,     // Default zero for Firebase
    var imageUrl: String = ""  // Default empty string for Firebase
)

