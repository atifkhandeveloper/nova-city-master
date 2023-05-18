package com.creativegarage.dreamcity.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gpfreetech.awesomescanner.ui.GpCodeScanner
import com.gpfreetech.awesomescanner.util.DecodeCallback
import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.creativegarage.dreamcity.R
import com.creativegarage.dreamcity.Utils.hasActiveInternetConnection
import com.creativegarage.dreamcity.Utils.phoneValidation
import com.creativegarage.dreamcity.databinding.FragmentTransferBinding
import com.creativegarage.dreamcity.network.Constants
import com.creativegarage.dreamcity.network.Constants.OTP_VERIFIED
import com.creativegarage.dreamcity.network.Constants.VERIFY_URL
import com.creativegarage.dreamcity.network.PostWebAPI
import com.creativegarage.dreamcity.onResult
import android.view.inputmethod.EditorInfo


class TransferFragment : Fragment(), View.OnClickListener, onResult {
    private lateinit var binding: FragmentTransferBinding
    private lateinit var mCodeScanner: GpCodeScanner
    private lateinit var postWebAPI: PostWebAPI
    private val MY_CAMERA_PERMISSION_CODE = 400
    private var url = VERIFY_URL
    private var mOtpSend: BottomSheetMaterialDialog? = null
    private lateinit var onResul: onResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartScanner.setOnClickListener(this)
        binding.btnSendOtp.setOnClickListener(this)
        binding.btnVerify.setOnClickListener(this)
        binding.btnTrade.setOnClickListener(this)

        binding.verifyOtp2Group.visibility = View.GONE
        binding.verifyOtpGroup.visibility = View.GONE
        binding.thirdLayout.visibility = View.GONE
        binding.textView6.visibility = View.GONE
        binding.fourthLayout.visibility = View.GONE
        binding.textView8.visibility = View.GONE
        binding.btnSendOtp.text = resources.getString(R.string.send_otp)

        mCodeScanner = GpCodeScanner(requireActivity(), binding.scannerView)
        postWebAPI = PostWebAPI(requireActivity(), binding)
        onResul = this
        mCodeScanner.decodeCallback = DecodeCallback { result ->
            run {
                //binding.scanResult.visibility = View.VISIBLE
                Log.d("MyURL", "" + result.text);
                val separate1: List<String> = result.text.split("code=".toRegex())
                if (separate1.size > 1) {
                    onResul.onResultReceived("0", separate1[1])
                } else {
                    onResul.onResultReceived("1", "")
                }
            }
        }

