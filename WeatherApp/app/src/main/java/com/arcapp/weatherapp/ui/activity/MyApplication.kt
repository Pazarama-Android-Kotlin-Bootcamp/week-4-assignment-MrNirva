package com.arcapp.weatherapp.ui.activity

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.arcapp.weatherapp.constant.Constants
import com.arcapp.weatherapp.data.preference.SharedPref
import com.arcapp.weatherapp.enum.AppTheme

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPref = SharedPref(applicationContext)

        when(sharedPref.getAppTheme()){

            AppTheme.System.toString() -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
            }

            AppTheme.Light.toString() -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            AppTheme.Dark.toString() -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }

    }

}