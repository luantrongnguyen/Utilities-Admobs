package com.luantrongnguyen.ultis_admobs

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.luantrongnguyen.ultis_admobs.repository.AdMobRepository
import com.luantrongnguyen.ultis_admobs.viewmodel.AdMobViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class AppOpenAdUtil(
    private val application: Application,
    private val adUnitId: String,
    private val viewModel: AdMobViewModel = AdMobViewModel(AdMobRepository(adUnitId)),
    private val excludedActivities: Set<String> = emptySet() // List of activity class names to exclude
) : LifecycleObserver {

    private var isShowingAd = AtomicBoolean(false)
    private var currentActivity: android.app.Activity? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var wasBackgrounded = false

    val adState: StateFlow<AdMobRepository.AdState> = viewModel.adState

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        // Preload ad on initialization
        loadAndShowAd()
        scope.launch {
            adState.collectLatest { state ->
                if (state is AdMobRepository.AdState.LoadedAppOpen && !isShowingAd.get() && currentActivity != null && wasBackgrounded) {
                    // Check if current activity is in excluded list
                    val currentActivityName = currentActivity!!::class.java.name
                    if (currentActivityName !in excludedActivities) {
                        viewModel.showAppOpenAd(
                            activity = currentActivity!!,
                            onDismissed = {
                                isShowingAd.set(false)
                                wasBackgrounded = false
                                loadAndShowAd() // Preload next ad
                            },
                            onFailedToShow = {
                                isShowingAd.set(false)
                                wasBackgrounded = false
                                loadAndShowAd() // Retry loading
                            }
                        )
                        isShowingAd.set(true)
                    } else {
                        isShowingAd.set(false) // Reset flag if ad is skipped
                        println("AppOpenAdUtil: Ad skipped for excluded activity $currentActivityName")
                    }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        println("AppOpenAdUtil: App moved to foreground, wasBackgrounded=$wasBackgrounded, currentActivity=${currentActivity?.javaClass?.name}")
        if (!isShowingAd.get()) {
            loadAndShowAd()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackground() {
        println("AppOpenAdUtil: App moved to background")
        wasBackgrounded = true
    }

    fun setCurrentActivity(activity: android.app.Activity) {
        currentActivity = activity
    }

    private fun loadAndShowAd() {
        viewModel.loadAppOpenAd(application, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT)
    }

    fun cleanup() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        scope.cancel()
    }
}