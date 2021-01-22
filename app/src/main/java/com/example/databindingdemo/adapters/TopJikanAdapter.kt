package com.example.databindingdemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingdemo.R
import com.example.databindingdemo.data.models.NetworkState
import com.example.databindingdemo.data.models.TopJikan
import com.example.databindingdemo.viewholders.NetworkStateViewHolder
import com.example.databindingdemo.viewholders.TopJikanViewHolder

class TopJikanAdapter(val context: Context) :
    PagedListAdapter<TopJikan, RecyclerView.ViewHolder>(TopJikanDiffCallback()) {

    val ITEM_TYPE = 1
    val NETWORK_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE -> {
                val view = layoutInflater.inflate(R.layout.item_jikan, parent, false)
                TopJikanViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.item_network, parent, false)
                NetworkStateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == ITEM_TYPE)(holder as TopJikanViewHolder).onBind(getItem(position)!!)
        else (holder as NetworkStateViewHolder).onBind(networkState)
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_TYPE
        } else {
            ITEM_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val prevNetworkState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && prevNetworkState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class TopJikanDiffCallback : DiffUtil.ItemCallback<TopJikan>() {
        override fun areItemsTheSame(oldItem: TopJikan, newItem: TopJikan): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: TopJikan, newItem: TopJikan): Boolean {
            TODO("Not yet implemented")
        }

    }
}