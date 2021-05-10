package com.veen.homechefrider.fragment.side

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentDashboardBinding
import com.veen.homechefrider.model.dashboard.DashboardReq
import com.veen.homechefrider.model.dashboard.DashboardRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        val getsaveToken = AppUtils.getsaveToken(requireContext())
        val getsaveLogin = AppUtils.getsaveLogin(requireContext())

        getDashboard(getsaveToken, getsaveLogin)

        return binding.root
    }

    private fun getDashboard(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.viewdashboard(getsaveToken, DashboardReq(
                getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<DashboardRes> {
                override fun onResponse(call: Call<DashboardRes>, response: Response<DashboardRes>) {
                    if (response.isSuccessful) {
                        binding.dashboardOrder.text = response.body()!!.data.total_order.toString()
                        binding.dashboardTimeOrder.text = response.body()!!.data.count_on_time.toString()
                        binding.dashboardLateOrder.text = response.body()!!.data.count_late_time.toString()
                    }
                }

                override fun onFailure(call: Call<DashboardRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}