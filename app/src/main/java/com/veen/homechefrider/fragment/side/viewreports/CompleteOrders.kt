package com.veen.homechefrider.fragment.side.viewreports

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
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
import com.veen.homechefrider.adapter.CompleteAdapter
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentCompleteOrdersBinding
import com.veen.homechefrider.model.report.ReportReq
import com.veen.homechefrider.model.report.complete.CompleteData
import com.veen.homechefrider.model.report.complete.CompleteRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CompleteOrders : Fragment() {
    private lateinit var binding: FragmentCompleteOrdersBinding
    private lateinit var adapter: CompleteAdapter
    private var currentDate: String? = null
    private var DOB: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_complete_orders, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complete_orders, container, false)

        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())
            getComplete(getsaveToken,getsaveLogin)

            binding.completeSubmit.setOnClickListener {
                if (binding.completeSearch.text.toString() == "") {
                    if (TextUtils.isEmpty(binding.completeFromDate.text.toString())) {
                        binding.completeFromDate.setError("Field can't be empty!")
                    } else if (TextUtils.isEmpty(binding.completeToDate.text.toString())) {
                        binding.completeToDate.setError("Field can't be empty!")
                    } else {
                        getCompleteDate(getsaveToken,getsaveLogin)
                    }
                } else {
                    getCompleteSearch(getsaveToken,getsaveLogin)
                }

            }

            binding.completeReset.setOnClickListener {
                binding.completeSearch.text.clear()
                binding.completeFromDate.text = ""
                binding.completeToDate.text = ""
                getComplete(getsaveToken,getsaveLogin)
            }

            val calender = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            currentDate = df.format(calender)

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            binding.completeFromDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.completeFromDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }

            binding.completeToDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.completeToDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun getCompleteSearch(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.completeorder(getsaveToken, ReportReq(
                    "",
                    binding.completeSearch.text.toString(),
                    1,
                    "",
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<CompleteRes> {
                override fun onResponse(call: Call<CompleteRes>, response: Response<CompleteRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<CompleteRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCompleteDate(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.completeorder(getsaveToken, ReportReq(
                    binding.completeToDate.text.toString(),
                    "",
                    1,
                    binding.completeFromDate.text.toString(),
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<CompleteRes> {
                override fun onResponse(call: Call<CompleteRes>, response: Response<CompleteRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<CompleteRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getComplete(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.completeorder(getsaveToken, ReportReq(
                    "",
                    "",
                    1,
                    "",
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<CompleteRes> {
                override fun onResponse(call: Call<CompleteRes>, response: Response<CompleteRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<CompleteRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMyData(list: CompleteData, data: List<com.veen.homechefrider.model.report.complete.ReceivedPayment>) {
        try {
            binding.completeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = CompleteAdapter(requireContext(), list, data)
            binding.completeRecyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}