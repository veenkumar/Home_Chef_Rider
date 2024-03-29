package com.veen.homechefrider.api

import com.veen.homechefrider.model.complain.ComplainReq
import com.veen.homechefrider.model.complain.ComplainRes
import com.veen.homechefrider.model.complain.view.ViewComplaintReq
import com.veen.homechefrider.model.complain.view.ViewComplaintRes
import com.veen.homechefrider.model.dashboard.DashboardReq
import com.veen.homechefrider.model.dashboard.DashboardRes
import com.veen.homechefrider.model.location.LocationReq
import com.veen.homechefrider.model.location.LocationRes
import com.veen.homechefrider.model.login.LoginReq
import com.veen.homechefrider.model.login.LoginRes
import com.veen.homechefrider.model.message.MessageReq
import com.veen.homechefrider.model.message.MessageRes
import com.veen.homechefrider.model.order.assign.AssignReq
import com.veen.homechefrider.model.order.assign.AssignRes
import com.veen.homechefrider.model.order.checkstatus.CheckStatusReq
import com.veen.homechefrider.model.order.checkstatus.CheckStatusRes
import com.veen.homechefrider.model.order.status.StatusReq
import com.veen.homechefrider.model.order.status.StatusRes
import com.veen.homechefrider.model.order.vieworder.VieworderReq
import com.veen.homechefrider.model.order.vieworder.VieworderRes
import com.veen.homechefrider.model.password.PassReq
import com.veen.homechefrider.model.password.PassRes
import com.veen.homechefrider.model.profile.image.ImageReq
import com.veen.homechefrider.model.profile.image.ImageRes
import com.veen.homechefrider.model.profile.update.UpdateProfileReq
import com.veen.homechefrider.model.profile.update.UpdateProfileRes
import com.veen.homechefrider.model.profile.view.ViewProfileReq
import com.veen.homechefrider.model.profile.view.ViewProfileRes
import com.veen.homechefrider.model.registration.RegisReq
import com.veen.homechefrider.model.registration.RegisRes
import com.veen.homechefrider.model.report.ReportReq
import com.veen.homechefrider.model.report.complete.CompleteRes
import com.veen.homechefrider.model.report.escalate.EscalateRes
import com.veen.homechefrider.model.report.received.ReceivedRes
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

    @Headers("Content-Type: application/json")
    @POST("assign_order")
    fun assignorder(
            @Header("Authorization")token:String,
            @Body assignReq: AssignReq
    ): Call<AssignRes>

    @Headers("Content-Type: application/json")
    @POST("view_order_detail")
    fun vieworder(
            @Header("Authorization")token:String,
            @Body vieworderReq: VieworderReq
    ): Call<VieworderRes>

    @Headers("Content-Type: application/json")
    @POST("update_order_status")
    fun orderstatus(
            @Header("Authorization")token:String,
            @Body statusReq: StatusReq
    ): Call<StatusRes>

    @Headers("Content-Type: application/json")
    @POST("check_order_pickup_status")
    fun checkstatus(
            @Header("Authorization")token:String,
            @Body checkStatusReq: CheckStatusReq
    ): Call<CheckStatusRes>

    @Headers("Content-Type: application/json")
    @POST("registration")
    fun registration(
        @Body regisReq: RegisReq
    ): Call<RegisRes>

    @Headers("Content-Type: application/json")
    @POST("complain")
    fun complain(
        @Header("Authorization")token:String,
        @Body complainReq: ComplainReq
    ): Call<ComplainRes>

    @Headers("Content-Type: application/json")
    @POST("complain_list")
    fun complainlist(
        @Header("Authorization")token:String,
        @Body viewComplaintReq: ViewComplaintReq
    ): Call<ViewComplaintRes>

    @Headers("Content-Type: application/json")
    @POST("view_message")
    fun viewmessgae(
        @Header("Authorization")token:String,
        @Body messageReq: MessageReq
    ): Call<MessageRes>

    @Headers("Content-Type: application/json")
    @POST("count_on_time_late_order")
    fun viewdashboard(
        @Header("Authorization")token:String,
        @Body dashboardReq: DashboardReq
    ): Call<DashboardRes>

    @Headers("Content-Type: application/json")
    @POST("complete_order")
    fun completeorder(
            @Header("Authorization")token:String,
            @Body reportReq: ReportReq
    ): Call<CompleteRes>

    @Headers("Content-Type: application/json")
    @POST("received_payments")
    fun receivedpayments(
            @Header("Authorization")token:String,
            @Body reportReq: ReportReq
    ): Call<ReceivedRes>

    @Headers("Content-Type: application/json")
    @POST("pending_payments")
    fun pendingpayments(
            @Header("Authorization")token:String,
            @Body reportReq: ReportReq
    ): Call<ReceivedRes>

    @Headers("Content-Type: application/json")
    @POST("escalated_order")
    fun escalatedorder(
            @Header("Authorization")token:String,
            @Body reportReq: ReportReq
    ): Call<EscalateRes>

    @Headers("Content-Type: application/json")
    @POST("update_location")
    fun location(
            @Header("Authorization")token:String,
            @Body locationReq: LocationReq
    ): Call<LocationRes>

}