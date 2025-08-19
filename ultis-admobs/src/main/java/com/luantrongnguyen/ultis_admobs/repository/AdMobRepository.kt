package com.luantrongnguyen.ultis_admobs.repository
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdMobRepository(private val adUnitId: String) {
    private val _adState = MutableStateFlow<AdState>(AdState.Loading)
    val adState: StateFlow<AdState> = _adState

    // Load Banner (không cần load async, vì Banner load trực tiếp trong View)
    // Banner không cần hàm load riêng ở repo, vì nó load trong Composable

    // Load Interstitial Ad
    fun loadInterstitialAd(context: Context) {
        _adState.value = AdState.Loading
        InterstitialAd.load(context, adUnitId, AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                _adState.value = AdState.LoadedInterstitial(ad)
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                _adState.value = AdState.Error(error.message)
            }
        })
    }

    // Load Rewarded Ad
    fun loadRewardedAd(context: Context) {
        _adState.value = AdState.Loading
        RewardedAd.load(context, adUnitId, AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                _adState.value = AdState.LoadedRewarded(ad)
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                _adState.value = AdState.Error(error.message)
            }
        })
    }

    // Load Rewarded Interstitial Ad
    fun loadRewardedInterstitialAd(context: Context) {
        _adState.value = AdState.Loading
        RewardedInterstitialAd.load(context, adUnitId, AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                _adState.value = AdState.LoadedRewardedInterstitial(ad)
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                _adState.value = AdState.Error(error.message)
            }
        })
    }

    // Load Native Ad
    fun loadNativeAd(context: Context) {
        _adState.value = AdState.Loading
        val adLoader = com.google.android.gms.ads.AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->
                _adState.value = AdState.LoadedNative(ad)
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    _adState.value = AdState.Error(error.message)
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    // Load App Open Ad
    fun loadAppOpenAd(context: Context, orientation: Int = AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT) {
        _adState.value = AdState.Loading
        AppOpenAd.load(context, adUnitId, AdRequest.Builder().build(), orientation, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                _adState.value = AdState.LoadedAppOpen(ad)
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                _adState.value = AdState.Error(error.message)
            }
        })
    }

    sealed class AdState {
        object Idle : AdState()
        object Loading : AdState()
        data class LoadedInterstitial(val ad: InterstitialAd) : AdState()
        data class LoadedRewarded(val ad: RewardedAd) : AdState()
        data class LoadedRewardedInterstitial(val ad: RewardedInterstitialAd) : AdState()
        data class LoadedNative(val ad: NativeAd) : AdState()
        data class LoadedAppOpen(val ad: AppOpenAd) : AdState()
        data class Error(val message: String) : AdState()
    }
}