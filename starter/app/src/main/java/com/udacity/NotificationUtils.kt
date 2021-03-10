package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0

//    private lateinit var notificationManager: NotificationManager
private lateinit var pendingIntent: PendingIntent
private lateinit var action: NotificationCompat.Action

fun NotificationManager.sendNotification(title: String, status: Boolean, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(Constants.TITLE, title)
    contentIntent.putExtra(Constants.STATUS, status)
    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.notification_channel_id))
            .setSmallIcon(R.drawable.ic_cloud_download)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(title)
            .setAutoCancel(true)
            .addAction(
                    R.drawable.ic_cloud_download,
                    applicationContext.getString(R.string.notification_button),
                    contentPendingIntent
            )
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}