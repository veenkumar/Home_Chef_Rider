package com.veen.homechefrider.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
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
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
//        setContentView(R.layout.activity_login)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

//        binding.loginEmailId.setText("bk_deliveryboy@gmail.com")
//        binding.loginPass.setText("123456")

        binding.logingetstarted.setOnClickListener {
            startActivity(Intent(applicationContext, Registration::class.java))
        }

        binding.loginbutton.setOnClickListener {
            if (TextUtils.isEmpty(binding.loginEmailId.text.toString())) {
                binding.loginEmailId.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.loginPass.text.toString())) {
                binding.loginPass.setError("Field not empty!")
            } else {
                doLogin(binding.loginEmailId, binding.loginPass)
            }
        }

    }

    private fun doLogin(loginEmailId: EditText, loginPass: EditText) {
        try {
            RetrofitInstance.instence?.login(LoginReq(
                loginEmailId.text.toString(),
                loginPass.text.toString()
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