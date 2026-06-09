package my.lokalan.posq.presentation.utils

import android.app.NotificationManager
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
import my.lokalan.posq.R

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration =
    AlarmeeAndroidPlatformConfiguration(
        notificationIconResId = R.drawable.ic_notification,
        notificationChannels = listOf(
            AlarmeeNotificationChannel(
                id = "dailyNewsChannelId",
                name = "Daily news notifications",
                importance = NotificationManager.IMPORTANCE_HIGH,
                soundFilename = "notifications_sound"
            ),
            AlarmeeNotificationChannel(
                id = "breakingNewsChannelId",
                name = "Breaking news notifications",
                importance = NotificationManager.IMPORTANCE_LOW
            )
        )
    )