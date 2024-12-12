package com.smartherd.kini.data


data class Order(
    var id: String = "",
    var address: String = "",
    var products: List<Product> = emptyList(),
    var shippingCost: Double = 0.0,
    var subtotal: Double = 0.0,
    var tax: Double = 0.0,
    var totalPrice: Double = 0.0,
    var userId: String = "",
    var timestamp: Long = 0L,
    var status: String = "pending",
    var date: String = "",

    )

