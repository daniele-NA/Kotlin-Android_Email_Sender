package com.controller

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.view.R

interface Utils {

    fun startAnimation(component: View) {
        val animation = Runnable {
            component.animate().scaleX(1.2f).scaleY(1.2f)
                .withEndAction {
                    component.scaleX = 1f
                    component.scaleY = 1f
                    component.alpha = 1f
                }
        }
        animation.run()
    }

    fun sendNotification(parent: Context, text: String) {
        if (Bridge.database.notification) {
            val notificationManager = getSystemService(parent, NotificationManager::class.java)
            val channelId = "my_channel_id"
            val channelName = "Notifiche e-mail"
            val channelDescription = "channel_description"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = channelDescription
                notificationManager?.createNotificationChannel(channel)
            }
            val builder = NotificationCompat.Builder(parent, channelId)
                .setSmallIcon(R.drawable.main_icon)
                .setContentTitle(text).setColor(Color.YELLOW)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager?.notify(0, builder.build())
        }
    }

    fun shortToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun longToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}