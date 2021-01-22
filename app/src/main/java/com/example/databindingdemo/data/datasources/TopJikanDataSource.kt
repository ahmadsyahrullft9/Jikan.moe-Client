package com.example.databindingdemo.data.datasources

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.databindingdemo.data.apis.ApiClient
import com.example.databindingdemo.data.apis.ApiInterface
import com.example.databindingdemo.data.models.Conf
import com.example.databindingdemo.data.models.NetworkState
import com.example.databindingdemo.data.models.TopJikan
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TopAnimeDataSourceFactory(
    private val type: String,
    private val subType: String,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, TopJikan>() {

    val topAnimeDataSource = MutableLiveData<TopAnimeDataSource>()

    override fun create(): DataSource<Int, TopJikan> {
        val dataSource = TopAnimeDataSource(type, subType, compositeDisposable)
        topAnimeDataSource.postValue(dataSource)
        return dataSource
    }
}

class TopAnimeDataSource(
    private val type: String,
    private val subType: String,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, TopJikan>() {

    companion object {
        const val TAG = "TopAnimeDataSource"
    }

    private val apiInterface: ApiInterface = ApiClient.getClien()
    private var page = Conf.FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, TopJikan>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiInterface.getTop(type, subType, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.topJikan.isNotEmpty()) {
                            callback.onResult(it.topJikan, null, page + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e(TAG, it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TopJikan>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TopJikan>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiInterface.getTop(type, subType, params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.topJikan.isNotEmpty()) {
                            callback.onResult(it.topJikan, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e(TAG, it.message.toString())
                    }
                )
        )
    }

}