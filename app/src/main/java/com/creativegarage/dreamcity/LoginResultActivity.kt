package com.creativegarage.dreamcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog

class LoginResultActivity : AppCompatActivity() {
    private var mSuccess: BottomSheetMaterialDialog? = null
    private var mError: BottomSheetMaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_result)
    }


}