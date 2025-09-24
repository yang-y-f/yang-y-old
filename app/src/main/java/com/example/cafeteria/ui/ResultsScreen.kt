package com.example.cafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cafeteria.data.BackendApi
import kotlinx.coroutines.launch

@Composable
fun ResultsScreen(requestId: String, onBack: () -> Unit) {
	val scope = rememberCoroutineScope()
	var resultText by remember { mutableStateOf("正在获取结果…") }

	LaunchedEffect(requestId) {
		scope.launch {
			resultText = BackendApi.fetchResult(requestId)
		}
	}

	Scaffold(topBar = {
		TopAppBar(title = { Text("识别结果") })
	}) { padding ->
		Column(
			modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(text = resultText, style = MaterialTheme.typography.titleLarge)
			Spacer(Modifier.height(24.dp))
			Button(onClick = onBack) { Text("返回") }
		}
	}
}