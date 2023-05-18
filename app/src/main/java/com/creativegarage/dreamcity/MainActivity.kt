package com.creativegarage.dreamcity

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.creativegarage.dreamcity.Fragments.AboutFragment
import com.creativegarage.dreamcity.Fragments.ContactFragment
import com.creativegarage.dreamcity.Fragments.HomeFragment
import com.creativegarage.dreamcity.Fragments.TransferFragment
import com.creativegarage.dreamcity.Utils.changeFragmentDashboard
import com.creativegarage.dreamcity.databinding.ActivityMainBinding
import com.creativegarage.dreamcity.network.Constants.OTP_VERIFIED


class MainActivity : AppCompatActivity(), VersionListner {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fm: FragmentManager
    private var mExitApp: BottomSheetMaterialDialog? = null

    private val REQ_CODE_VERSION_UPDATE = 530
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    clearBackStack()
                    changeFragmentDashboard(this, HomeFragment(), "HomeFragment", false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_trasnfer -> {
                    clearBackStack()
                    changeFragmentDashboard(this, TransferFragment(), "TransferFragment", true)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_contact -> {
                    clearBackStack()
                    changeFragmentDashboard(this, ContactFragment(), "ContactFragment", true)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_about -> {
                    clearBackStack()
                    changeFragmentDashboard(this, AboutFragment(), "AboutFragment", true)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fm = supportFragmentManager
        checkForAppUpdate();
        setNavData()
        exitapp()
    }

    override fun onResume() {
        super.onResume()
        checkNewAppVersionState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            REQ_CODE_VERSION_UPDATE -> if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                Log.d("Updateflowfailed!", "Result code $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
                unregisterInstallStateUpdListener()
            }
        }
    }

    override fun onDestroy() {
        unregisterInstallStateUpdListener()
        super.onDestroy()
    }

    private fun checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        // Create a listener to track request state updates.
        installStateUpdatedListener =
            InstallStateUpdatedListener { installState ->
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED) // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister()
            }

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener)
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo)
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo)
                }
            }
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,  // The current activity making the update request.
                this,  // Include a request code to later monitor this update request.
                REQ_CODE_VERSION_UPDATE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,  // The current activity making the update request.
                this,  // Include a request code to later monitor this update request.
                REQ_CODE_VERSION_UPDATE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
            unregisterInstallStateUpdListener()
        }
    }

    private fun popupSnackbarForCompleteUpdateAndUnregister() {
//        val snackbar = Snackbar.make(
//            drawerLayout,
//            getString(R.string.update_downloaded),
//            Snackbar.LENGTH_INDEFINITE
//        )
//        snackbar.setAction(R.string.restart, object : View.OnClickListener() {
//            fun onClick(view: View?) {
//                appUpdateManager.completeUpdate()
//            }
//        })
//        snackbar.setActionTextColor(resources.getColor(R.color.action_color))
//        snackbar.show()
//        unregisterInstallStateUpdListener()
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private fun checkNewAppVersionState() {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                //FLEXIBLE:
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdateAndUnregister()
                }

                //IMMEDIATE:
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    startAppUpdateImmediate(appUpdateInfo)
                }
            }
    }

    /**
     * Needed only for FLEXIBLE update
     */
    private fun unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null) appUpdateManager.unregisterListener(
            installStateUpdatedListener
        )
    }

    private fun setNavData() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home)
    }

    private fun clearBackStack() {
        OTP_VERIFIED = false
        for (i in 0 until fm.getBackStackEntryCount()) {
            fm.popBackStack()
        }
    }

    fun showDialoge(titile: String?, message: String?) {
        BottomSheetMaterialDialog.Builder(this)
            .setTitle(titile!!)
            .setMessage(Html.fromHtml(message))
            .setCancelable(false)
            .setPositiveButton(
                "Update Current Version", R.drawable.ic_baseline_update_24
            ) { dialogInterface, which ->
                dialogInterface.cancel()
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())
                    )
                )
            }
            .setNegativeButton(
                "Update Later", R.drawable.ic_baseline_close_24
            ) { dialogInterface, which -> dialogInterface.cancel() }
            .build().show()
    }

    private fun exitapp() {
        mExitApp = BottomSheetMaterialDialog.Builder(this)
            .setTitle("Exit " + getString(R.string.app_name))
            .setMessage(Html.fromHtml("Do you really want to exit this application?"))
            .setCancelable(false)
            .setPositiveButton(
                "Yes", R.drawable.ic_baseline_exit_to_app_24
            ) { dialogInterface, i ->
                super.onBackPressed()
                dialogInterface.dismiss()
            }.setNegativeButton(
                "No", R.drawable.ic_baseline_close_24
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .build()
    }

    override fun onBackPressed() {
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
            binding.bottomNavigation.setSelectedItemId(R.id.nav_home)
        } else {
            mExitApp?.show()
        }
    }

    override fun onVersionReceived(versionCode: String) {
        Toast.makeText(this, "Version Name: " + versionCode, Toast.LENGTH_SHORT).show()
//        if (versionCode == CURRENT_VERSION) {
//            showDialoge(
//                "Update Available",
//                "A new version of Mivida is available. Please update to latest version now."
//            );
//        }
    }
}