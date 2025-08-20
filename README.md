# Utilities-Admobs

Thư viện `Utilities-Admobs` cung cấp các thành phần Composable của Jetpack Compose để tích hợp các loại quảng cáo AdMob (Banner, Interstitial, Rewarded, Rewarded Interstitial, Native, App Open) vào ứng dụng Android một cách dễ dàng và hiệu quả.

## Tính năng

- **Hỗ trợ đầy đủ các loại quảng cáo AdMob**:
  - Banner Ad
  - Interstitial Ad
  - Rewarded Ad
  - Rewarded Interstitial Ad
  - Native Ad
  - App Open Ad (hiển thị khi ứng dụng quay lại từ nền, với tùy chọn loại trừ Activity)
- **Tích hợp Jetpack Compose**: Cung cấp các Composable dễ sử dụng.
- **Quản lý trạng thái quảng cáo**: Sử dụng `AdMobRepository` và `AdMobViewModel`.
- **Hỗ trợ vòng đời ứng dụng**: Tự động hiển thị App Open Ad khi ứng dụng quay lại từ nền, trừ các Activity được chỉ định.
- **Dễ dàng tùy chỉnh**: Hỗ trợ tùy chỉnh giao diện Native Ad qua lambda.

## Cài đặt

### Bước 1: Thêm kho lưu trữ JitPack
Thêm vào `settings.gradle.kts`:

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
Thêm vào `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.luantrongnguyen:Utilities-Admobs:1.0.6")
    implementation("com.google.android.gms:play-services-ads:23.3.0")
}
```

## Cấu hình AdMob

1. **Thêm AdMob App ID**:
   Thêm vào `AndroidManifest.xml`:

   ```xml
   <application>
       <meta-data
           android:name="com.google.android.gms.ads.APPLICATION_ID"
           android:value="ca-app-pub-xxxxxxxxxxxxxxxxxxxx~yyyyyyyyyy" />
   </application>
   ```

2. **Khởi tạo AdMob**:
   Tạo lớp kế thừa `AdMobApplication`:

   ```kotlin
   package com.yourpackage.sampleapp

   import com.luantrongnguyen.ultis_admobs.AdMobApplication

   class MyApplication : AdMobApplication() {
       override val appOpenAdUnitId: String
           get() = "ca-app-pub-3940256099942544/3416116414"

       override val excludedActivities: Set<String>
           get() = setOf(
               "com.yourpackage.sampleapp.MainActivity",
               "com.yourpackage.sampleapp.SettingsActivity"
           )
   }
   ```

   Cập nhật `AndroidManifest.xml`:

   ```xml
   <application
       android:name=".MyApplication"
       ... >
   </application>
   ```

## Cách sử dụng

### 1. App Open Ad
`AppOpenAdUtil` tự động hiển thị quảng cáo App Open khi ứng dụng quay lại từ nền (foreground), trừ các Activity được liệt kê trong `excludedActivities`. Đảm bảo gọi `setCurrentActivity` trong mỗi Activity để liên kết với `AppOpenAdUtil`:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as AdMobApplication).setCurrentActivity(this)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AdTestScreen()
                }
            }
        }
    }
}
```

Để kiểm tra thủ công (ví dụ: trong môi trường phát triển), sử dụng `AppOpenAdHandler`:

```kotlin
@Composable
fun AppOpenAdScreen() {
    val context = LocalContext.current
    val viewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/3416116414")) }
    Button(onClick = { viewModel.loadAppOpenAd(context) }) {
        Text("Load App Open Ad")
    }
    AppOpenAdHandler(
        viewModel = viewModel,
        showOnLoad = false // Rely on AppOpenAdUtil for automatic display
    )
}
```

**Lưu ý**:
- Quảng cáo tự động hiển thị khi ứng dụng vào foreground, trừ các Activity trong `excludedActivities`.
- `showOnLoad = false` đảm bảo quảng cáo không hiển thị ngay khi tải trong `AppOpenAdHandler`, để tránh xung đột với `AppOpenAdUtil`.

### 2. Banner Ad
```kotlin
@Composable
fun BannerAdScreen() {
    BannerAd(adUnitId = "ca-app-pub-3940256099942544/6300978111")
}
```

### 3. Native Ad
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

### 4. Interstitial Ad
```kotlin
@Composable
fun InterstitialAdScreen() {
    val context = LocalContext.current
    val viewModel = remember { AdMobViewModel(AdMobRepository("ca-app-pub-3940256099942544/1033173712")) }
    Button(onClick = { viewModel.loadInterstitialAd(context) }) {
        Text("Load Interstitial Ad")
    }
    InterstitialAdHandler(viewModel = viewModel, showOnLoad = true)
}
```

### 5. Rewarded Ad
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
        viewModel = viewModel,
        onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
        showOnLoad = true
    )
    Text(rewardMessage)
}
```

### 6. Rewarded Interstitial Ad
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
        viewModel = viewModel,
        onReward = { type, amount -> rewardMessage = "Reward: $type, Amount: $amount" },
        showOnLoad = true
    )
    Text(rewardMessage)
}
```

## Giải quyết sự cố

### 1. Lỗi `Cannot access class 'NativeAd'`
- Đảm bảo `com.google.android.gms:play-services-ads:23.3.0` có trong `app/build.gradle.kts` và `ultis-admobs/build.gradle.kts`.
- Kiểm tra import `com.google.android.gms.ads.nativead.NativeAd`.
- Chạy `./gradlew app:dependencies` để kiểm tra xung đột.

### 2. Lỗi `Backend Internal error: Exception during IR lowering`
- Đảm bảo Kotlin (`1.9.24`) và Compose compiler (`1.5.14`) tương thích.
- Thử hạ cấp xuống Kotlin `1.9.22` và `kotlinCompilerExtensionVersion = "1.5.11"`.
- Thêm cấu hình giải quyết xung đột:
  ```kotlin
  configurations.all {
      resolutionStrategy {
          force("com.google.android.gms:play-services-ads:23.3.0")
          force("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
      }
  }
  ```
- Xóa cache IDE: **File > Invalidate Caches / Restart**.

### 3. Quảng cáo App Open hiển thị ở Activity không mong muốn
- Kiểm tra `excludedActivities` trong `MyApplication`.
- Kiểm tra Logcat (tag: `AppOpenAdUtil`) để xác nhận Activity bị loại trừ.

### 4. Quảng cáo không tải
- Sử dụng ID kiểm thử của AdMob.
- Kiểm tra Logcat (tag: `AdMob`) để xem lỗi tải quảng cáo.

## Đóng góp

- Fork repository và tạo pull request với cải tiến hoặc sửa lỗi.
- Báo cáo vấn đề qua [GitHub Issues](https://github.com/luantrongnguyen/Utilities-Admobs/issues).

## Giấy phép

[MIT License](LICENSE).
