package com.veen.homechefrider.model.order.status

data class StatusReq(
    val chef_id: Int,
    val order_id: Int,
    val order_status: Int,
    val user_id: Int
)