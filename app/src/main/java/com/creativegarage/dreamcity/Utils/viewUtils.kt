package com.creativegarage.dreamcity.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.creativegarage.dreamcity.MainActivity
import com.creativegarage.dreamcity.R
import com.creativegarage.dreamcity.SplashActivity
import com.creativegarage.dreamcity.network.Constants
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun phoneValidation(textField: EditText): Boolean {
    var returnVal = true
    if (textField.text.length != 11) {
        returnVal = false
    }
    return returnVal
}


fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun getApiKey(): String {
    return "309847a8f0698e596e6ae19d1611fc83"
}

fun ProgressBar.showProgress() {
    visibility = View.VISIBLE
}

fun ProgressBar.hideProgress() {
    visibility = View.GONE
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}


fun hasActiveInternetConnection(context: Context?): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    context.toast("No internet connection available!")
    return false
}


fun changeFragmentDashboard(
    context: Context,
    fragment: Fragment?,
    device_back_tag: String,
    status: Boolean
) {
    val transaction: FragmentTransaction =
        (context as MainActivity).getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(
                R.anim.fadein,
                R.anim.fadeout
            )
    transaction.replace(R.id.dashboard_container, fragment!!)
    if (status) {
        transaction.addToBackStack(device_back_tag)
    }
    Constants.back_stack = device_back_tag
    Constants.back_status = status
    transaction.commit()
}


fun changeFragmentSplash(
    context: Context,
    fragment: Fragment?,
    device_back_tag: String,
    status: Boolean
) {
    val transaction: FragmentTransaction =
        (context as SplashActivity).getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(
                R.anim.fadein,
                R.anim.fadeout
            )
    transaction.replace(R.id.splash_container, fragment!!)
    if (status) {
        transaction.addToBackStack(device_back_tag)
    }
    Constants.back_stack = device_back_tag
    Constants.back_status = status
    transaction.commitAllowingStateLoss()
}