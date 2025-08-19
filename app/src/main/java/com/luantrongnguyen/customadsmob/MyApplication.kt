package com.luantrongnguyen.customadsmob

import android.app.Application
import com.luantrongnguyen.ultis_admobs.AdMobApplication
import com.luantrongnguyen.ultis_admobs.AppOpenAdUtil

class MyApplication : AdMobApplication() {
    override val appOpenAdUnitId: String
        get() = "ca-app-pub-3940256099942544/3416116414" // Test ID for App Open Ad
}