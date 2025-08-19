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
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class AppOpenAdUtil(
    private val application: Application,
    private val adUnitId: String,
    private val viewModel: AdMobViewModel = AdMobViewModel(AdMobRepository(adUnitId))
) : LifecycleObserver {

    private var isShowingAd = false
    private var currentActivity: android.app.Activity? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Trạng thái quảng cáo từ ViewModel
    val adState: StateFlow<AdMobRepository.AdState> = viewModel.adState

    init {
        // Đăng ký observer để theo dõi lifecycle của ứng dụng
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        // Theo dõi trạng thái quảng cáo
        scope.launch {
            adState.collectLatest { state ->
                if (state is AdMobRepository.AdState.LoadedAppOpen && !isShowingAd && currentActivity != null) {
                    isShowingAd = true
                    viewModel.showAppOpenAd(
                        activity = currentActivity!!,
                        onDismissed = { isShowingAd = false },
                        onFailedToShow = { isShowingAd = false }
                    )
                }
            }
        }
    }

    // Gọi khi ứng dụng vào foreground
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        if (!isShowingAd) {
            loadAndShowAd()
        }
    }

    // Thiết lập activity hiện tại (gọi từ Activity)
    fun setCurrentActivity(activity: android.app.Activity) {
        currentActivity = activity
    }

    private fun loadAndShowAd() {
        viewModel.loadAppOpenAd(application, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT)
    }

    // Hủy observer và coroutine scope khi không cần thiết
    fun cleanup() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        scope.cancel()
    }
}