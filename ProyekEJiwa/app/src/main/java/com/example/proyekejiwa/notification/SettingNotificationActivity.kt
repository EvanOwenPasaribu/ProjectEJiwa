package com.example.proyekejiwa.notification

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color.BLACK
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyekejiwa.R
import kotlinx.android.synthetic.main.activity_setting_notification.*
import java.util.*

class SettingNotificationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var alarmReceiver: AlarmReceiver  //deklarasi AlarmReceiver agar terjadi perulangan waktu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_notification)
        swNotif.setOnClickListener(this) //saat switch di klik
        btn_time.setOnClickListener(this) //saat tombol waktu ditekan
        alarmReceiver = AlarmReceiver()  //inisialisasi alarmReceiver
        btn_time.visibility = View.GONE
        btn_time.isEnabled = false;
        btn_time.setTextColor(BLACK)
        btn_time.setText("") //mengatur teks waktu mula mula.
    }

    override fun onClick(v: View) {
        //saat tombol switch di klik
        if(v.id == R.id.swNotif) {
            if(swNotif.isChecked) {
                val now = Calendar.getInstance() //mengambil waktu sekarang
                val jam = now.get(Calendar.HOUR) //mengambil jam sekarang
                val menit = now.get(Calendar.MINUTE) //mengambil menit sekarang

                //untuk mengatur waktu yang akan digunakan pada notifikasi.
                var myTimePicker = TimePickerDialog(this,
                    TimePickerDialog
                        .OnTimeSetListener { datePicker, hour, minutes->
                            run {
                                btn_time.text = hour.toString().padStart(2, '0') +
                                        ":" + minutes.toString().padStart(2, '0')
                                swNotif.text =
                                    "Pengingat Di Atur Di Jam " + btn_time.text //mengatur teks ketika ditekan oke pada picker
                                val repeatTime =
                                    btn_time.text.toString()  //menyimpan waktu dalam string
                                alarmReceiver.setRepeatingAlarm(
                                    this,
                                    repeatTime
                                ) //Melakukan pemanggilan Receiver untuk Melakukan setting waktu
                                //pemanggilan notifikasi
                                btn_time.visibility = View.VISIBLE
                            }
                        },jam,menit,true)

                //Ketika Ditekan Cancel pada Time Picker Dialog maka switch akan dikembalikan seperti semula.
                myTimePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",object :DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(this@SettingNotificationActivity, "Cancel", Toast.LENGTH_LONG).show()
                        //Mengembalikan switch ke semula atau kembali off.
                        swNotif.isChecked = false;
                    }
                })
                //Menampilkan Time Picker Dialog
                myTimePicker.show()
            }
            else {
                btn_time.visibility = View.GONE
                swNotif.text = "Pengingat Tidak Aktif" //ketika switch mati, teks berganti.
                alarmReceiver.cancelAlarm(this) //melakukan cancel pada receiver, agar tidak terjadi
                                                        // tejadi pemanggilan notifikasi lagi.
            }
        }
    }
}