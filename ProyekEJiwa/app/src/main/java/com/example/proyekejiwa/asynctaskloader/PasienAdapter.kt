package com.example.proyekejiwa.asynctaskloader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekejiwa.R

class PasienAdapter(val listPasien: ArrayList<Pasien>) : RecyclerView.Adapter<PasienAdapter.GridViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GridViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_pasien, viewGroup, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.nama.text = listPasien[position].nama
        holder.jk.text = "Jenis Kelamin : " + listPasien[position].jenisKelamin
        holder.umur.text = "  Umur      : " + listPasien[position].umur.toString()
        holder.alamat.text = "Alamat     : " + listPasien[position].alamat
        holder.telp.text = "Telp          :  " +  listPasien[position].noHp
    }

    override fun getItemCount(): Int {
        return listPasien.size
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nama = itemView.findViewById<TextView>(R.id.tvName)
        var alamat = itemView.findViewById<TextView>(R.id.tvAlamat)
        var jk = itemView.findViewById<TextView>(R.id.tvJenisKelamin)
        var umur = itemView.findViewById<TextView>(R.id.tvUmur)
        var telp = itemView.findViewById<TextView>(R.id.tvTelp)
    }
}