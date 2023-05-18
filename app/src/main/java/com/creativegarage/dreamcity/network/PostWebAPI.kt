package com.creativegarage.dreamcity.network

import android.app.Activity
import android.content.Context
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.View
import com.creativegarage.dreamcity.Globals
import com.creativegarage.dreamcity.R
import com.creativegarage.dreamcity.databinding.FragmentTransferBinding
import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.creativegarage.dreamcity.network.Constants.OTP_VERIFIED

class PostWebAPI(val context: Context, val binding: FragmentTransferBinding) {
    private var mOtpSend: BottomSheetMaterialDialog? = null

    fun sendOTP(status: Int, mobileno: String, securityCode: String) {
        Globals.showProgressDialog(context)
        Api.getService(Urls.BASE_URL).sendOTP(status, mobileno, securityCode, Constants.API_KEY)
            .enqueue(
                object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Globals.hideProgressDialog()
                        val responseData = response.body().toString()
                        Log.d("APICall", response.body().toString())
                        if (response.isSuccessful()) {
                            if (responseData.equals(Constants.OTP_SEND)) {
                                binding.thirdLayout.visibility = View.VISIBLE
                                binding.textView6.visibility = View.VISIBLE
                                binding.qrlayout.visibility = View.GONE
                                binding.etPhoneNumber.isEnabled = false
                                binding.etPhoneNumber.inputType = InputType.TYPE_NULL
                                OTPSend("OTP Send", responseData)
                                // OTPSend("OTP Send", "OTP send to your Registered Phone #")
                                binding.btnSendOtp.text =
                                    context.resources.getString(R.string.resend)
                                binding.verifyOtpGroup.visibility = View.VISIBLE
                                mOtpSend!!.show()
                            } else {
                                binding.qrlayout.visibility = View.VISIBLE
                                binding.thirdLayout.visibility = View.GONE
                                binding.textView6.visibility = View.GONE
                                OTPSend("Error", responseData)
                                mOtpSend?.show()
                            }
                            Log.d("onMessageReceived", "")
                        } else {
                            binding.thirdLayout.visibility = View.GONE
                            binding.textView6.visibility = View.GONE
                            Log.d("onMessageReceived", "")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Globals.hideProgressDialog()
                        Globals.showAlertDialogue(context, "Alert", "" + t.message)

                    }
                })
    }

    fun verifyOTP(otp: String, status: Int, mobileno: String, securityCode: String) {
        Globals.showProgressDialog(context)
        Api.getService(Urls.BASE_URL)
            .verifyOTP(status, otp, mobileno, securityCode, Constants.API_KEY).enqueue(
                object : Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        Globals.hideProgressDialog()
                        val responseData = response.body()?.toInt()
                        Log.d("APICall", response.body().toString())
                        if (response.isSuccessful()) {
                            if (responseData == 1) {
                                OTP_VERIFIED = true
                                OTPSend("OTP Verified", "OTP Verification successed")
                                binding.etVerifyotp.setFocusableInTouchMode(true)
                                mOtpSend?.show()
                                binding.etVerifyotp.isEnabled = false
                                binding.etVerifyotp.inputType = InputType.TYPE_NULL
                            } else if (responseData == 0) {
                                OTP_VERIFIED = false
                                binding.etVerifyotp.setFocusableInTouchMode(true)
                                OTPSend("Error", "OTP Verification failed")
                                mOtpSend?.show()
                            }
                            Log.d("onMessageReceived", "")
                        } else {

                            Log.d("onMessageReceived", "")
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Globals.hideProgressDialog()
                        Globals.showAlertDialogue(context, "Alert", "" + t.message)

                    }
                })
    }

    fun verifyOTPn(otp: String, status: Int, mobileno: String, securityCode: String) {
        Globals.showProgressDialog(context)
        Api.getService(Urls.BASE_URL)
            .verifyOTPn(status, otp, mobileno, securityCode, Constants.API_KEY).enqueue(
                object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Globals.hideProgressDialog()
                        val responseData = response.body().toString()
                        Log.d("APICall", response.body().toString())
                        if (response.isSuccessful()) {
                            if (responseData.equals(Constants.TRADE_SUCCESS)) {
                                OTPSend("OTP Verified", responseData)
                                binding.etVerifyotp2.setFocusableInTouchMode(true)
                                mOtpSend?.show()
                                binding.etVerifyotp2.setText("")
                                binding.etPhoneNumber2.setText("")
                                binding.etVerifyotp.setText("")
                                binding.etPhoneNumber.setText("")
                                binding.etPassword.setText("")
                                binding.etPhoneNumber.isEnabled = true
                                binding.etPhoneNumber.inputType = InputType.TYPE_CLASS_NUMBER
                                binding.etVerifyotp.isEnabled = true
                                binding.etVerifyotp.inputType = InputType.TYPE_CLASS_NUMBER
                                binding.qrlayout.visibility = View.VISIBLE
                                binding.verifyOtp2Group.visibility = View.GONE
                                binding.verifyOtpGroup.visibility = View.GONE
                                binding.thirdLayout.visibility = View.GONE
                                binding.textView6.visibility = View.GONE
                                binding.fourthLayout.visibility = View.GONE
                                binding.textView8.visibility = View.GONE
                                binding.btnSendOtp.setText("Send OTP")
                                OTP_VERIFIED = false
                                // binding.etPassword.setFocusable(false)
                            } else {
                                binding.qrlayout.visibility = View.GONE
                                binding.etVerifyotp2.setFocusableInTouchMode(true)
                                OTPSend("Error", "" + responseData)
                                mOtpSend?.show()
                            }
                            Log.d("onMessageReceived", "")
                        } else {

                            Log.d("onMessageReceived", "")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Globals.hideProgressDialog()
                        Globals.showAlertDialogue(context, "Alert", "" + t.message)

                    }
                })
    }

    fun sendOTPn(status: Int, mobileno: String, securityCode: String) {
        Globals.showProgressDialog(context)
        Api.getService(Urls.BASE_URL).sendOTPn(status, mobileno, securityCode, Constants.API_KEY)
            .enqueue(
                object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Globals.hideProgressDialog()
                        val responseData = response.body().toString()
                        Log.d("APICall", response.body().toString())
                        if (response.isSuccessful()) {
                            if (responseData.equals(Constants.OTP_SEND_NEW)) {
                                binding.fourthLayout.visibility = View.VISIBLE
                                binding.textView8.visibility = View.VISIBLE
                                OTPSend("OTP Send", responseData)
                                binding.btnSendOtp.text =
                                    context.resources.getString(R.string.resend_otp)
                                mOtpSend!!.show()
                            } else {
                                binding.textView8.visibility = View.GONE
                                binding.fourthLayout.visibility = View.GONE
                                OTPSend("Error", responseData)
                                mOtpSend?.show()
                            }
                            Log.d("onMessageReceived", "")
                        } else {
                            binding.textView8.visibility = View.GONE
                            binding.fourthLayout.visibility = View.GONE
                            Log.d("onMessageReceived", "")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Globals.hideProgressDialog()
                        Globals.showAlertDialogue(context, "Alert", "" + t.message)

                    }
                })
    }

    private fun OTPSend(titile: String, message: String) {
        mOtpSend = BottomSheetMaterialDialog.Builder(context as Activity)
            .setTitle(titile)
            .setMessage(Html.fromHtml(message))
            .setCancelable(false)
            .setPositiveButton(
                "Close", R.drawable.ic_baseline_close_24
            ) { dialogInterface, i ->

                dialogInterface.dismiss()
            }.setNegativeButton(
                "Ok", R.drawable.ic_baseline_close_24
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .build()
    }

}