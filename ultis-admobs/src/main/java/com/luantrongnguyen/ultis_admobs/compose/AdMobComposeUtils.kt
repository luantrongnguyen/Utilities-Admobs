package com.luantrongnguyen.ultis_admobs.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.luantrongnguyen.ultis_admobs.repository.AdMobRepository
import com.luantrongnguyen.ultis_admobs.viewmodel.AdMobViewModel

@Composable
fun AdStateHandler(
    adState: AdMobRepository.AdState,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    errorContent: @Composable (String) -> Unit = { message -> DefaultErrorContent(message) },
    content: @Composable () -> Unit
) {
    when (adState) {
        is AdMobRepository.AdState.Loading -> loadingContent()
        is AdMobRepository.AdState.Error -> errorContent(adState.message)
        else -> content()
    }
}

@Composable
fun DefaultLoadingContent() {
    CircularProgressIndicator()
}

@Composable
fun DefaultErrorContent(message: String) {
    Text(text = "Ad Error: $message")
}

@Composable
fun BannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun NativeAd(
    adUnitId: String,
    viewModel: AdMobViewModel = remember { AdMobViewModel(AdMobRepository(adUnitId)) },
    nativeAdContent: @Composable (NativeAd) -> Unit
) {
    val context = LocalContext.current
    val adState by viewModel.adState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadNativeAd(context)
    }

    AdStateHandler(
        adState = adState,
        content = {
            if (adState is AdMobRepository.AdState.LoadedNative) {
                nativeAdContent((adState as AdMobRepository.AdState.LoadedNative).ad)
            }
        }
    )
}

@Composable
fun InterstitialAdHandler(
    adUnitId: String,
    viewModel: AdMobViewModel = remember { AdMobViewModel(AdMobRepository(adUnitId)) },
    showOnLoad: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val adState by viewModel.adState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInterstitialAd(context)
    }

    AdStateHandler(
        adState = adState,
        content = {
            if (showOnLoad && adState is AdMobRepository.AdState.LoadedInterstitial) {
                LaunchedEffect(adState) {
                    viewModel.showInterstitialAd(lifecycleOwner as android.app.Activity)
                }
            }
        }
    )
}

@Composable
fun RewardedAdHandler(
    adUnitId: String,
    viewModel: AdMobViewModel = remember { AdMobViewModel(AdMobRepository(adUnitId)) },
    onReward: (type: String, amount: Int) -> Unit,
    showOnLoad: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val adState by viewModel.adState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRewardedAd(context)
    }

    AdStateHandler(
        adState = adState,
        content = {
            if (showOnLoad && adState is AdMobRepository.AdState.LoadedRewarded) {
                LaunchedEffect(adState) {
                    viewModel.showRewardedAd(lifecycleOwner as android.app.Activity, onReward)
                }
            }
        }
    )
}

@Composable
fun RewardedInterstitialAdHandler(
    adUnitId: String,
    viewModel: AdMobViewModel = remember { AdMobViewModel(AdMobRepository(adUnitId)) },
    onReward: (type: String, amount: Int) -> Unit,
    showOnLoad: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val adState by viewModel.adState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRewardedInterstitialAd(context)
    }

    AdStateHandler(
        adState = adState,
        content = {
            if (showOnLoad && adState is AdMobRepository.AdState.LoadedRewardedInterstitial) {
                LaunchedEffect(adState) {
                    viewModel.showRewardedInterstitialAd(
                        lifecycleOwner as android.app.Activity,
                        onReward
                    )
                }
            }
        }
    )
}

@Composable
fun AppOpenAdHandler(
    adUnitId: String,
    viewModel: AdMobViewModel = remember { AdMobViewModel(AdMobRepository(adUnitId)) },
    showOnLoad: Boolean = false,
    orientation: Int = com.google.android.gms.ads.appopen.AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val adState by viewModel.adState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAppOpenAd(context, orientation)
    }

    AdStateHandler(
        adState = adState,
        content = {
            if (showOnLoad && adState is AdMobRepository.AdState.LoadedAppOpen) {
                LaunchedEffect(adState) {
                    viewModel.showAppOpenAd(lifecycleOwner as android.app.Activity)
                }
            }
        }
    )
}

@Composable
fun ExampleNativeAdView(nativeAd: NativeAd) {
    AndroidView(factory = { context ->
        NativeAdView(context).apply {
            headlineView = android.widget.TextView(context).also { it.text = nativeAd.headline }
            bodyView = android.widget.TextView(context).also { it.text = nativeAd.body }
            setNativeAd(nativeAd)
        }
    })
}