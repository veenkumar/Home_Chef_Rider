package com.veen.homechefrider.fragment.side

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentChangePasswordBinding
import com.veen.homechefrider.model.password.PassReq
import com.veen.homechefrider.model.password.PassRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ChangePassword : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_change_password, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        val regexPassword =
            Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}")

        binding.changepassBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.oldpassword?.text.toString())) {
                binding.oldpassword?.setError("Please Enter Old Password")
            } else if (TextUtils.isEmpty(binding.newpassword?.text.toString())) {
                binding.newpassword?.setError("Please Enter New Password")
            } else if (TextUtils.isEmpty(binding.confirmpassword?.text.toString())) {
                binding.confirmpassword?.setError("Please Enter confirmpassword Password")
            } else if (binding.newpassword?.text.toString().length < 8 || regexPassword.matcher(binding.newpassword?.text.toString())
                            .find()
            ) {
                Toast.makeText(
                        requireContext(),
                        "Password length should be at least 8 character long.",
                        Toast.LENGTH_SHORT
                ).show()
            } else {
                if (binding.newpassword?.text.toString() == binding.confirmpassword?.text.toString()) {
                    changePassword()
                } else {
                    Toast.makeText(
                            requireContext(),
                            "New & Confirm Password Not Matched",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun changePassword() {
        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())
            RetrofitInstance.instence?.passchange(getsaveToken, PassReq(
                binding.newpassword.text.toString(),
                binding.oldpassword.text.toString(),
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<PassRes> {
                override fun onResponse(call: Call<PassRes>, response: Response<PassRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        binding.newpassword.text.clear()
                        binding.oldpassword.text.clear()
                        binding.confirmpassword.text.clear()
                    }
                }

                override fun onFailure(call: Call<PassRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}