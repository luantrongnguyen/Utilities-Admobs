package com.luantrongnguyen.ultis_admobs

import android.app.Application
import com.google.android.gms.ads.MobileAds

abstract class AdMobApplication : Application() {
    private lateinit var appOpenAdUtil: AppOpenAdUtil

    abstract val appOpenAdUnitId: String

    // Define excluded activities (override in subclass if needed)
    open val excludedActivities: Set<String> = emptySet()

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) { println("AdMob initialized") }
        appOpenAdUtil = AppOpenAdUtil(
            application = this,
            adUnitId = appOpenAdUnitId,
            excludedActivities = excludedActivities
        )
    }

    fun setCurrentActivity(activity: android.app.Activity) {
        appOpenAdUtil.setCurrentActivity(activity)
    }

    fun cleanup() {
        appOpenAdUtil.cleanup()
    }
}