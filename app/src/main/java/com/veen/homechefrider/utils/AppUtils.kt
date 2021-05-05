package com.veen.homechefrider.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

object AppUtils {
    fun saveLogin(context: Context?, loginID: String) {
        val shred = context?.getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Login", loginID)?.apply()
    }

    fun getsaveLogin(context: Context?): String {
        val shred = context?.getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Login", "") ?: ""
    }

    fun getdeleteLogin(context: Context?) {
        val shred = context?.getSharedPreferences("Login", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.remove("Login")?.apply()
    }

    fun saveToken(context: Context?, token: String) {
        val shred = context?.getSharedPreferences("Token", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("Token", token)?.apply()
    }

    fun getsaveToken(context: Context?): String {
        val shred = context?.getSharedPreferences("Token", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("Token", "") ?: ""
    }

    fun getdeleteToken(context: Context?) {
        val shred = context?.getSharedPreferences("Token", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.remove("Token")?.apply()
    }
}