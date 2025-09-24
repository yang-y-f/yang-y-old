plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-parcelize")
}

android {
	namespace = "com.example.cafeteria"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.cafeteria"
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
		debug {
			isMinifyEnabled = false
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs = freeCompilerArgs + listOf(
			"-Xjvm-default=all"
		)
	}

	buildFeatures {
		compose = true
		buildConfig = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.15"
	}

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	implementation(platform("androidx.compose:compose-bom:2024.09.00"))
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
	implementation("androidx.navigation:navigation-compose:2.8.2")

	// Compose UI
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.ui:ui-tooling-preview")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	// CameraX
	val cameraXVersion = "1.3.4"
	implementation("androidx.camera:camera-core:$cameraXVersion")
	implementation("androidx.camera:camera-camera2:$cameraXVersion")
	implementation("androidx.camera:camera-lifecycle:$cameraXVersion")
	implementation("androidx.camera:camera-view:$cameraXVersion")

	// Networking - Retrofit + Moshi + OkHttp
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
	implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

	// DataStore
	implementation("androidx.datastore:datastore-preferences:1.1.1")

	// Coil for image loading
	implementation("io.coil-kt:coil-compose:2.6.0")

	// Material Icons
	implementation("androidx.compose.material:material-icons-extended")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
	androidTestImplementation("androidx.test.ext:junit:1.2.1")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}