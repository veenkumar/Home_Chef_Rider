package com.veen.homechefrider.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.veen.homechefrider.R
import com.veen.homechefrider.model.order.vieworder.ItemsDetail

class ViewOrderAdapter(private val context: Context, private val data: List<ItemsDetail>) :
    RecyclerView.Adapter<ViewOrderAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewImage = itemView.findViewById<ImageView>(R.id.viewImage)
        val viewFood = itemView.findViewById<TextView>(R.id.viewFood)
        val viewPlateSize = itemView.findViewById<TextView>(R.id.viewPlateSize)
        val viewQty = itemView.findViewById<TextView>(R.id.viewQty)
        val viewPrice = itemView.findViewById<TextView>(R.id.viewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list3, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        Picasso.get().load(data[position].image).into(holder.viewImage)
        holder.viewFood.text = data[position].food_name
        holder.viewPlateSize.text = data[position].plate_size
        holder.viewQty.text = "Qty: " + data[position].qty
        holder.viewPrice.text = "Price: " + data[position].price
    }

    override fun getItemCount(): Int {
        return data.size
    }
}