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
import com.veen.homechefrider.databinding.FragmentReceivedPaymentsBinding
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

class ReceivedPayments : Fragment() {
    private lateinit var binding: FragmentReceivedPaymentsBinding
    private lateinit var adapter: ReceivedAdapter
    private var currentDate: String? = null
    private var DOB: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_received_payments, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_received_payments, container, false)

        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())
            getreceived(getsaveToken,getsaveLogin)

            binding.receivedSubmit.setOnClickListener {
                if (binding.receivedSearch.text.toString() == "") {
                    if (TextUtils.isEmpty(binding.receivedFromDate.text.toString())) {
                        binding.receivedFromDate.setError("Field can't be empty!")
                    } else if (TextUtils.isEmpty(binding.receivedToDate.text.toString())) {
                        binding.receivedToDate.setError("Field can't be empty!")
                    } else {
                        getreceivedDate(getsaveToken,getsaveLogin)
                    }
                } else {
                    getreceivedSearch(getsaveToken,getsaveLogin)
                }

            }

            binding.receivedReset.setOnClickListener {
                binding.receivedSearch.text.clear()
                binding.receivedFromDate.text = ""
                binding.receivedToDate.text = ""
                getreceived(getsaveToken,getsaveLogin)
            }

            val calender = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            currentDate = df.format(calender)

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            binding.receivedFromDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.receivedFromDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }

            binding.receivedToDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.receivedToDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun getreceivedSearch(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.receivedpayments(getsaveToken, ReportReq(
                    "",
                    binding.receivedSearch.text.toString(),
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

    private fun getreceivedDate(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.receivedpayments(getsaveToken, ReportReq(
                    binding.receivedToDate.text.toString(),
                    "",
                    1,
                    binding.receivedFromDate.text.toString(),
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

    private fun getreceived(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.receivedpayments(getsaveToken, ReportReq(
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
            binding.receivedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ReceivedAdapter(requireContext(), list, data)
            binding.receivedRecyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}