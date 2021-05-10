package com.veen.homechefrider.fragment.side

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechefrider.R
import com.veen.homechefrider.adapter.AssignOrderAdapter
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentOrderListBinding
import com.veen.homechefrider.model.order.assign.AssignData
import com.veen.homechefrider.model.order.assign.AssignReq
import com.veen.homechefrider.model.order.assign.AssignRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderList : Fragment() {
    private lateinit var binding: FragmentOrderListBinding
    private lateinit var orderRecycler: RecyclerView
    private lateinit var adapter: AssignOrderAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentOrderListBinding>(inflater, R.layout.fragment_order_list, container, false)
        orderRecycler = binding.orderRecycler
        val getsaveToken = AppUtils.getsaveToken(requireContext())
        val getsaveLogin = AppUtils.getsaveLogin(requireContext())

        OrderlistData(getsaveToken, getsaveLogin)

        return binding.root
    }

    private fun OrderlistData(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.assignorder(getsaveToken, AssignReq(
                getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<AssignRes> {
                override fun onResponse(call: Call<AssignRes>, response: Response<AssignRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        bindmyAssignOrder(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<AssignRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindmyAssignOrder(data: List<AssignData>) {
        try {
            orderRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = AssignOrderAdapter(requireContext(), data)
            orderRecycler.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}