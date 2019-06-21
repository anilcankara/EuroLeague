package com.example.anil.euroleague

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // createNotificationChannel()

        standings.setOnClickListener{
            val standings = Intent(this, Standings::class.java )
            startActivity(standings)
        }

        livescores.setOnClickListener{
            val livescores = Intent(this, Livescore::class.java)
            startActivity(livescores)
        }
        results.setOnClickListener{
            val results = Intent(this, Results::class.java)
            startActivity(results)
        }

        //Notification, will be implemented later
       /* val date = Calendar.getInstance().set(2019,1,21,22,30).getTimeInMillis()

        var mBuilder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.vesely)
            .setContentTitle("DENEME")
            .setContentText("Bu bir bildirimdir.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setWhen(date)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, mBuilder.build())
        } */

    }
    /* Notification
     fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // val name = getString(R.string.channel_name)
           // val descriptionText = getString(R.string.channel_description)
            val name = "My Notification Name"
            val descriptionText = "My description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    } */
}
