package com.veen.homechefrider.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.ActivityRegistrationBinding
import com.veen.homechefrider.model.registration.RegisReq
import com.veen.homechefrider.model.registration.RegisRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_registration)
        val binding = DataBindingUtil.setContentView<ActivityRegistrationBinding>(this, R.layout.activity_registration)
        val regexPassword =
            Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}")

        binding.onLoginClick.setOnClickListener {
            startActivity(Intent(applicationContext, Login::class.java))
        }

        binding.cirRegisterButton.setOnClickListener {
            if (TextUtils.isEmpty(binding.editTextName.text.toString())) {
                binding.editTextName.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.editTextEmail.text.toString())) {
                binding.editTextEmail.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.editTextMobile.text.toString())) {
                binding.editTextMobile.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.editTextAddress.text.toString())) {
                binding.editTextAddress.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.editTextPassword.text.toString())) {
                binding.editTextPassword.setError("Field not empty!")
            } else if (binding.editTextPassword.text.toString().length < 8 || regexPassword.matcher(binding.editTextPassword.text.toString())
                    .find()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Password length should be at least 8 character long.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                doRegistration(binding.editTextName, binding.editTextEmail, binding.editTextMobile, binding.editTextAddress, binding.editTextPassword)
            }
        }
    }

    private fun doRegistration(
        editTextName: EditText,
        editTextEmail: EditText,
        editTextMobile: EditText,
        editTextAddress: EditText,
        editTextPassword: EditText
    ) {
        try {
            RetrofitInstance.instence?.registration(RegisReq(
                editTextAddress.text.toString(),
                editTextEmail.text.toString(),
                editTextName.text.toString(),
                editTextPassword.text.toString(),
                editTextMobile.text.toString()
            ))!!.enqueue(object : Callback<RegisRes> {
                override fun onResponse(call: Call<RegisRes>, response: Response<RegisRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "" + response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        editTextAddress.text.clear()
                        editTextEmail.text.clear()
                        editTextName.text.clear()
                        editTextPassword.text.clear()
                        editTextMobile.text.clear()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "This Email or Mobile Registrated already!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}