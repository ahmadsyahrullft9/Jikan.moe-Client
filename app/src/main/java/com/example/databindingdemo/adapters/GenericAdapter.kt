package com.example.databindingdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

abstract class GenericAdapter<TipeData, ViewHolder : RecyclerView.ViewHolder>(
    protected var mLayout: Int,
    var mViewHolderClass: Class<ViewHolder>,
    var mModelClass: Class<TipeData>,
    var mData: ArrayList<TipeData>
) :
    RecyclerView.Adapter<ViewHolder>() {

    fun addData(data: TipeData) {
        mData.add(data)
        notifyItemInserted(itemCount - 1)
    }

    fun addDataBatch(dataList: List<TipeData>) {
        mData.addAll(dataList)
        notifyItemRangeInserted(itemCount - 1, dataList.size)
    }

    fun setDataBatch(dataList: List<TipeData>) {
        mData = dataList as ArrayList<TipeData>
        notifyDataSetChanged()
    }

    fun insertData(position: Int, data: TipeData) {
        mData.add(position, data)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, mData.size)
    }

    fun removeData(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mData.size)
    }

    fun removeAll() {
        mData.removeAll(mData)
        notifyDataSetChanged()
    }

    fun getmData(): List<TipeData> {
        return mData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mLayout, parent, false) as ViewGroup
        return try {
            val constructor: Constructor<ViewHolder> =
                mViewHolderClass.getConstructor(View::class.java)
            constructor.newInstance(view)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        bindView(holder, model, position)
    }

    protected abstract fun bindView(holder: ViewHolder, model: TipeData, position: Int)

    private fun getItem(position: Int): TipeData {
        return mData[position]
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}

