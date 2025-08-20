package com.luantrongnguyen.customadsmob

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
            remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/9257395921")) }
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
            Button(onClick = { interstitialViewModel.showInterstitialAd(context as Activity) }) {
                Text("Show Interstitial Ad")
            }
            InterstitialAdHandler(
                viewModel = interstitialViewModel,
                showOnLoad = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Rewarded Ad", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { rewardedViewModel.showRewardedAd(context as Activity, onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" }) }) {
                Text("Show Rewarded Ad")
            }
            RewardedAdHandler(
                viewModel = rewardedViewModel,
                showOnLoad = false
            )
            Text(rewardMessage)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Rewarded Interstitial Ad", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { rewardedInterstitialViewModel.showRewardedInterstitialAd(context as Activity, onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },) }) {
                Text("Show Rewarded Interstitial Ad")
            }
            RewardedInterstitialAdHandler(
                viewModel = rewardedInterstitialViewModel,
//                onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
                showOnLoad = false
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
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("App Open Ad", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { appOpenViewModel.showAppOpenAd(context as Activity, onFailedToShow = { mess -> Log.d("open",mess)}) }) {
                Text("Load App Open Ad")
            }
            AppOpenAdHandler(
                viewModel = appOpenViewModel,
                showOnLoad = true
            )

        }
    }
}