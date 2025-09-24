package com.example.cafeteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cafeteria.ui.CameraScreen
import com.example.cafeteria.ui.ResultsScreen
import com.example.cafeteria.ui.SettingsScreen
import com.example.cafeteria.ui.theme.CafeteriaTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			CafeteriaTheme {
				Surface(color = MaterialTheme.colorScheme.background) {
					AppNav()
				}
			}
		}
	}
}

@Composable
fun AppNav() {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = "camera") {
		composable("camera") {
			CameraScreen(
				onNavigateToSettings = { navController.navigate("settings") },
				onNavigateToResults = { requestId -> navController.navigate("results/$requestId") }
			)
		}
		composable("settings") {
			SettingsScreen(onBack = { navController.popBackStack() })
		}
		composable("results/{requestId}") { backStackEntry ->
			val requestId = backStackEntry.arguments?.getString("requestId").orEmpty()
			ResultsScreen(requestId = requestId, onBack = { navController.popBackStack() })
		}
	}
}