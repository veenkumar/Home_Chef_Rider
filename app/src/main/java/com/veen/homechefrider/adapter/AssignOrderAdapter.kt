package com.veen.homechefrider.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechefrider.R
import com.veen.homechefrider.fragment.side.OrderListDirections
import com.veen.homechefrider.model.order.assign.AssignData

class AssignOrderAdapter(private val context: Context, private val data: List<AssignData>) :
        RecyclerView.Adapter<AssignOrderAdapter.ItemViewAbout> () {
    var orderID = ""
    var chefID = ""
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderID = itemView.findViewById<TextView>(R.id.orderID)
        val orderNumbItem = itemView.findViewById<TextView>(R.id.orderNumbItem)
        val orderFood = itemView.findViewById<TextView>(R.id.orderFood)
        val orderStatus = itemView.findViewById<TextView>(R.id.orderStatus)
        val orderTime = itemView.findViewById<TextView>(R.id.orderTime)
        val orderReceived = itemView.findViewById<TextView>(R.id.orderReceived)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list2, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.orderID.text = "Order Id: " + data[position].order_id
        holder.orderNumbItem.text = "Number of Item: " + data[position].no_of_item
        holder.orderFood.text = data[position].food_name
        holder.orderStatus.text = data[position].order_status
        holder.orderTime.text = data[position].created_at
        holder.orderReceived.setOnClickListener { view: View ->
            orderID = data[position].order_id
            chefID = data[position].chef_id
            view.findNavController().navigate(OrderListDirections.actionOrderListToViewOrder(orderID, chefID))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}