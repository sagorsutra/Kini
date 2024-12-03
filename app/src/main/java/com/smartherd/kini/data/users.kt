package com.smartherd.kini.data

data class users(
//    val firstname : String?=null,
//    val lastname : String?=null,
//    val email : String?=null
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val cart: Map<String, Int>? = null, // productId -> quantity
    val orders: List<Order>? = null
)
