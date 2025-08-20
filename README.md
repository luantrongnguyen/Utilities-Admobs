# Utilities-Admobs

Thư viện `Utilities-Admobs` cung cấp các thành phần Composable của Jetpack Compose để tích hợp các loại quảng cáo AdMob (Banner, Interstitial, Rewarded, Rewarded Interstitial, Native, App Open) vào ứng dụng Android một cách dễ dàng và hiệu quả. Thư viện được thiết kế để đơn giản hóa việc quản lý trạng thái quảng cáo và tích hợp với kiến trúc MVVM.

## Tính năng

- **Hỗ trợ đầy đủ các loại quảng cáo AdMob**:
  - Banner Ad
  - Interstitial Ad
  - Rewarded Ad
  - Rewarded Interstitial Ad
  - Native Ad
  - App Open Ad
- **Tích hợp Jetpack Compose**: Cung cấp các Composable dễ sử dụng để hiển thị quảng cáo.
- **Quản lý trạng thái quảng cáo**: Sử dụng `AdMobRepository` và `AdMobViewModel` để xử lý việc tải và hiển thị quảng cáo.
- **Hỗ trợ vòng đời ứng dụng**: Tích hợp với `Lifecycle` để quản lý quảng cáo App Open khi ứng dụng vào trạng thái foreground.
- **Dễ dàng tùy chỉnh**: Hỗ trợ các tùy chỉnh giao diện Native Ad thông qua lambda.

## Cài đặt

### Bước 1: Thêm kho lưu trữ JitPack
Thêm kho lưu trữ JitPack vào file `settings.gradle.kts` của dự án, trong phần `dependencyResolutionManagement`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Bước 2: Thêm phụ thuộc
Thêm phụ thuộc `Utilities-Admobs` vào file `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.luantrongnguyen:Utilities-Admobs:1.0.5")
    implementation("com.google.android.gms:play-services-ads:23.3.0")
}
```

### Bước 3: Đồng bộ dự án
Đồng bộ dự án với Gradle bằng cách nhấn **Sync Project with Gradle Files** trong Android Studio.


## Cấu hình AdMob

1. **Thêm AdMob App ID**:
   Thêm AdMob App ID vào `AndroidManifest.xml`:

   ```xml
   <application>
       <meta-data
           android:name="com.google.android.gms.ads.APPLICATION_ID"
           android:value="ca-app-pub-xxxxxxxxxxxxxxxxxxxx~yyyyyyyyyy" />
   </application>
   ```

   Sử dụng ID kiểm thử: `ca-app-pub-3940256099942544~3347511713` cho môi trường phát triển.

2. **Khởi tạo AdMob**:
   Tạo một lớp kế thừa `AdMobApplication`:

   ```kotlin
   package com.yourpackage.sampleapp

   import com.luantrongnguyen.ultis_admobs.AdMobApplication

   class MyApplication : AdMobApplication() {
       override val appOpenAdUnitId: String
           get() = "ca-app-pub-3940256099942544/3416116414"
   }
   ```

   Cập nhật `AndroidManifest.xml` để sử dụng lớp này:

   ```xml
   <application
       android:name=".MyApplication"
       ... >
   </application>
   ```

## Cách sử dụng

### 1. Banner Ad
Hiển thị quảng cáo Banner:

```kotlin
@Composable
fun BannerAdScreen() {
    BannerAd(adUnitId = "ca-app-pub-3940256099942544/6300978111")
}
```

### 2. Native Ad
Hiển thị quảng cáo Native với giao diện tùy chỉnh:

