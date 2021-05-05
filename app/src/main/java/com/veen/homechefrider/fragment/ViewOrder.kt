package com.veen.homechefrider.fragment

import android.content.Intent
import android.net.Uri
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
import com.veen.homechefrider.adapter.ViewOrderAdapter
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentViewOrderBinding
import com.veen.homechefrider.model.order.checkstatus.CheckStatusReq
import com.veen.homechefrider.model.order.checkstatus.CheckStatusRes
import com.veen.homechefrider.model.order.status.StatusReq
import com.veen.homechefrider.model.order.status.StatusRes
import com.veen.homechefrider.model.order.vieworder.ItemsDetail
import com.veen.homechefrider.model.order.vieworder.VieworderReq
import com.veen.homechefrider.model.order.vieworder.VieworderRes
import com.veen.homechefrider.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_view_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewOrder : Fragment() {
    private lateinit var vieworderRecyclerView: RecyclerView
    private lateinit var adapter: ViewOrderAdapter
    private var chefcall: String? = ""
    private var customercall: String? = ""
    private var status: Int? = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentViewOrderBinding>(inflater, R.layout.fragment_view_order, container, false)
        val OrID = ViewOrderArgs.fromBundle(requireArguments())
        val chID = ViewOrderArgs.fromBundle(requireArguments())
        val getsaveToken = AppUtils.getsaveToken(requireContext())
        val getsaveLogin = AppUtils.getsaveLogin(requireContext())
        vieworderRecyclerView = binding.vieworderRecyclerView
        ViewOrderList(getsaveToken, getsaveLogin, OrID, chID)
        CheckOrderStatus(getsaveToken, getsaveLogin, OrID, chID)

        binding.chefCall.setOnClickListener {
            if (chefcall != "") {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + chefcall)
                startActivity(dialIntent)
            }
        }

        binding.customerCall.setOnClickListener {
            if (customercall != "") {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + customercall)
                startActivity(dialIntent)
            }
        }

        binding.receivedOrder.setOnClickListener {
            if (binding.receivedOrder.text == "Not Picked") {
                PickedAPI(getsaveToken, getsaveLogin, OrID, chID)
            } else if (binding.receivedOrder.text == "Picked") {
                DeliveredAPI(getsaveToken, getsaveLogin, OrID, chID)
            } else if (binding.receivedOrder.text == "Delivered") {
                Toast.makeText(requireContext(), "You already delivered food!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun DeliveredAPI(getsaveToken: String, getsaveLogin: String, orID: ViewOrderArgs, chID: ViewOrderArgs) {
        try {
            val OrID = ViewOrderArgs.fromBundle(requireArguments())
            RetrofitInstance.instence?.orderstatus(getsaveToken, StatusReq(
                    chID.chefID.toInt(),
                    orID.orderID.toInt(),
                    4,
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<StatusRes> {
                override fun onResponse(call: Call<StatusRes>, response: Response<StatusRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        CheckOrderStatus(getsaveToken, getsaveLogin, OrID, chID)
                    }
                }

                override fun onFailure(call: Call<StatusRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun PickedAPI(getsaveToken: String, getsaveLogin: String, orID: ViewOrderArgs, chID: ViewOrderArgs) {
        try {
            val OrID = ViewOrderArgs.fromBundle(requireArguments())
            RetrofitInstance.instence?.orderstatus(getsaveToken, StatusReq(
                    chID.chefID.toInt(),
                    orID.orderID.toInt(),
                    6,
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<StatusRes> {
                override fun onResponse(call: Call<StatusRes>, response: Response<StatusRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        CheckOrderStatus(getsaveToken, getsaveLogin, OrID, chID)
                    }
                }

                override fun onFailure(call: Call<StatusRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun CheckOrderStatus(getsaveToken: String, getsaveLogin: String, orID: ViewOrderArgs, chID: ViewOrderArgs) {
        try {
            RetrofitInstance.instence?.checkstatus(getsaveToken, CheckStatusReq(
                    chID.chefID.toInt(),
                    orID.orderID.toInt(),
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<CheckStatusRes> {
                override fun onResponse(call: Call<CheckStatusRes>, response: Response<CheckStatusRes>) {
                    if (response.isSuccessful) {
                        status = response.body()!!.data
                        if (status == 0) {
                            receivedOrder.text = "Not Picked"
                        } else if (status == 1) {
                            receivedOrder.text = "Picked"
                        } else if (status == 2) {
                            receivedOrder.text = "Delivered"
                        }
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CheckStatusRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ViewOrderList(
        getsaveToken: String,
        getsaveLogin: String,
        orID: ViewOrderArgs,
        chID: ViewOrderArgs
    ) {
        try {
            RetrofitInstance.instence?.vieworder(getsaveToken, VieworderReq(
                chID.chefID.toInt(),
                orID.orderID.toInt(),
                getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<VieworderRes> {
                override fun onResponse(call: Call<VieworderRes>, response: Response<VieworderRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT)
                            .show()
                        chefcall = response.body()!!.data.chef_phone
                        customercall = response.body()!!.data.phone_no
                        vieworderName.text = response.body()!!.data.name
                        vieworderAddress.text = response.body()!!.data.address +" "+response.body()!!.data.pincode
                        vieworderChefName.text = response.body()!!.data.chef_name
                        vieworderChefAddress.text = response.body()!!.data.chef_address
                        vieworderDiscount.text = "Discount: " + response.body()!!.data.discount_amount
                        vieworderDelivery.text = "Delivery Charge: " + response.body()!!.data.delivery_charge
                        vieworderTotal.text = "Total: " + response.body()!!.data.total_amount
                        bindmyViewOrder(response.body()!!.data.items_detail)
                    }
                }

                override fun onFailure(call: Call<VieworderRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindmyViewOrder(list: List<ItemsDetail>) {
        try {
            vieworderRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ViewOrderAdapter(requireContext(), list)
            vieworderRecyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}