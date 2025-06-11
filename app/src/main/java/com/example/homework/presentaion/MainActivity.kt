package com.example.homework.presentaion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.app.R
import com.example.homework.utils.RemoteConfigFlag
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance())
                .commit()
        }
        
        setupRemoteConfig()
        checkNotificationPermission()
    }
    
    private fun setupRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig

        val configDefaults = mapOf("test_feature_flag" to false)
        remoteConfig.setDefaultsAsync(configDefaults)

        val fetchInterval = if (com.google.firebase.BuildConfig.DEBUG) {
            0L
        } else {
            43200L
        }
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = fetchInterval
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    val isFeatureEnabled = remoteConfig.getBoolean("test_feature_flag")
                    RemoteConfigFlag.TEST_FEATURE = isFeatureEnabled
                } else {
                    RemoteConfigFlag.TEST_FEATURE = false
                }
            } catch (e: Exception) {
                RemoteConfigFlag.TEST_FEATURE = false
            }
        }
    }
    
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Разрешение получено
        } else {
            // Разрешение не получено
        }
    }
}