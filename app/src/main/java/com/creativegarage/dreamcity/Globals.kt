package com.creativegarage.dreamcity

import android.app.AlertDialog
import android.content.Context

object Globals {
    private var progressDialog: AlertDialog? = null

    fun showProgressDialog(context: Context) {
        progressDialog = AlertDialog.Builder(context)
            .setView(R.layout.progress_dialoge)
            .setCancelable(false)
            .show()
    }

    fun hideProgressDialog() {
        if (progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    fun showAlertDialogue(context: Context, title: String, message: String) {
        val builder1 = AlertDialog.Builder(context)
        builder1.setTitle(title)
        builder1.setMessage(message)
        builder1.setCancelable(true)
        builder1.setPositiveButton("Okay") { dialog, _ -> dialog.cancel() }
        builder1.create().show()
    }

}