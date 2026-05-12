plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    // Enable ViewBinding so we can access XML views by name without findViewById
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // RecyclerView for displaying product and cart lists
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView for product/cart item cards
    implementation("androidx.cardview:cardview:1.0.0")

    // ViewModel and LiveData for MVVM architecture
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // activity-ktx provides the 'by viewModels()' delegate used in HomeActivity & CartActivity
    // Without this, the app will not compile
    implementation("androidx.activity:activity-ktx:1.8.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // DrawerLayout for category sidebar
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")

    // ViewPager2 for promotional banner carousel
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Shimmer effect for loading states
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // ── Feature 2: Room Database for Order History ──────────────────────────
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // ── Feature 4: Lottie for animated splash screen ─────────────────────────
    implementation("com.airbnb.android:lottie:6.4.0")

    // WorkManager for background offers
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}