package com.example.cafeteria.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.cafeteria.data.BackendApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
	onNavigateToSettings: () -> Unit,
	onNavigateToResults: (String) -> Unit
) {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val coroutineScope = rememberCoroutineScope()

	var hasCameraPermission by remember { mutableStateOf(
		ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
	) }
	val cameraPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { granted -> hasCameraPermission = granted }
	)

	LaunchedEffect(Unit) {
		if (!hasCameraPermission) cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
	}

	var trayDetected by remember { mutableStateOf(false) }
	var isCapturing by remember { mutableStateOf(false) }
	var uploading by remember { mutableStateOf(false) }
	val imageCapture = remember { ImageCapture.Builder().setTargetResolution(Size(1280, 720)).build() }

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Cafeteria") },
				actions = {
					IconButton(onClick = onNavigateToSettings) {
						Icon(Icons.Default.Settings, contentDescription = null)
					}
				}
			)
		},
		bottomBar = {
			BottomAppBar {
				Spacer(modifier = Modifier.weight(1f))
				Button(
					enabled = trayDetected && !isCapturing && !uploading,
					onClick = {
						isCapturing = true
						val outputFile = File.createTempFile("capture", ".jpg", context.cacheDir)
						val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
						imageCapture.takePicture(
							outputOptions,
							ContextCompat.getMainExecutor(context),
							object : ImageCapture.OnImageSavedCallback {
								override fun onError(exception: ImageCaptureException) {
									isCapturing = false
								}
								override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
									coroutineScope.launch {
										uploading = true
										val bytes = withContext(Dispatchers.IO) { outputFile.readBytes() }
										val requestId = withContext(Dispatchers.IO) { BackendApi.uploadImage(bytes) }
										uploading = false
										isCapturing = false
										onNavigateToResults(requestId)
									}
								}
							}
						)
					}
				) { Text(text = if (trayDetected) "已检测到餐盘" else "未检测到餐盘") }
				Spacer(modifier = Modifier.weight(1f))
			}
		}
	) { padding ->
		Box(Modifier.fillMaxSize().padding(padding)) {
			if (!hasCameraPermission) {
				Text(text = "需要相机权限", modifier = Modifier.align(Alignment.Center))
			} else {
				AndroidView(
					factory = { ctx ->
						PreviewView(ctx).apply {
							val cameraProvider = ProcessCameraProvider.getInstance(ctx).get()
							val preview = androidx.camera.core.Preview.Builder().build().also { it.setSurfaceProvider(surfaceProvider) }
							val selector = CameraSelector.DEFAULT_BACK_CAMERA
							val analysis = ImageAnalysis.Builder()
								.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
								.build()
							analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->
								trayDetected = detectTray(image.cropRect)
								image.close()
							}
							cameraProvider.unbindAll()
							cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, imageCapture, analysis)
						}
					},
					modifier = Modifier.fillMaxSize()
				)
			}
			if (uploading) {
				CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
			}
		}
	}
}

private fun detectTray(rect: Rect): Boolean {
	return rect.width() > 0 && rect.height() > 0
}