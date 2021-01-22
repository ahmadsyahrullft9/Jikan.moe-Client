package com.example.databindingdemo.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.databindingdemo.data.datasources.TopAnimeDataSource
import com.example.databindingdemo.data.datasources.TopAnimeDataSourceFactory
import com.example.databindingdemo.data.models.*
import io.reactivex.disposables.CompositeDisposable

class TopJikanRepo(
    var type: String,
    var subType: String,
    private val compositeDisposable: CompositeDisposable
) {

    lateinit var topJikanPagedList: LiveData<PagedList<TopJikan>>
    var topAnimeDataSourceFactory: TopAnimeDataSourceFactory =
        TopAnimeDataSourceFactory(type, subType, compositeDisposable)

    fun fetchLiveTopAnimePagedList(): LiveData<PagedList<TopJikan>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Conf.POST_PER_PAGE)
            .build()
        topJikanPagedList = LivePagedListBuilder(topAnimeDataSourceFactory, config).build()
        return topJikanPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TopAnimeDataSource, NetworkState>(
            topAnimeDataSourceFactory.topAnimeDataSource,
            TopAnimeDataSource::networkState
        )
    }
}