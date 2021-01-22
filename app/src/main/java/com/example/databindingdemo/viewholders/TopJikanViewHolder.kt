package com.example.databindingdemo.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.databindingdemo.data.models.TopJikan
import com.example.databindingdemo.databinding.ItemJikanBinding

class TopJikanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun onBind(topJikan: TopJikan) {
        ItemJikanBinding.bind(itemView).apply {
            Glide.with(itemView)
                .asBitmap()
                .load(topJikan.imageUrl)
                .into(imageJikan)
            txtTitleJikan.text = topJikan.title
            txtTypeJikan.text = "type : ${topJikan.type}"
            txtStartdateJikan.text = "start at : ${topJikan.startDate}"
        }
    }
}