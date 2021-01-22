package com.example.databindingdemo.data.models


import com.google.gson.annotations.SerializedName

data class TopJikanResponse(
    @SerializedName("request_cache_expiry")
    val requestCacheExpiry: Int,
    @SerializedName("request_cached")
    val requestCached: Boolean,
    @SerializedName("request_hash")
    val requestHash: String,
    @SerializedName("top")
    val topJikan: List<TopJikan>
)