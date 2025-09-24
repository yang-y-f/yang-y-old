package com.example.cafeteria

import android.app.Application
import com.example.cafeteria.util.AppPreferences

class CafeteriaApp : Application() {
	override fun onCreate() {
		super.onCreate()
		AppPreferences.init(this)
	}
}