        OTPVerification()

    }

    private fun OTPVerification() {
        binding.etVerifyotp.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val contentText = SpannableString((binding.etVerifyotp as TextView).text)
                verifyOTP(contentText.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etVerifyotp2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val contentText = SpannableString((binding.etVerifyotp2 as TextView).text)
                verifyOTPn(contentText.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etVerifyotp.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                verifyOTP(binding.etVerifyotp.text.toString())
                return@OnEditorActionListener true
            }
            false
        })



        binding.etVerifyotp2.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                verifyOTPn(binding.etVerifyotp2.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun verifyOTPn(contentText: String) {
        if (contentText.length == 4) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            if (hasActiveInternetConnection(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.etVerifyotp2.setFocusable(false)
                    postWebAPI.verifyOTPn(
                        binding.etVerifyotp2.text.toString(),
                        4,
                        binding.etPhoneNumber2.text.toString(),
                        binding.etPassword.text.toString()
                    )
                };
            }
        }
    }

    fun verifyOTP(contentText: String) {
        if (contentText.length == 4) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (hasActiveInternetConnection(activity)) {
                    binding.etVerifyotp.setFocusable(false);
                    postWebAPI.verifyOTP(
                        binding.etVerifyotp.text.toString(),
                        2,
                        binding.etPhoneNumber.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }
            };
        }
    }

    override fun onPause() {
        mCodeScanner.releaseResources()
        binding.scanResult.visibility = View.GONE
        binding.scannerView.visibility = View.GONE
        binding.btnStartScanner.text = getString(R.string.signin)
        super.onPause()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnStartScanner -> {
                checkCameraPermissions(MY_CAMERA_PERMISSION_CODE)
            }

            binding.btnSendOtp -> {
                if (binding.btnSendOtp.text.equals(resources.getString(R.string.send_otp)) || binding.btnSendOtp.text.equals(
                        resources.getString(R.string.resend)
                    )
                ) {
                    if (phoneValidation(binding.etPhoneNumber)) {
                        if (hasActiveInternetConnection(activity)) {
                            postWebAPI.sendOTP(
                                1,
                                binding.etPhoneNumber.text.toString(),
                                binding.etPassword.text.toString()
                            )
                        }
                    } else {
                        OTPSend("Error", "" + Constants.MOBILE_NO_ERROR)
                        mOtpSend!!.show()
                    }
                } else if (binding.btnSendOtp.text.equals(resources.getString(R.string.resend_otp)) ||
                    binding.btnSendOtp.text.equals(resources.getString(R.string.send_otp_new))
                ) {
                    if (hasActiveInternetConnection(activity)) {
                        postWebAPI.sendOTPn(
                            3,
                            binding.etPhoneNumber2.text.toString(),
                            binding.etPassword.text.toString()
                        )
                    }
                }
            }

            binding.btnTrade -> {
                if (OTP_VERIFIED) {
                    if (binding.verifyOtp2Group.isVisible) {
                        if (phoneValidation(binding.etPhoneNumber2)) {
                            if (hasActiveInternetConnection(activity)) {
                                postWebAPI.verifyOTPn(
                                    binding.etVerifyotp2.text.toString(),
                                    4,
                                    binding.etPhoneNumber2.text.toString(),
                                    binding.etPassword.text.toString()
                                )
                            }
                        } else {
                            OTPSend("Error", "" + Constants.MOBILE_NO_ERROR)
                            mOtpSend!!.show()
                        }
                    } else {
                        binding.btnSendOtp.text = resources.getString(R.string.send_otp_new)
                        binding.verifyOtpGroup.visibility = View.VISIBLE
                        binding.verifyOtp2Group.visibility = View.VISIBLE
                        binding.btnVerify.visibility = View.GONE
                    }
                } else {
                    OTPSend("Error", "OTP not verified yet")
                    mOtpSend!!.show()
                }
            }

            binding.btnVerify -> {
                if (OTP_VERIFIED) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                } else {
                    OTPSend("Error", "OTP not verified yet")
                    mOtpSend!!.show()
                }
            }
        }
    }

    private fun scanner() {
        if (binding.btnStartScanner.text.equals(getString(R.string.signin))) {
            binding.scannerView.visibility = View.VISIBLE
            binding.btnStartScanner.text = getString(R.string.hideqr)
            mCodeScanner.startPreview()
        } else {
            binding.scanResult.visibility = View.GONE
            binding.scannerView.visibility = View.GONE
            binding.btnStartScanner.text = getString(R.string.signin)
            mCodeScanner.releaseResources()
        }
    }


    private fun OTPSend(titile: String, message: String) {
        mOtpSend = BottomSheetMaterialDialog.Builder(requireActivity())
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


    fun checkCameraPermissions(permission: Int) {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    permission
                )
            }
        } else {
            scanner()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            scanner()
        }
    }

    override fun onResultReceived(error: String?, message: String?) {
        activity?.runOnUiThread(Runnable {
            // Stuff that updates the UI

            if (error.equals("0")) {
                binding.etPassword.setText(message)
                url += message
                mCodeScanner.releaseResources()
                binding.scannerView.visibility = View.GONE
                binding.btnStartScanner.text = getString(R.string.signin)
            } else if (error.equals("1")) {
                Toast.makeText(
                    activity,
                    "Invalid QR Code",
                    Toast.LENGTH_SHORT
                ).show()
                binding.etPassword.setText("")
                url = VERIFY_URL
                mCodeScanner.releaseResources()
                binding.scannerView.visibility = View.GONE
                binding.btnStartScanner.text = getString(R.string.signin)
            }
        })
    }
}