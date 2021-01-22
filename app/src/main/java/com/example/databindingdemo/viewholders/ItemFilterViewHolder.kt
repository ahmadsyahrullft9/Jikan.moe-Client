package com.example.databindingdemo.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingdemo.data.models.TopJikanType
import com.example.databindingdemo.databinding.ItemFilterBinding

class ItemFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onbind(itemFilter: ItemFilter) {
        ItemFilterBinding.bind(itemView).apply {
            txtTitleFilter.text = itemFilter.topJikanType.type
            icSelected.visibility = if (itemFilter.selected) View.VISIBLE else View.GONE
        }
    }

    data class ItemFilter(var topJikanType: TopJikanType, var selected: Boolean)
}