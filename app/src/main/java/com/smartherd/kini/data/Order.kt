package com.smartherd.kini.data

data class Order(
    val orderId: String? = null,
    val products: Map<String, Int>? = null, // productId -> quantity
    val status: String? = null
)