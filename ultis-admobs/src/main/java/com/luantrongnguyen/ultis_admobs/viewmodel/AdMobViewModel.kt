package com.luantrongnguyen.ultis_admobs.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.luantrongnguyen.ultis_admobs.repository.AdMobRepository

class AdMobViewModel(private val repository: AdMobRepository) : ViewModel() {
    val adState: StateFlow<AdMobRepository.AdState> = repository.adState

    // Load functions
    
    fun loadInterstitialAd(context: android.content.Context) {
        viewModelScope.launch { repository.loadInterstitialAd(context) }
    }

    fun loadRewardedAd(context: android.content.Context) {
        viewModelScope.launch { repository.loadRewardedAd(context) }
    }

    fun loadRewardedInterstitialAd(context: android.content.Context) {
        viewModelScope.launch { repository.loadRewardedInterstitialAd(context) }
    }

    fun loadNativeAd(context: android.content.Context) {
        viewModelScope.launch { repository.loadNativeAd(context) }
    }

    fun loadAppOpenAd(context: Context, orientation: Int = AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT) {
        viewModelScope.launch { repository.loadAppOpenAd(context, orientation) }
    }

    // Show functions (cho các ad fullscreen)
    fun showInterstitialAd(activity: Activity, onDismissed: () -> Unit = {}, onFailedToShow: (String) -> Unit = {}) {
        val state = adState.value
        if (state is AdMobRepository.AdState.LoadedInterstitial) {
            state.ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() { onDismissed() }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onFailedToShow(error.message)
                }
            }
            state.ad.show(activity)
        }
    }

    fun showRewardedAd(activity: Activity, onReward: (String, Int) -> Unit, onDismissed: () -> Unit = {}, onFailedToShow: (String) -> Unit = {}) {
        val state = adState.value
        if (state is AdMobRepository.AdState.LoadedRewarded) {
            state.ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() { onDismissed() }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onFailedToShow(error.message)
                }
            }
            state.ad.show(activity) { rewardItem ->
                onReward(rewardItem.type, rewardItem.amount)
            }
        }
    }

    fun showRewardedInterstitialAd(activity: Activity, onReward: (type: String, amount: Int) -> Unit, onDismissed: () -> Unit = {}, onFailedToShow: (String) -> Unit = {}) {
        val state = adState.value
        if (state is AdMobRepository.AdState.LoadedRewardedInterstitial) {
            state.ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() { onDismissed() }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onFailedToShow(error.message)
                }
            }
            state.ad.show(activity) { rewardItem ->
                onReward(rewardItem.type, rewardItem.amount)
            }
        }
    }

    fun showAppOpenAd(activity: Activity, onDismissed: () -> Unit = {}, onFailedToShow: (String) -> Unit = {}) {
        val state = adState.value
        if (state is AdMobRepository.AdState.LoadedAppOpen) {
            state.ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() { onDismissed() }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onFailedToShow(error.message)
                }
            }
            state.ad.show(activity)
        }
    }
}