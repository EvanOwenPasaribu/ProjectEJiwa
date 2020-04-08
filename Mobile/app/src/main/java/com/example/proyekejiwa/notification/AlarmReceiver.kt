package com.example.proyekejiwa.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.proyekejiwa.MainActivity
import com.example.proyekejiwa.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_TIME = "time"
        private const val ID_REPEATING = 101
    }
    override fun onReceive(context: Context, intent: Intent) {
        val time = intent.getStringExtra(EXTRA_TIME)  //Menerima Intent berupa waktu, agar nanti bisa ditampilkan pada notifikasi.
        showAlarmNotification(context, time) //Menampilkan Notifikasi.
    }
    private fun showAlarmNotification(context: Context, time : String) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"
        //Deklarasi Nootification service
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //mengatur suara saat notifikasi muncul.
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //Mengambil layout yang digunakan pada notifikasi. karena menggunakan Custom layout.
        val contentView = RemoteViews(context.packageName, R.layout.notification_layout)
        //Mengatur teks di custom layout
        contentView.setTextViewText(R.id.tvTimeNotif, "Anda membuat pengingat di jam $time")

        //Deklarasi intent supaya ketika notification di klik, kembali pada Main Activity Aplikasi.
        val intent = Intent(context, MainActivity::class.java)
        //Notifikasi hanya mendukung pending intent, tidak bisa hanya intent.
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications) //setting icon notifikasi ketika muncul di pemberitahuna ponsel.
            .setContent(contentView) //mengatur custom layout notifikasi.
            .setContentIntent(pendingIntent) //Mengatur intent, ketika notifikasi di klik.
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) //getaran yang diterima sata notifikasi muncul.
            .setSound(alarmSound) //Mengatur suara ketika notifikasi muncul.

        //Ketika API level lebih besar sama dengan 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        //agar keluar saat notifikasi diklik.
        builder.setAutoCancel(true)
        val notification = builder.build()//menyimpan build pada variabel
        notificationManagerCompat.notify(ID_REPEATING, notification) //melakukan pemanggilan notifikasi.
    }

    //Repeat Alarm
    fun setRepeatingAlarm(context: Context, time: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java) //Melakukan intent pada AlarmReceiver, ketika sudah diwaktu yang dibuat.
        intent.putExtra(EXTRA_TIME, time) //meletakkan extra data untuk ditampilkan di notifikasi.
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() //mengolah waktu dari bentuk string.
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        //melakukan intent
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        //Mengatur perulangan setiap hari yang akan dilakukan pada waktu yang ditentukan.
        //Mengatur Waktu interval hanya 1 menit agar tidak lama menunggu Video.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 60*1000, pendingIntent)
        Toast.makeText(context, "Notifikasi di Aktifkan", Toast.LENGTH_SHORT).show()
    }

    //Membatalkan Alarm
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java) //intent pada alarm receiver.
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel() //intent di cancel.
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Notifikasi Di Non-Aktifkan", Toast.LENGTH_SHORT).show()
    }
}
