package com.example.databindingdemo.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databindingdemo.R
import com.example.databindingdemo.adapters.GenericAdapter
import com.example.databindingdemo.binding.BaseBottomSheetDialogFragmentBinding
import com.example.databindingdemo.data.models.TopJikanType
import com.example.databindingdemo.databinding.FragmentDialogFilterBinding
import com.example.databindingdemo.viewholders.ItemFilterViewHolder

class FilterBottomSheetDialogFragment :
    BaseBottomSheetDialogFragmentBinding<FragmentDialogFilterBinding>() {

    companion object {

        const val TAG = "FilterBottomSheetDialog"

        fun newInstance(
            topJikanTypes: ArrayList<TopJikanType>,
            selectedTopJikanType: TopJikanType
        ): FilterBottomSheetDialogFragment {
            val args = Bundle().apply {
                putSerializable("data", topJikanTypes)
                putSerializable("selected", selectedTopJikanType)
            }
            val fragment = FilterBottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    interface Listener {
        fun select(itemFilter: ItemFilterViewHolder.ItemFilter)
    }

    var topJikanTypes: ArrayList<TopJikanType>? = null
    var selectedTopJikanType: TopJikanType? = null
    lateinit var filterAdapter: GenericAdapter<ItemFilterViewHolder.ItemFilter, ItemFilterViewHolder>
    var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topJikanTypes = arguments?.getSerializable("data") as ArrayList<TopJikanType>
        selectedTopJikanType = arguments?.getSerializable("selected") as TopJikanType
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FilterBottomSheetDialogFragment.Listener")
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDialogFilterBinding
        get() = FragmentDialogFilterBinding::inflate

    override fun setupView(binding: FragmentDialogFilterBinding) {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            attributes.windowAnimations = android.R.style.Animation_Translucent
        }

        filterAdapter = object :
            GenericAdapter<ItemFilterViewHolder.ItemFilter, ItemFilterViewHolder>(
                R.layout.item_filter,
                ItemFilterViewHolder::class.java,
                ItemFilterViewHolder.ItemFilter::class.java,
                ArrayList()
            ) {
            override fun bindView(
                holder: ItemFilterViewHolder,
                model: ItemFilterViewHolder.ItemFilter,
                position: Int
            ) {
                holder.onbind(model)
                holder.itemView.setOnClickListener {
                    dismiss()
                    listener?.select(model)
                }
            }
        }
        binding.rvFilter.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = filterAdapter
        }

        val dataFilters = ArrayList<ItemFilterViewHolder.ItemFilter>()
        topJikanTypes?.forEach {
            dataFilters.add(
                ItemFilterViewHolder.ItemFilter(
                    it, it.type == selectedTopJikanType?.type
                )
            )
        }
        filterAdapter.setDataBatch(dataFilters)
    }
}