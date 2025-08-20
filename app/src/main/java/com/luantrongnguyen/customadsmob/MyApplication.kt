package com.luantrongnguyen.customadsmob

import android.app.Application
import com.luantrongnguyen.ultis_admobs.AdMobApplication
import com.luantrongnguyen.ultis_admobs.AppOpenAdUtil

class MyApplication : AdMobApplication() {
    override val appOpenAdUnitId: String
        get() = "ca-app-pub-3940256099942544/9257395921" // Test ID for App Open Ad
    override val excludedActivities: Set<String>
        get() = setOf(
            "com.luantrongnguyen.customadsmob.MainActivity", // Example: Exclude MainActivity
        )
}