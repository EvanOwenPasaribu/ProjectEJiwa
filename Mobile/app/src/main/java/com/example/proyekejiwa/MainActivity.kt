package com.example.proyekejiwa

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.proyekejiwa.broadcastnservice.ConnectivityReceiver
import com.example.proyekejiwa.broadcastnservice.TryConnectService
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_LOADING_STATUS ="status"
const val EXTRA_FINISH = "finish"
const val EXTRA_PERSEN = "persen"
class MainActivity : AppCompatActivity(), View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {


    private val loadingReceiver= object : BroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            var selesai= p1?.getBooleanExtra(EXTRA_FINISH,false)
            var persenSelesai= p1?.getIntExtra(EXTRA_PERSEN,0)

            //mengupdate value dari progress bar
            myProgerss.progress= persenSelesai?: 0
            //check apakah sudah mencapai 100, jika sudah mencapai 100 tidak ada pengubahan koneksi internet,
            // maka keluar dari aplikasi.
            if(selesai!!){
                Toast.makeText(this@MainActivity,"Keluar Dari Halaman", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //deklarasi semua variabel button navigation menu
        val homeMenu = findViewById<View>(R.id.home_menu)
        val serviceMenu = findViewById<View>(R.id.service_menu)
        val profileMenu = findViewById<View>(R.id.profil_menu)
        //deklarasi varibel untuk agar support fragment manager
        val mFragmentManager = supportFragmentManager
        val mhomeFragment = homeFragment()
        val fragment = mFragmentManager.findFragmentByTag(homeFragment::class.java.simpleName)

        //pengecekan apakah saat proses oncreate pada activity main fragment yang terbuka adalah fragment home
        if(fragment !is homeFragment){
            Log.d("MyFlexibleFRagment", "Fragment Name : " + homeFragment::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mhomeFragment, homeFragment::class.java.simpleName)
                .commit()
        }
        //Melakukan Register Receciver
        val filterReceiver= IntentFilter(EXTRA_LOADING_STATUS)
        registerReceiver(loadingReceiver,filterReceiver)
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        //penggunaan fungsi setOnClickListener pada masing masing menu button navigation
        homeMenu.setOnClickListener(this)
        serviceMenu.setOnClickListener(this)
        profileMenu.setOnClickListener(this)
    }

    //fungsi onclick pada menu buttom navigation
    override fun onClick(v: View) {
        //Saat Salah Satu Menu Diklik, Semua Proses Dilakukan di Back-end
        Thread(Runnable {
            //Pengecekan jika tombol yang diklik menu home
            if(v.id == R.id.home_menu) {
                //deklarasi varibel untuk agar support fragment manager
                val mFragmentManager = supportFragmentManager
                val mhomeFragment = homeFragment()
                val fragment = mFragmentManager.findFragmentByTag(homeFragment::class.java.simpleName)
                //pengecekan apakah fragment yang terbuka bukan fragment Home
                if(fragment !is homeFragment){
                    //jika bukan maka fragment manager akan menggantikan fragment yang terbuka dengan fragment home
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_container, mhomeFragment, homeFragment::class.java.simpleName)
                        .commit()
                }
            //Pengecekan jika tombol yang diklik menu layanan
            }else if(v.id == R.id.service_menu) {
                //deklarasi varibel untuk agar support fragment manager
                val mFragmentManager = supportFragmentManager
                val mServiceFragment = layananFragment()
                val fragment = mFragmentManager.findFragmentByTag(layananFragment::class.java.simpleName)
                //pengecekan apakah fragment yang terbuka bukan fragment Layanan
                if(fragment !is layananFragment){
                    //jika bukan maka fragment manager akan menggantikan fragment yang terbuka dengan fragment home
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_container, mServiceFragment, layananFragment::class.java.simpleName)
                        .commit()
                }
            //Pengecekan jika tombol yang diklik menu profil
            }else if(v.id == R.id.profil_menu) {
                //deklarasi varibel untuk agar support fragment manager
                val mFragmentManager = supportFragmentManager
                val mProfileFragment = profileFragment()
                val fragment = mFragmentManager.findFragmentByTag(profileFragment::class.java.simpleName)
                //pengecekan apakah fragment yang terbuka bukan fragment Profil
                if(fragment !is profileFragment){
                    //jika bukan maka fragment manager akan menggantikan fragment yang terbuka dengan fragment home
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_container, mProfileFragment, profileFragment::class.java.simpleName)
                        .commit()
                }
            }
        }).start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingNotif) {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    //fungsi show detail dari masing-masing fitur di menu home
    fun showDetail(v : View)
    {
        //Proses Saat Layanan Di Klik, Proses Dilakukan Di Back-End
        Thread(Runnable {
            //deklarasi variabel-variabel yang akan dikirim
            var name : String? = null
            var deskripsi : String? = null
            var telepon : String? = null
            var photo : Int = 0
            //proses pengecekan apa data yang akan disimpan didalam variabel untuk dikirim nantinya kefragment detail
            if(v.id == R.id.btn_konsultasi) {
                name = "Konsultasi" as String
                deskripsi = "User Bila Menggunakan Layanan Konsultasi mengenai keadaan Psikologi yang dideritanya" as String
                telepon = "082277296928" as String
                photo = R.drawable.konsultasi
            }else if(v.id == R.id.btn_asuransi) {
                name = "Asuransi"
                deskripsi = "User Bila Bisa Mengurus Asuransi"
                telepon = "082277296958"
                photo = R.drawable.asuransi
            }else if(v.id == R.id.btn_hubungi) {
                name = "Hubungi Dokter"
                deskripsi = "User Melakukan pertemuan dengan dokter"
                telepon = "082277296918"
                photo = R.drawable.bicaradengandokter
            }else if(v.id == R.id.btn_beliObat) {
                name = "Beli Obat"
                deskripsi = "User Melakukan transaksi Pembelian Obat"
                telepon = "082277596918"
                photo = R.drawable.beliobat
            }

            //deklarasi varibel untuk agar support fragment manager
            val mFragmentManager = supportFragmentManager
            val mDetailFragment = DetailLayananFragment()
            val mBundle = Bundle()

            //proses pengiriman data dari variabel-variabel menggunakan kata kunci EXTRA
            //memasukkan data dari varibel ke kata kunci EXTRA
            mBundle.putString(DetailLayananFragment.EXTRA_NAME, name)
            mBundle.putString(DetailLayananFragment.EXTRA_DESCRIPTION,deskripsi)
            mBundle.putString(DetailLayananFragment.EXTRA_TELEPON, telepon)
            mBundle.putInt(DetailLayananFragment.EXTRA_PHOTO, photo)

            //menampung data object berupa bundle
            mDetailFragment.arguments = mBundle
            //deklarasi fragment detail layanan
            val fragment = mFragmentManager.findFragmentByTag(DetailLayananFragment::class.java.simpleName)
            //proses pengecekan apakah fragment yang terbuka merupakan fragment detail layanan
            if(fragment !is DetailLayananFragment){
                //jika tidak maka fragment manager akan mengantikan fragment home dengan fragment detail layanan
                mFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, mDetailFragment, DetailLayananFragment::class.java.simpleName)
                    .commit()
            }
        }).start()
    }

    //Semua untuk proses connect ke Internet
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }
    private fun showNetworkMessage(isConnected: Boolean) {
        val mLoadingService= Intent (this, TryConnectService::class.java)
        //jika tidak ada koneksi maka memunculkan progress bar, untuk menunggu pengubahan koneksi.
        if (!isConnected) {
            llEConnect.visibility = View.VISIBLE  //Saat tidak ada koneksi progress bar akan muncul
            allContainer.visibility = View.GONE  //saat tidak ada koneksi Container untuk menampung semua fragement akan hidden
            bottomNavigationMenu.visibility = View.GONE  //bottom navigation juga akan terhidden
            myProgerss.progress=0 //inisial progress bar akan kembali 0 jika tidak ada koneksi
            startService(mLoadingService) //memulai service
        } else {
            llEConnect.visibility = View.GONE //Saat koneksi internet ada maka progress bar akan terhidden
            allContainer.visibility = View.VISIBLE //saat koneksi internet ada Container untuk menampung semua fragement akan muncul
            bottomNavigationMenu.visibility = View.VISIBLE //bottom navigation juga akan muncul
            myProgerss.progress=0 ////inisial progress bar akan kembali 0 jika tidak ada koneksi
            stopService(mLoadingService) //menghentikan service ketika koneksi internet ada.
        }
    }
}
