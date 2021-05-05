package com.veen.homechefrider.model.order.assign

data class AssignData(
    val chef_id: String,
    val created_at: String,
    val food_name: String,
    val no_of_item: String,
    val order_id: String,
    val order_status: String
)