package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url = ""


    //    private val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Timber.plant(Timber.DebugTree())

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        notificationManager = getSystemService(NotificationManager::class.java)

        custom_button.setOnClickListener {
            createChannel(notificationManager, getString(R.string.notification_channel_id), getString(R.string.notification_channel_name))
            download(when(radio_group.checkedRadioButtonId) {
                R.id.radio_button_glide -> Link.GLIDE
                R.id.radio_button_load_app -> Link.LOAD_APP
                R.id.radio_button_retrofit -> Link.RETROFIT
                else -> Link.NULL
            })
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor: Cursor = downloadManager.query(DownloadManager.Query())
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL
                    val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    notificationManager.sendNotification(title, status, applicationContext)
                }
            }
        }
    }

    private fun download(link: Link) {
        if (link == Link.NULL) {
            Toast.makeText(applicationContext, getString(R.string.radio_button_not_selected), Toast.LENGTH_SHORT).show()
        } else {
            val request = DownloadManager.Request(Uri.parse(link.url))
                .setTitle(getString(link.title))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            notificationManager.cancelNotifications()
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
            custom_button.buttonState = ButtonState.Clicked
        }
    }
}
