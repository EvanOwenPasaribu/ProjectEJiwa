package com.example.proyekejiwa

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_detail_layanan.*

class DetailLayananFragment : Fragment(), View.OnClickListener {
    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_TELEPON = "telepon"
        const val EXTRA_PHOTO = "photo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_layanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mengecek apakah ada argumen yang dikirim dari fragment home
        if(arguments != null) {
            //Mengambil Argument Berdasarkan kata kunci
            tvJudulLayanan.text = arguments?.getString(EXTRA_NAME)
            tvDetailLayanan.text = arguments?.getString(EXTRA_DESCRIPTION)
            tvNumber.text = arguments?.getString(EXTRA_TELEPON)
            //Meletakkan gambar menggunakan Library Glide
            Glide.with(this)
                .load(arguments?.getInt(EXTRA_PHOTO))
                .apply(RequestOptions().override(350, 550))
                .into(imgLayanan)
        }
        //Nomor Telepon Saat DiKlik
        tvNumber.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.tvNumber) {
            dial()
        }
    }

    //metode ini merupakan Intent Implisit, untuk melakukan Dial Nomor Telepon.
    fun dial() {
        //Proses dilakukan di backEnd
        Thread(Runnable {
            var number = tvNumber.text
            val dialPhoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(dialPhoneIntent)
        }).start()
    }
}
