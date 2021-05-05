package com.veen.homechefrider.model.order.vieworder

data class VieworderReq(
    val chef_id: Int,
    val order_id: Int,
    val user_id: Int
)