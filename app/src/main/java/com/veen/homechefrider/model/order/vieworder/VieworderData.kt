package com.veen.homechefrider.model.order.vieworder

data class VieworderData(
    val address: String,
    val chef_address: String,
    val chef_id: String,
    val chef_name: String,
    val chef_phone: String,
    val delivery_charge: Int,
    val discount_amount: Int,
    val email: String,
    val items_detail: List<ItemsDetail>,
    val name: String,
    val order_id: String,
    val order_status_list: List<OrderStatus>,
    val payment_type: String,
    val phone_no: String,
    val pincode: String,
    val total_amount: Int
)