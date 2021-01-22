package com.example.databindingdemo.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.databindingdemo.databinding.ItemErrorBinding

class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onbind(errorItemMessage: ErrorItemMessage) {
        ItemErrorBinding.bind(itemView).apply {
            icError.setImageResource(errorItemMessage.iconError)
            txtTitleMessageError.text = errorItemMessage.titleError
            txtDescMessageError.text = errorItemMessage.messageError
        }
    }

    data class ErrorItemMessage(
        val iconError: Int,
        val titleError: String,
        val messageError: String
    )
}