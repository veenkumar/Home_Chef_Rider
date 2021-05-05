package com.veen.homechefrider.api

import com.veen.homechefrider.model.login.LoginReq
import com.veen.homechefrider.model.login.LoginRes
import com.veen.homechefrider.model.password.PassReq
import com.veen.homechefrider.model.password.PassRes
import com.veen.homechefrider.model.profile.image.ImageReq
import com.veen.homechefrider.model.profile.image.ImageRes
import com.veen.homechefrider.model.profile.update.UpdateProfileReq
import com.veen.homechefrider.model.profile.update.UpdateProfileRes
import com.veen.homechefrider.model.profile.view.ViewProfileReq
import com.veen.homechefrider.model.profile.view.ViewProfileRes
import retrofit2.Call
import retrofit2.http.*

interface API {
    /*@Headers("Content-Type: application/json")
    @GET("get_course")
    fun getcourses(): Call<GetCourseRes>*/

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body loginReq: LoginReq
    ): Call<LoginRes>

    @Headers("Content-Type: application/json")
    @POST("change_password")
    fun passchange(
        @Header("Authorization")token:String,
        @Body passReq: PassReq
    ): Call<PassRes>

    @Headers("Content-Type: application/json")
    @POST("view_profile")
    fun viewprofile(
        @Header("Authorization")token:String,
        @Body viewProfileReq: ViewProfileReq
    ): Call<ViewProfileRes>

    @Headers("Content-Type: application/json")
    @POST("update_profile")
    fun updateprofile(
            @Header("Authorization")token:String,
            @Body updateProfileReq: UpdateProfileReq
    ): Call<UpdateProfileRes>

    @Headers("Content-Type: application/json")
    @POST("upload_photo")
    fun imageupdate(
            @Header("Authorization")token:String,
            @Body imageReq: ImageReq
    ): Call<ImageRes>
}