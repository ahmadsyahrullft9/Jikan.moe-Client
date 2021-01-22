package com.example.databindingdemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databindingdemo.adapters.GenericAdapter
import com.example.databindingdemo.adapters.TopJikanAdapter
import com.example.databindingdemo.binding.BaseActivityBinding
import com.example.databindingdemo.data.models.NetworkState
import com.example.databindingdemo.data.models.TopJikanType
import com.example.databindingdemo.data.models.Type
import com.example.databindingdemo.data.viewmodels.TopJikanViewModel
import com.example.databindingdemo.data.viewmodels.TopJikanViewModelFactory
import com.example.databindingdemo.databinding.ActivityMainBinding
import com.example.databindingdemo.fragments.FilterBottomSheetDialogFragment
import com.example.databindingdemo.viewholders.ErrorViewHolder
import com.example.databindingdemo.viewholders.ItemFilterViewHolder
import com.example.databindingdemo.viewholders.TabViewHolder

class MainActivity : BaseActivityBinding<ActivityMainBinding>(),
    FilterBottomSheetDialogFragment.Listener {

    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var topJikanViewModel: TopJikanViewModel
    lateinit var tabAdapter: GenericAdapter<TabViewHolder.TabItem, TabViewHolder>
    lateinit var errorAdapter: GenericAdapter<ErrorViewHolder.ErrorItemMessage, ErrorViewHolder>
    lateinit var topJikanAdapter: TopJikanAdapter

    private var topJikanType = TopJikanType(Type.ANIME)
    var subTypeIndex = 0
    val types = arrayOf(Type.ANIME, Type.MANGA)

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {
        //setup data
        setup_topjikan()

        //setup tab adapter
        tabAdapter = createTabAdapter()
        setup_tab()

        //setup data
        setup_data()

        //setup error adapter
        setup_errorview()

        //set action
        setup_filter_action()
    }

    private fun setup_filter_action() {
        binding.viewType.setOnClickListener {
            /*val adaper = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, types)
            val builder = AlertDialog.Builder(this@MainActivity)
                .setAdapter(adaper) { dialog, which ->
                    dialog.dismiss()
                    if (topJikanType.type != types[which]) {
                        subTypeIndex = 0
                        topJikanType = TopJikanType(types[which])
                        setup_tab()
                        changeConfig()
                    }
                }.setTitle("Choose Content")
            val alertDialog = builder.create()
            alertDialog.show()*/

            val topJikanTypes = ArrayList<TopJikanType>()
            types.forEach {
                topJikanTypes.add(TopJikanType(it))
            }
            val filterDialog =
                FilterBottomSheetDialogFragment.newInstance(topJikanTypes, topJikanType)
            filterDialog.show(supportFragmentManager, FilterBottomSheetDialogFragment.TAG)
        }
    }

    override fun select(itemFilter: ItemFilterViewHolder.ItemFilter) {
        if (topJikanType.type != itemFilter.topJikanType.type) {
            subTypeIndex = 0
            topJikanType = itemFilter.topJikanType
            setup_tab()
            changeConfig()
        }
    }

    private fun setup_errorview() {
        errorAdapter = object : GenericAdapter<ErrorViewHolder.ErrorItemMessage, ErrorViewHolder>(
            R.layout.item_error,
            ErrorViewHolder::class.java,
            ErrorViewHolder.ErrorItemMessage::class.java,
            ArrayList()
        ) {
            override fun bindView(
                holder: ErrorViewHolder,
                model: ErrorViewHolder.ErrorItemMessage,
                position: Int
            ) {
                holder.onbind(model)
            }
        }
    }

    private fun setup_data() {
        topJikanViewModel = createTopJikanViewModel()
        topJikanViewModel.networkState.observe(this@MainActivity, {
            Log.d(TAG, "setup_data: ${it.message}")
            Log.d(TAG, "setup_data: ${topJikanAdapter.itemCount}")
            if (!topJikanViewModel.listIsEmpty()) {
                topJikanAdapter.setNetworkState(it)
            } else {
                //add view error
                //Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_LONG).show()
                if ((it == NetworkState.ERROR || it == NetworkState.ENDOFLIST) && topJikanAdapter.itemCount <= 0) {
                    val errorItemMessage = ErrorViewHolder.ErrorItemMessage(
                        iconError = if (it == NetworkState.ENDOFLIST) R.drawable.ic_undraw_void_3ggu else R.drawable.ic_undraw_fixing_bugs_w7gi,
                        titleError = if (it == NetworkState.ENDOFLIST) "O Data Result !" else "An Error Found",
                        messageError = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras et posuere metus. Maecenas viverra sem at est vestibulum, ut vulputate orci tincidunt. Sed bibendum diam sed facilisis accumsan. Nulla pharetra orci metus, at tempor libero auctor quis. Ut odio metus, convallis vel enim sed, malesuada imperdiet mauris. Donec quis posuere dolor. Mauris congue eget sem quis pretium. Praesent lobortis tellus vel justo fermentum finibus. Donec interdum dui eu dui tempus, sed eleifend tortor tempor. Suspendisse at massa facilisis, rhoncus odio feugiat, varius lectus. Mauris tincidunt, lorem non maximus tincidunt, lorem libero mattis neque, eu consectetur arcu dolor eu magna. Fusce dolor mi, placerat vel tempus ut, vestibulum ac dui. Mauris lobortis neque at purus eleifend, in tempus sapien venenatis. Etiam eu imperdiet ipsum. Sed ac bibendum magna, lobortis tempor erat. Donec euismod scelerisque arcu, quis molestie sapien commodo nec. "
                    )
                    errorAdapter.setDataBatch(arrayListOf(errorItemMessage))
                    binding.rvData.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        setHasFixedSize(true)
                        adapter = errorAdapter
                    }
                }
            }
        })
        topJikanViewModel.topJikanPagedList.observe(this@MainActivity, {
            topJikanAdapter.submitList(it)
        })
    }

    private fun changeConfig() {
        setup_topjikan()
        setup_data()
    }

    private fun setup_topjikan() {
        val context: Context = this@MainActivity
        topJikanAdapter = TopJikanAdapter(context)

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = topJikanAdapter
        }
    }

    private fun createTopJikanViewModel(): TopJikanViewModel {
        return TopJikanViewModelFactory(
            topJikanType.type,
            topJikanType.getSubType(subTypeIndex)
        ).create(TopJikanViewModel::class.java)
    }

    private fun createTabAdapter(): GenericAdapter<TabViewHolder.TabItem, TabViewHolder> {
        return object : GenericAdapter<TabViewHolder.TabItem, TabViewHolder>(
            R.layout.item_tab,
            TabViewHolder::class.java,
            TabViewHolder.TabItem::class.java,
            ArrayList()
        ) {
            override fun bindView(
                holder: TabViewHolder,
                model: TabViewHolder.TabItem,
                position: Int
            ) {
                holder.onBindItem(model)
                holder.itemView.setOnClickListener {
                    Log.d("itemclicked", holder.adapterPosition.toString())
                    val lastIndex = subTypeIndex
                    subTypeIndex = holder.adapterPosition
                    tabAdapter.apply {
                        mData[lastIndex].selected = false
                        mData[subTypeIndex].selected = true
                        notifyDataSetChanged()
                    }
                    setup_data()
                }
            }
        }
    }

    private fun setup_tab() {
        binding.txtType.text = topJikanType.type
        val context: Context = this@MainActivity
        val dataitems = ArrayList<TabViewHolder.TabItem>()
        topJikanType.getSubTypeList().forEach { dataitems.add(TabViewHolder.TabItem(it, false)) }
        dataitems[subTypeIndex].selected = true
        tabAdapter.setDataBatch(dataitems)
        binding.rvTab.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = tabAdapter
        }
    }
}