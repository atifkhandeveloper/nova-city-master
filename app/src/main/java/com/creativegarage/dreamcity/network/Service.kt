package com.creativegarage.dreamcity.network

import retrofit2.Call
import retrofit2.http.*

interface Service {

    @FormUrlEncoded
    @POST(Urls.OTP_SEND)
    fun sendOTP(
        @Query("status") status: Int,
        @Field("mobileno") mobileno: String,
        @Field("securityno") securityno: String,
        @Field("APIKEY") apikey: String
    ): Call<String>


    @FormUrlEncoded
    @POST(Urls.OTP_SEND)
    fun verifyOTP(
        @Query("status") status: Int,
        @Field("otp") otp: String,
        @Field("mobileno") mobileno: String,
        @Field("securityno") securityno: String,
        @Field("APIKEY") apikey: String
    ): Call<Int>

    @FormUrlEncoded
    @POST(Urls.OTP_SEND)
    fun sendOTPn(
        @Query("status") status: Int,
        @Field("mobilenon") mobileno: String,
        @Field("securityno") securityno: String,
        @Field("APIKEY") apikey: String
    ): Call<String>

    @FormUrlEncoded
    @POST(Urls.OTP_SEND)
    fun verifyOTPn(
        @Query("status") status: Int,
        @Field("otpn") otp: String,
        @Field("mobilenon") mobileno: String,
        @Field("securityno") securityno: String,
        @Field("APIKEY") apikey: String
    ): Call<String>

}
