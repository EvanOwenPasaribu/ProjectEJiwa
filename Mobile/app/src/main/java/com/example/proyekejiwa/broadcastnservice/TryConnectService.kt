package com.example.proyekejiwa.broadcastnservice

import android.app.IntentService
import android.content.Intent
import com.example.proyekejiwa.EXTRA_FINISH
import com.example.proyekejiwa.EXTRA_LOADING_STATUS
import com.example.proyekejiwa.EXTRA_PERSEN

private var status = 0
class TryConnectService : IntentService("TryConnectService") {
    override fun onHandleIntent(intent: Intent?) {

        //inisialisasi variabel
        status = 0
        var loadingPersentage= 0
        do{
            //Melakukan penambahan Berselang waktu 3 Detik
            Thread.sleep(3000L)
            //setiap 3 detik terjadi penambahan pada loadingPersentase
            loadingPersentage+=10;
            var intentLoadingFinish= Intent(EXTRA_LOADING_STATUS)
            //meletakkan semua data yang akan dikirim
            intentLoadingFinish.putExtra(EXTRA_PERSEN,loadingPersentage)
            intentLoadingFinish.putExtra(EXTRA_FINISH,false)
            //jika sudah mencapai 100 proses loading berhenti
            if(loadingPersentage==100)
                intentLoadingFinish.putExtra(EXTRA_FINISH,true)
            //melakukan pengiriman Broadcast
            sendBroadcast(intentLoadingFinish)
        }while (loadingPersentage<100 && status ==0)
    }

    //Ketika Loading selesai maka akan langsung diDestroy
    override fun onDestroy() {
        super.onDestroy()
        status = 1
    }
}
