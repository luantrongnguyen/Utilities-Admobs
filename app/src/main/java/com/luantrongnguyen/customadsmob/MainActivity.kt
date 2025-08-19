package com.luantrongnguyen.customadsmob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.luantrongnguyen.ultis_admobs.AdMobApplication
import com.luantrongnguyen.ultis_admobs.compose.AppOpenAdHandler
import com.luantrongnguyen.ultis_admobs.compose.BannerAd
import com.luantrongnguyen.ultis_admobs.compose.InterstitialAdHandler
import com.luantrongnguyen.ultis_admobs.compose.NativeAd
import com.luantrongnguyen.ultis_admobs.compose.RewardedAdHandler
import com.luantrongnguyen.ultis_admobs.compose.RewardedInterstitialAdHandler
import com.luantrongnguyen.ultis_admobs.repository.AdMobRepository
import com.luantrongnguyen.ultis_admobs.viewmodel.AdMobViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Gọi setCurrentActivity từ AdMobApplication
        (application as AdMobApplication).setCurrentActivity(this)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AdTestScreen()
                }
            }
        }
    }

    @Composable
    fun AdTestScreen() {
        val context = LocalContext.current
        val interstitialViewModel =
            remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/1033173712")) }
        val rewardedViewModel =
            remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/5224354917")) }
        val rewardedInterstitialViewModel =
            remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/5354046379")) }
        val appOpenViewModel =
            remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/3416116414")) }
        var rewardMessage by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Banner Ad", style = MaterialTheme.typography.titleMedium)
            BannerAd(adUnitId = "ca-app-pub-3940256099942544/6300978111")
            Spacer(modifier = Modifier.height(16.dp))

            Text("Interstitial Ad", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { interstitialViewModel.loadInterstitialAd(context) }) {
                Text("Load Interstitial Ad")
            }
            InterstitialAdHandler(
                adUnitId = "ca-app-pub-3940256099942544/1033173712",
                viewModel = interstitialViewModel,
                showOnLoad = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Rewarded Ad", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { rewardedViewModel.loadRewardedAd(context) }) {
                Text("Load Rewarded Ad")
            }
            RewardedAdHandler(
                adUnitId = "ca-app-pub-3940256099942544/5224354917",
                viewModel = rewardedViewModel,
                onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
                showOnLoad = true
            )
            Text(rewardMessage)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Rewarded Interstitial Ad", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { rewardedInterstitialViewModel.loadRewardedInterstitialAd(context) }) {
                Text("Load Rewarded Interstitial Ad")
            }
            RewardedInterstitialAdHandler(
                adUnitId = "ca-app-pub-3940256099942544/5354046379",
                viewModel = rewardedInterstitialViewModel,
                onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
                showOnLoad = true
            )
            Text(rewardMessage)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Native Ad", style = MaterialTheme.typography.titleMedium)
            NativeAd(adUnitId = "ca-app-pub-3940256099942544/2247696110") { nav ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = nav.headline ?: "No Headline",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = nav.body ?: "No Body",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("App Open Ad", style = MaterialTheme.typography.titleMedium)
                Button(onClick = { appOpenViewModel.loadAppOpenAd(context) }) {
                    Text("Load App Open Ad")
                }
                AppOpenAdHandler(
                    adUnitId = "ca-app-pub-3940256099942544/3416116414",
                    viewModel = appOpenViewModel,
                    showOnLoad = true
                )
            }
        }
    }
}