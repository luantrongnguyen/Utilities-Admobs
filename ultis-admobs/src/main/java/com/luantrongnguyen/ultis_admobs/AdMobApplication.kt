package com.luantrongnguyen.ultis_admobs

import android.app.Application
import com.google.android.gms.ads.MobileAds

abstract class AdMobApplication : Application() {
    private lateinit var appOpenAdUtil: AppOpenAdUtil

    // Abstract property để yêu cầu ứng dụng cung cấp adUnitId
    abstract val appOpenAdUnitId: String

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo AdMob SDK
        MobileAds.initialize(this) {}
        // Khởi tạo AppOpenAdUtil
        appOpenAdUtil = AppOpenAdUtil(
            application = this,
            adUnitId = appOpenAdUnitId
        )
    }

    // Cung cấp Activity hiện tại cho AppOpenAdUtil
    fun setCurrentActivity(activity: android.app.Activity) {
        appOpenAdUtil.setCurrentActivity(activity)
    }

    // Hủy AppOpenAdUtil khi cần
    fun cleanup() {
        appOpenAdUtil.cleanup()
    }
}