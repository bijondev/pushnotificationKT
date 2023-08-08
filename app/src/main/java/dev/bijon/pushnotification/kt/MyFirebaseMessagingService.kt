package dev.bijon.pushnotification.kt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var title = ""
        var message = ""


        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            try {
                title = remoteMessage.notification!!.title!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                message = remoteMessage.notification!!.body!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Check if message contains a data payload :: when app in foreground

        // Check if message contains a data payload :: when app in foreground
        val dataPayloads = remoteMessage.data
        if (dataPayloads.size > 0) {
            for (key in dataPayloads.keys) {
                val value = dataPayloads[key]
                Log.i(TAG, "Key: $key Value: $value")
                //do actions using data payloads
            }
        }
    }

//    fun onNewToken(s: String?) {
//        super.onNewToken(s!!)
//        sendTokenToServer(s)
//    }

//    override fun onNewToken(s: String?) {
//        super.onNewToken(s!!)
//        Log.e("newToken", s)
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply()
//    }

    private fun sendTokenToServer(newToken: String) {
        Log.i(TAG, "sendTokenToServer: $newToken")
    }

    private fun sendNotification(messageTitle: String, messageBody: String) {
        Log.i(TAG, "sendNotification: ")
        //'MainActivity' is the target activity. When notification will be clicked, 'MainActivity' will be triggered
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = "OK"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_red)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


}