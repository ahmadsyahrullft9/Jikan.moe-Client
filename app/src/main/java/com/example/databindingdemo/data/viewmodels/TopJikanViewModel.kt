package com.example.databindingdemo.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.example.databindingdemo.data.models.NetworkState
import com.example.databindingdemo.data.models.TopJikan
import com.example.databindingdemo.data.repos.TopJikanRepo
import io.reactivex.disposables.CompositeDisposable

class TopJikanViewModelFactory(var type: String, var subType: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopJikanViewModel(type, subType) as T
    }
}

class TopJikanViewModel(var type: String, var subType: String) : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var topJikanRepo: TopJikanRepo

    init {
        topJikanRepo = TopJikanRepo(type, subType, compositeDisposable)
    }

    val topJikanPagedList: LiveData<PagedList<TopJikan>> by lazy {
        topJikanRepo.fetchLiveTopAnimePagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        topJikanRepo.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return topJikanRepo.topJikanPagedList.value?.isEmpty() ?: true
    }

    fun clearRequest() {
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}