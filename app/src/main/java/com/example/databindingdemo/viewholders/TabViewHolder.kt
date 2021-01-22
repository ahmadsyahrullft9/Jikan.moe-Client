package com.example.databindingdemo.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingdemo.R
import com.example.databindingdemo.databinding.ItemTabBinding

class TabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBindItem(tabItem: TabItem) {
        ItemTabBinding.bind(itemView).apply {
            txtTabItem.text = tabItem.title
            txtTabItem.setTextColor(Color.parseColor(if (tabItem.selected) "#FFFFFFFF" else "#FF424242"))
            root.setBackgroundResource(if (tabItem.selected) R.drawable.bg_tab_item_selected else R.drawable.bg_tab_item)
        }
    }

    data class TabItem(
        var title: String,
        var selected: Boolean
    )
}