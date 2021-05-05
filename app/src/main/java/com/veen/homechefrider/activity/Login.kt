package com.veen.homechefrider.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.ActivityLoginBinding
import com.veen.homechefrider.model.login.LoginReq
import com.veen.homechefrider.model.login.LoginRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        binding.loginEmailId.setText("bk_deliveryboy@gmail.com")
        binding.loginPass.setText("123456")

        binding.loginbutton.setOnClickListener {
            try {
                RetrofitInstance.instence?.login(LoginReq(
                    binding.loginEmailId.text.toString(),
                    binding.loginPass.text.toString()
                ))!!.enqueue(object : Callback<LoginRes> {
                    override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "" + response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()

                            val getLoginID = response.body()!!.data.id
                            val getToken = response.body()!!.token

                            AppUtils.saveLogin(applicationContext, getLoginID)
                            AppUtils.saveToken(applicationContext, getToken)

                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                        Log.d("TAG", "onFailure: Failed")
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}