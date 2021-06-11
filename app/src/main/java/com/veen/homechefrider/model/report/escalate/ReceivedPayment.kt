package com.veen.homechefrider.model.report.escalate

data class ReceivedPayment(
    val created_at: String,
    val delivered_time: String,
    val delivery_time: String,
    val order_id: String,
    val total_amount: Int
)