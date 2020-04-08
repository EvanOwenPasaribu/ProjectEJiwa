package com.example.proyekejiwa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.proyekejiwa.R
import com.example.proyekejiwa.notification.SettingNotificationActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        tvSettingNotif.setOnClickListener(this)
        tvPengggunaanApp.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.tvSettingNotif) {
            startActivity(Intent(this, SettingNotificationActivity::class.java))
        }
    }
}
