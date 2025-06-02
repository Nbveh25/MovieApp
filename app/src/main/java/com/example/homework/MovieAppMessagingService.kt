package com.example.homework

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.content.edit
import com.example.homework.presentaion.MainActivity

class MovieAppMessagingService : FirebaseMessagingService()  {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val category = data["category"]

        when (category) {
            "first" -> {
                // 1. Нарисовать уведомление с максимальным приоритетом
                val title = data["title"] ?: "Нет заголовка"
                val msg = data["message"] ?: "Нет текста"
                showHighPriorityNotification(title, msg)
            }
            "second" -> {
                // 2. Сохранить данные в SharedPreferences или БД
                val extra = data["extra"] ?: ""
                saveToPrefs(category, extra)
            }
            "third" -> {
                // 3. Открыть экран фичи, если приложение открыто
                if (isAppInForeground()) {
                    // Проверить авторизацию, открыть экран или показать Toast
                    handleThirdCategory()
                }
            }
        }
    }

    // Add at class level
    companion object {
        private const val HIGH_PRIORITY_CHANNEL_ID = "high_priority_channel"
        private const val NOTIFICATION_ID = 1
        private const val PREFS_NAME = "MovieAppPrefs"
    }

    private fun showHighPriorityNotification(title: String, message: String) {
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    HIGH_PRIORITY_CHANNEL_ID,
                    "High Priority Channel",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for important notifications"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(this, HIGH_PRIORITY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e("MovieAppMessaging", "Failed to show notification", e)
        }
    }

    private fun saveToPrefs(key: String, value: String) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit() {
                putString(key, value)
            }
    }

    private fun isAppInForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance ==
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }

    private fun handleThirdCategory() {
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val isAuthorized = prefs.getBoolean("is_authorized", false)

        if (isAuthorized) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("navigate_to", "graph_screen")
            }
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Please authorize to access this feature",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}

