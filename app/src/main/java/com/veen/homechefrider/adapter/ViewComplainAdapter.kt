package com.veen.homechefrider.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.model.complain.view.ViewComplaintData
import com.veen.homechefrider.model.message.MessageReq
import com.veen.homechefrider.model.message.MessageRes
import com.veen.homechefrider.utils.AppUtils
import kotlinx.android.synthetic.main.item_list5.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewComplainAdapter(private val context: Context, private val data: List<ViewComplaintData>) :
    RecyclerView.Adapter<ViewComplainAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewHeading = itemView.findViewById<TextView>(R.id.viewHeading)
        val viewDate = itemView.findViewById<TextView>(R.id.viewDate)
        val viewDiscription = itemView.findViewById<TextView>(R.id.viewDiscription)
        val viewMessage = itemView.findViewById<TextView>(R.id.viewMessage)
        val replyButton = itemView.findViewById<Button>(R.id.replyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list4, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.viewHeading.text = data[position].subject
        holder.viewDate.text = data[position].created_at
        holder.viewDiscription.text = data[position].message

        holder.viewMessage.setOnClickListener {
            val getsaveToken = AppUtils.getsaveToken(context)
            var MessageID = data[position].id
            val mdialogview =
                LayoutInflater.from(context).inflate(R.layout.item_list5, null)
            var messageHeading = mdialogview.findViewById<TextView>(R.id.messageHeading)
            var messageSubject = mdialogview.findViewById<TextView>(R.id.messageSubject)
            var messageStatus = mdialogview.findViewById<TextView>(R.id.messageStatus)
            var messageReply = mdialogview.findViewById<TextView>(R.id.messageReply)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mdialogview)

            val mAlertDialog = mBuilder.show()

            try {
                RetrofitInstance.instence?.viewmessgae(getsaveToken, MessageReq(
                    MessageID.toInt()
                ))!!.enqueue(object : Callback<MessageRes> {
                    override fun onResponse(
                        call: Call<MessageRes>,
                        response: Response<MessageRes>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                context,
                                "" + response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()

                            messageHeading.text = response.body()!!.data.subject
                            messageSubject.text = response.body()!!.data.message
                            messageStatus.text = response.body()!!.data.reply_status
                            if (response.body()!!.data.reply_message != null) {
                                messageReply.text = response.body()!!.data.reply_message.toString()
                            } else {
                                messageReply.text = "N/A"
                            }
                        }
                    }

                    override fun onFailure(call: Call<MessageRes>, t: Throwable) {
                        Log.d("TAG", "onFailure: Failed")
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }


            mdialogview.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}