package com.veen.homechefrider.model.order.checkstatus

data class CheckStatusReq(
    val chef_id: Int,
    val order_id: Int,
    val user_id: Int
)