```kotlin
@Composable
fun NativeAdScreen() {
    NativeAd(adUnitId = "ca-app-pub-3940256099942544/2247696110") { nativeAd ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = nativeAd.headline ?: "No Headline",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = nativeAd.body ?: "No Body",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

### 3. Interstitial Ad
Tải và hiển thị quảng cáo Interstitial:

```kotlin
@Composable
fun InterstitialAdScreen() {
    val context = LocalContext.current
    val viewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/1033173712")) }
    Button(onClick = { viewModel.loadInterstitialAd(context) }) {
        Text("Load Interstitial Ad")
    }
    InterstitialAdHandler(adUnitId = "ca-app-pub-3940256099942544/1033173712", viewModel = viewModel, showOnLoad = true)
}
```

### 4. Rewarded Ad
Tải và hiển thị quảng cáo Rewarded với phần thưởng:

```kotlin
@Composable
fun RewardedAdScreen() {
    val context = LocalContext.current
    val viewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/5224354917")) }
    var rewardMessage by remember { mutableStateOf("") }
    Button(onClick = { viewModel.loadRewardedAd(context) }) {
        Text("Load Rewarded Ad")
    }
    RewardedAdHandler(
        adUnitId = "ca-app-pub-3940256099942544/5224354917",
        viewModel = viewModel,
        onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
        showOnLoad = true
    )
    Text(rewardMessage)
}
```

### 5. Rewarded Interstitial Ad
Tương tự Rewarded Ad nhưng sử dụng ID quảng cáo khác:

```kotlin
@Composable
fun RewardedInterstitialAdScreen() {
    val context = LocalContext.current
    val viewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/5354046379")) }
    var rewardMessage by remember { mutableStateOf("") }
    Button(onClick = { viewModel.loadRewardedInterstitialAd(context) }) {
        Text("Load Rewarded Interstitial Ad")
    }
    RewardedInterstitialAdHandler(
        adUnitId = "ca-app-pub-3940256099942544/5354046379",
        viewModel = viewModel,
        onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
        showOnLoad = true
    )
    Text(rewardMessage)
}
```

### 6. App Open Ad
Hiển thị quảng cáo khi ứng dụng vào foreground:

```kotlin
@Composable
fun AppOpenAdScreen() {
    val context = LocalContext.current
    val viewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/3416116414")) }
    Button(onClick = { viewModel.loadAppOpenAd(context) }) {
        Text("Load App Open Ad")
    }
    AppOpenAdHandler(adUnitId = "ca-app-pub-3940256099942544/3416116414", viewModel = viewModel, showOnLoad = true)
}
```

## Ví dụ mẫu

Tạo một màn hình kiểm thử hiển thị tất cả các loại quảng cáo:

```kotlin
@Composable
fun AdTestScreen() {
    val context = LocalContext.current
    val interstitialViewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/1033173712")) }
    val rewardedViewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/5224354917")) }
    val rewardedInterstitialViewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/5354046379")) }
    val appOpenViewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/3416116414")) }
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

        Text("Native Ad", style = MaterialTheme.typography.titleMedium)
        NativeAd(adUnitId = "ca-app-pub-3940256099942544/2247696110") { nativeAd ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = nativeAd.headline ?: "No Headline",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = nativeAd.body ?: "No Body",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Interstitial Ad", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { interstitialViewModel.loadInterstitialAd(context) }) {
            Text("Load Interstitial Ad")
        }
        InterstitialAdHandler(adUnitId = "ca-app-pub-3940256099942544/1033173712", viewModel = interstitialViewModel, showOnLoad = true)
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

        Text("App Open Ad", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { appOpenViewModel.loadAppOpenAd(context) }) {
            Text("Load App Open Ad")
        }
        AppOpenAdHandler(adUnitId = "ca-app-pub-3940256099942544/3416116414", viewModel = appOpenViewModel, showOnLoad = true)
    }
}
```

## Đóng góp

- Fork repository và tạo pull request với các cải tiến hoặc sửa lỗi.
- Báo cáo vấn đề qua [GitHub Issues](https://github.com/luantrongnguyen/Utilities-Admobs/issues).

## Giấy phép

Thư viện được phát hành dưới [MIT License](LICENSE).
