package com.example.databindingdemo.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingdemo.data.models.NetworkState
import com.example.databindingdemo.databinding.ItemNetworkBinding

class NetworkStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(networkState: NetworkState?) {
        ItemNetworkBinding.bind(itemView).apply {
            progressBar.visibility =
                if (networkState != null && networkState == NetworkState.LOADING) View.VISIBLE else View.GONE
            txtError.visibility =
                if (networkState == null || (networkState == NetworkState.ERROR || networkState == NetworkState.ENDOFLIST)) View.VISIBLE else View.GONE
            val message = networkState?.message ?: "network state is null"
            txtError.text = message
        }
    }
}