package com.example.proyekejiwa.asynctaskloader

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekejiwa.R

@Suppress("DEPRECATION")


class PasienActivity : AppCompatActivity(), BackgroundProgressInterface
    ,LoaderManager.LoaderCallbacks<ArrayList<Pasien>> //Load manager dengan parameter
                                                    // ArrayList<Pasien> agar nanti menghasilkan sebuah list.
{
    val LOADER_ID = 25
    val bundleValue: String = "1,000"
    var progressTxtView: TextView? = null
    val bundle = Bundle()
    //inisialisasi Recycle View
    private lateinit var rviewPasien : RecyclerView

    companion object {
        val TAG = "PasienActivity"
        val BUNDLE_KEY = "key.to.identify.bundle.value"
    }

    //proses yang melakungan pengupdate pada progress bar. diambil dari Interface
    override fun onUpdateProgress(progress: Int) {
        runOnUiThread(Runnable {
            // This will run on the UI thread
            kotlin.run {
                progressTxtView?.setText("Progress = $progress%")  //update Text.
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasien)
        //buat di variabel agar terbaca di onUpdateProgress
        progressTxtView = findViewById(R.id.progress)
        //agar terlihat saat diulang kembali.
        progressTxtView?.visibility = View.VISIBLE
        bundle.putString(BUNDLE_KEY, bundleValue)  //meletakkan nilai bundle
        supportLoaderManager.initLoader(LOADER_ID, null, this) //init loader
        makeOperationAddNumber() //Memanggil fungsi agar proses AsyncTaskLoader berjalan.
    }

    //Pembuatan loader return hasil dari AsycTaskLoader
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Pasien>> {
        return AsyncTaskLoaderSubClass(this, args, this)
    }

    private fun makeOperationAddNumber() {
        // this will try to fetch a Loader with ID = LOADER_ID
        val loader: Loader<Long>? = supportLoaderManager.getLoader(LOADER_ID)
        if (loader == null) {
            /* jika Loader dengan loaderID tidak ditemukan,
             Inisialisasi Loader Baru dengan ID = LOADER_ID
             Masukkan bundel yang akan digunakan AsynTaskLoader
             Juga berikan callback yang diperlukan yaitu 'this' karena kami telah menerapkannya pada activity
             */
            supportLoaderManager.initLoader(LOADER_ID, bundle, this)
        } else {
            /* jika Loader dengan loaderID tidak ditemukan,
             Inisialisasi Loader Baru dengan ID = LOADER_ID
             Masukkan bundel yang akan digunakan AsynTaskLoader
             Juga berikan callback yang diperlukan yaitu 'this' karena kami telah menerapkannya pada activity
             */
            supportLoaderManager.restartLoader(LOADER_ID, bundle, this)
        }
    }
    //Saat proses pengambilan data selesai
    override fun onLoadFinished(loader: Loader<ArrayList<Pasien>>, data: ArrayList<Pasien>?) {
        //jika data kosong maka tidak mengeluarkan output apa apa.
        if (data == null || data.count() < 1) { return
        } else {
            //menyembunyikan progress bar.
            progressTxtView?.visibility = View.GONE
            //menghasilkan output dalam bentuk recyclerView.
            rviewPasien = findViewById(R.id.rvPasien)
            rviewPasien.setHasFixedSize(true)
            rviewPasien.layoutManager = LinearLayoutManager(this)
            //memasukkan data ke adapter sehingga menghasilkan output.
            var pasienAdapter = PasienAdapter(data)
            rviewPasien.adapter = pasienAdapter
        }
    }
    override fun onLoaderReset(loader: Loader<ArrayList<Pasien>>) {}
}