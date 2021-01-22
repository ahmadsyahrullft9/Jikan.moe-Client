package com.example.databindingdemo.data.models

import java.io.Serializable

class TopJikanType(var type: String) : Serializable {

    fun getSubType(i: Int): String {
        return getSubTypeList()[i]
    }

    fun getSubTypeList(): java.util.ArrayList<String> {
        when (type) {
            Type.ANIME -> {
                return arrayListOf(
                    Subtype.ANIME_AIRING,
                    Subtype.ANIME_MOVIE,
                    Subtype.ANIME_OVA,
                    Subtype.ANIME_SPECIAL,
                    Subtype.ANIME_TV,
                    Subtype.ANIME_UPCOMING
                )
            }
            Type.MANGA -> {
                return arrayListOf(
                    Subtype.MANGA_DOUJIN,
                    Subtype.MANGA_MANGA,
                    Subtype.MANGA_MANHUA,
                    Subtype.MANGA_MANHWA,
                    Subtype.MANGA_NOVELS,
                    Subtype.MANGA_ONESHOTS
                )
            }
            else -> {
                return arrayListOf(
                    Subtype.BOTH_BYPOPULARITY, Subtype.BOTH_FAVORITE
                )
            }
        }
    }
}