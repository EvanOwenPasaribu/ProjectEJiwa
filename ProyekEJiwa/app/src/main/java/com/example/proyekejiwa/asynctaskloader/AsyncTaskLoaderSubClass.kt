package com.example.proyekejiwa.asynctaskloader

import android.content.Context
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import kotlin.math.roundToInt

class AsyncTaskLoaderSubClass
    (context: Context, val args: Bundle?, val progressInterface: BackgroundProgressInterface)
    : AsyncTaskLoader<ArrayList<Pasien>>(context) { //menggunakan parameter array agar returnya juga array list.

    //inisialisasi variabel penampung data.
    var namaPasien = arrayListOf<String>()
    var jkPasien = arrayListOf<String>()
    var umurPasien = arrayListOf<Int>()
    var alamatPasien = arrayListOf<String>()
    var telpPasien = arrayListOf<String>()

    //Permulaan Loading.
    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    //Proses asyncronous.
    override fun loadInBackground(): ArrayList<Pasien> {
        //melakukan penambahan data pada array penampung data.
        namaPasien.addAll(DataPasien.namaPasien)
        jkPasien.addAll(DataPasien.jenisKelaminPasien)
        umurPasien.addAll(DataPasien.umurPasien)
        alamatPasien.addAll(DataPasien.alamatPasien)
        telpPasien.addAll(DataPasien.teleponPasien)

        //inisialisasi array penampung semua data.
        val list = arrayListOf<Pasien>()
        //lakukan perulangan agar data disimpan di satu array saja.
        for (position in DataPasien.namaPasien.indices) {
            val pasien = Pasien(
                DataPasien.namaPasien[position],
                DataPasien.jenisKelaminPasien[position],
                DataPasien.umurPasien[position],
                DataPasien.alamatPasien[position],
                DataPasien.teleponPasien[position]
            )
            //penambahan pada array penampung semua data.
            list.add(pasien)
            //karena data hanya sedikit, proses tidak akan keliahatan
            //sehingga dibuat Thread,sleep.
            Thread.sleep(300L)
            //melakukan update pada Interface
            progressInterface.onUpdateProgress(((position/namaPasien.count().toFloat())*100).roundToInt())
        }
        return list  //result semua data.
    }
}