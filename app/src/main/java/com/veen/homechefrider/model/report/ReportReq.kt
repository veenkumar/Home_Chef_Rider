package com.veen.homechefrider.model.report

data class ReportReq(
    val end_date: String,
    val order_id: String,
    val page: Int,
    val start_date: String,
    val user_id: Int
)