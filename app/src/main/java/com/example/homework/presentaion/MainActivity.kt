package com.example.homework.presentaion

import android.Manifest
import android.R.attr.key
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.homework.presentaion.navigation.AppNavigation
import com.example.homework.presentaion.navigation.Destinations
import com.example.homework.utils.RemoteConfigFlag
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Cache.Companion.key
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }

            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                if (intent?.getStringExtra("navigate_to") == "graph_screen") {
                    navController.navigate(Destinations.GRAPH) {
                        popUpTo(Destinations.MOVIE_LIST) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }

            if (intent?.getStringExtra("navigate_to") == "graph_screen") {
                navController.navigate(route = Destinations.GRAPH)
            }

            /*val id = UUID.randomUUID()
            Firebase.crashlytics.setCustomKeys {
                key("user_id", "$id")
            }

            lifecycleScope.launch {
                delay(3000)
                throw IllegalArgumentException("Test crash")
            }*/

            val remoteConfig = Firebase.remoteConfig

            val configDefaults = mapOf("test_feature_flag" to false)
            remoteConfig.setDefaultsAsync(configDefaults)

            val fetchInterval = if (BuildConfig.DEBUG) {
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