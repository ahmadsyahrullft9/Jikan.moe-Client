package com.example.databindingdemo.data.apis

import com.example.databindingdemo.data.models.TopJikanResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("top/{type}/{page}/{subtype}")
    fun getTop(
        @Path("type") type: String,
        @Path("subtype") subtype: String,
        @Path("page") page: Int
    ): Single<TopJikanResponse>
}