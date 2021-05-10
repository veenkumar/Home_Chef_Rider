package com.veen.homechefrider.model.complain.view

data class ViewComplaintRes(
    val `data`: List<ViewComplaintData>,
    val msg: String,
    val status: Boolean
)