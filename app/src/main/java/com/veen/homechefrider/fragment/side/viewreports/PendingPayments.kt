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
import com.veen.homechefrider.adapter.ReceivedAdapter
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentPendingPaymentsBinding
import com.veen.homechefrider.model.report.ReportReq
import com.veen.homechefrider.model.report.received.ReceivedData
import com.veen.homechefrider.model.report.received.ReceivedPayment
import com.veen.homechefrider.model.report.received.ReceivedRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PendingPayments : Fragment() {
    private lateinit var binding: FragmentPendingPaymentsBinding
    private lateinit var adapter: ReceivedAdapter
    private var currentDate: String? = null
    private var DOB: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending_payments, container, false)

        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())
            getpending(getsaveToken,getsaveLogin)

            binding.pendingSubmit.setOnClickListener {
                if (binding.pendingSearch.text.toString() == "") {
                    if (TextUtils.isEmpty(binding.pendingFromDate.text.toString())) {
                        binding.pendingFromDate.setError("Field can't be empty!")
                    } else if (TextUtils.isEmpty(binding.pendingToDate.text.toString())) {
                        binding.pendingToDate.setError("Field can't be empty!")
                    } else {
                        getpendingDate(getsaveToken,getsaveLogin)
                    }
                } else {
                    getpendingSearch(getsaveToken,getsaveLogin)
                }

            }

            binding.pendingReset.setOnClickListener {
                binding.pendingSearch.text.clear()
                binding.pendingFromDate.text = ""
                binding.pendingToDate.text = ""
                getpending(getsaveToken,getsaveLogin)
            }

            val calender = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            currentDate = df.format(calender)

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            binding.pendingFromDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.pendingFromDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }

            binding.pendingToDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.pendingToDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun getpendingSearch(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.pendingpayments(getsaveToken, ReportReq(
                    "",
                    binding.pendingSearch.text.toString(),
                    1,
                    "",
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ReceivedRes> {
                override fun onResponse(call: Call<ReceivedRes>, response: Response<ReceivedRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<ReceivedRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getpendingDate(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.pendingpayments(getsaveToken, ReportReq(
                    binding.pendingToDate.text.toString(),
                    "",
                    1,
                    binding.pendingFromDate.text.toString(),
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ReceivedRes> {
                override fun onResponse(call: Call<ReceivedRes>, response: Response<ReceivedRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<ReceivedRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getpending(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.pendingpayments(getsaveToken, ReportReq(
                    "",
                    "",
                    1,
                    "",
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ReceivedRes> {
                override fun onResponse(call: Call<ReceivedRes>, response: Response<ReceivedRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<ReceivedRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMyData(list: ReceivedData, data: List<ReceivedPayment>) {
        try {
            binding.pendingRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ReceivedAdapter(requireContext(), list, data)
            binding.pendingRecyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}