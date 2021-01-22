package com.example.databindingdemo.data.models

enum class Status {
    RUNNING, SUCCESS, FAILED
}

class NetworkState(val status: Status, val message: String) {

    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
            ENDOFLIST = NetworkState(Status.FAILED, "End of list")
        }
    }
}