package com.d204.rumeet.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.d204.rumeet.R
import com.d204.rumeet.ui.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kakao.sdk.common.KakaoSdk.type

class FcmSettingTool: FirebaseMessagingService() {
    private lateinit var builder: NotificationCompat.Builder
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationMessage = remoteMessage.notification!!

        builder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager.getNotificationChannel(CHANNEL_ID) == null){
                val channel = NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(applicationContext)
        }

        val title = notificationMessage.title.toString()
        val content = notificationMessage.body.toString()
        val type = remoteMessage.data["type"].toString()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("type", type)
        }

        Log.d(TAG, "onMessageReceived: $title , $content, $type")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_main_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FcmSetting..."
        private const val CHANNEL_ID = "rumeet"
        private const val CHANNEL_NAME = "러밋 푸쉬 알림"
        private const val NOTIFICATION_ID = 204
    }
}