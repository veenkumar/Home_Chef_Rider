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
import com.veen.homechefrider.adapter.ViewComplainAdapter
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentViewComplainBinding
import com.veen.homechefrider.model.complain.view.ViewComplaintData
import com.veen.homechefrider.model.complain.view.ViewComplaintReq
import com.veen.homechefrider.model.complain.view.ViewComplaintRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewComplain : Fragment() {
    private lateinit var binding: FragmentViewComplainBinding
    private lateinit var adapter: ViewComplainAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_view_complain, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_complain, container, false)
        val getsaveToken = AppUtils.getsaveToken(requireContext())
        val getsaveLogin = AppUtils.getsaveLogin(requireContext())
        ComplainList(getsaveToken, getsaveLogin)

        return binding.root
    }

    private fun ComplainList(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.complainlist(getsaveToken, ViewComplaintReq(
                1,
                getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ViewComplaintRes> {
                override fun onResponse(
                    call: Call<ViewComplaintRes>,
                    response: Response<ViewComplaintRes>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT)
                            .show()
                        bindMyComplainlist(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<ViewComplaintRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMyComplainlist(data: List<ViewComplaintData>) {
        try {
            binding.viewComplainRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ViewComplainAdapter(requireContext(), data)
            binding.viewComplainRecyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}