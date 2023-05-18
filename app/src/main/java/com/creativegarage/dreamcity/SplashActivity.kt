package com.creativegarage.dreamcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Handler
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.creativegarage.dreamcity.Fragments.OnBoardFragment
import com.creativegarage.dreamcity.Fragments.SplashFragment
import com.creativegarage.dreamcity.Utils.changeFragmentSplash
import com.creativegarage.dreamcity.databinding.ActivitySplashBinding
import com.creativegarage.dreamcity.R
//import sabeeldev.creativegarage.mivida.databinding.ActivitySplashBinding
import java.util.*
import kotlin.collections.ArrayList


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var installedAppsList: ArrayList<AppModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        changeFragmentSplash(this, SplashFragment(), "SplashFragment", false)
        installedAppsList = ArrayList()

        getInstalledApps()
        Handler().postDelayed(Runnable {
            changeFragmentSplash(this, OnBoardFragment(), "OnBoardFragment", false)
        }, 2500)
    }

    private fun getInstalledApps(): ArrayList<AppModel> {
        installedAppsList.clear()
        val packs = packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            if (!isSystemPackage(p)) {
                val appName = p.applicationInfo.loadLabel(packageManager).toString()
                val icon = p.applicationInfo.loadIcon(packageManager)
                val packages = p.applicationInfo.packageName
                Log.d("AppPackageName", "" + appName + "---" + packages);
                installedAppsList.add(AppModel(appName, icon, packages))
            }
        }
        installedAppsList.sortBy { it.getName().capitalized() }
        return installedAppsList
    }

    private fun String.capitalized(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}