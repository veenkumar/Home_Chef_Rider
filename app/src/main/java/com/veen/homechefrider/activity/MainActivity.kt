package com.veen.homechefrider.activity

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.ActivityMainBinding
import com.veen.homechefrider.model.profile.view.ViewProfileReq
import com.veen.homechefrider.model.profile.view.ViewProfileRes
import com.veen.homechefrider.utils.AppUtils
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val getsaveToken = AppUtils.getsaveToken(applicationContext)
        val getsaveLogin = AppUtils.getsaveLogin(applicationContext)
        getSideBar(getsaveToken, getsaveLogin)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    private fun getSideBar(token: String, login: String) {
        try {
            RetrofitInstance.instence?.viewprofile(token, ViewProfileReq(
                login.toInt()
            ))!!.enqueue(object : Callback<ViewProfileRes> {
                override fun onResponse(
                    call: Call<ViewProfileRes>,
                    response: Response<ViewProfileRes>
                ) {
                    if (response.isSuccessful) {
                        Picasso.get().load(response.body()!!.data.profile_pic).into(sidebar_img)
                        main_sidebar_name.setText(response.body()!!.data.name)
                        main_sidebar_email.setText(response.body()!!.data.email)
                    }
                }

                override fun onFailure(call: Call<ViewProfileRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(drawerLayout)
    }
}