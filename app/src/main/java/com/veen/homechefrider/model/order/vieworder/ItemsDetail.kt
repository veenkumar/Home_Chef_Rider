package com.veen.homechefrider.model.order.vieworder

data class ItemsDetail(
    val created_at: String,
    val food_name: String,
    val id: String,
    val image: String,
    val order_status: String,
    val plate_size: String,
    val price: String,
    val qty: String,
    val subtotal: Int
)