package com.veen.homechefrider.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechefrider.R
import com.veen.homechefrider.model.report.complete.CompleteData

class CompleteAdapter(private val context: Context, private val list: CompleteData, private val data: List<com.veen.homechefrider.model.report.complete.ReceivedPayment>) :
        RecyclerView.Adapter<CompleteAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etorderID = itemView.findViewById<TextView>(R.id.etorderID)
        val etorderdateID = itemView.findViewById<TextView>(R.id.etorderdateID)
        val etorderamountID = itemView.findViewById<TextView>(R.id.etorderamountID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list6, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.etorderID.text = data[position].order_id
        holder.etorderdateID.text = data[position].created_at
        holder.etorderamountID.text = list.currency + " " + data[position].total_amount.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}