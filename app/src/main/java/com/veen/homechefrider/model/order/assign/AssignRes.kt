package com.veen.homechefrider.model.order.assign

data class AssignRes(
        val `data`: List<AssignData>,
        val msg: String,
        val status: Boolean
)