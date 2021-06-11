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
import com.veen.homechefrider.adapter.EscalatedAdapter
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentEscalatedOrdersBinding
import com.veen.homechefrider.model.report.ReportReq
import com.veen.homechefrider.model.report.escalate.EscalateData
import com.veen.homechefrider.model.report.escalate.EscalateRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EscalatedOrders : Fragment() {
    private lateinit var binding: FragmentEscalatedOrdersBinding
    private lateinit var adapter: EscalatedAdapter
    private var currentDate: String? = null
    private var DOB: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_escalated_orders, container, false)

        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())
            getescalate(getsaveToken,getsaveLogin)

            binding.escalateSubmit.setOnClickListener {
                if (binding.escalateSearch.text.toString() == "") {
                    if (TextUtils.isEmpty(binding.escalateFromDate.text.toString())) {
                        binding.escalateFromDate.setError("Field can't be empty!")
                    } else if (TextUtils.isEmpty(binding.escalateToDate.text.toString())) {
                        binding.escalateToDate.setError("Field can't be empty!")
                    } else {
                        getescalateDate(getsaveToken,getsaveLogin)
                    }
                } else {
                    getescalateSearch(getsaveToken,getsaveLogin)
                }

            }

            binding.escalateReset.setOnClickListener {
                binding.escalateSearch.text.clear()
                binding.escalateFromDate.text = ""
                binding.escalateToDate.text = ""
                getescalate(getsaveToken,getsaveLogin)
            }

            val calender = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            currentDate = df.format(calender)

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            binding.escalateFromDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.escalateFromDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }

            binding.escalateToDate.setOnClickListener {
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    if (month < 9) {
                        DOB = year.toString() + "-" + "0" + (month + 1) + "-" + dayOfMonth
                    } else {
                        DOB = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                    }
                    binding.escalateToDate.setText(DOB)
                }, year, month, day)
                dpd.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun getescalateSearch(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.escalatedorder(getsaveToken, ReportReq(
                    "",
                    binding.escalateSearch.text.toString(),
                    1,
                    "",
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<EscalateRes> {
                override fun onResponse(call: Call<EscalateRes>, response: Response<EscalateRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<EscalateRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getescalateDate(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.escalatedorder(getsaveToken, ReportReq(
                    binding.escalateToDate.text.toString(),
                    "",
                    1,
                    binding.escalateFromDate.text.toString(),
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<EscalateRes> {
                override fun onResponse(call: Call<EscalateRes>, response: Response<EscalateRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<EscalateRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getescalate(getsaveToken: String, getsaveLogin: String) {
        try {
            RetrofitInstance.instence?.escalatedorder(getsaveToken, ReportReq(
                    "",
                    "",
                    1,
                    "",
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<EscalateRes> {
                override fun onResponse(call: Call<EscalateRes>, response: Response<EscalateRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        bindMyData(response.body()!!.data, response.body()!!.data.received_payment)
                    }
                }

                override fun onFailure(call: Call<EscalateRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMyData(list: EscalateData, data: List<com.veen.homechefrider.model.report.escalate.ReceivedPayment>) {
        try {
            binding.escalateRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = EscalatedAdapter(requireContext(), list, data)
            binding.escalateRecyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}