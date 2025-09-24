package com.example.cafeteria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cafeteria.util.AppPreferences
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onBack: () -> Unit) {
	val scope = rememberCoroutineScope()
	val serverUrlFlow = AppPreferences.serverUrlFlow()
	val currentUrl by serverUrlFlow.collectAsState(initial = "")
	var textState by remember(currentUrl) { mutableStateOf(TextFieldValue(currentUrl)) }

	Scaffold(topBar = {
		TopAppBar(title = { Text("设置") })
	}) { padding ->
		Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
			OutlinedTextField(
				value = textState,
				onValueChange = { textState = it },
				label = { Text("服务器地址") },
				modifier = Modifier.fillMaxWidth()
			)
			Spacer(Modifier.height(16.dp))
			Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
				Button(onClick = {
					scope.launch { AppPreferences.setServerUrl(textState.text) }
				}) { Text("保存") }
				OutlinedButton(onClick = onBack) { Text("返回") }
			}
		}
	}
}