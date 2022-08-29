package com.example.bulletinboard.utils

import com.example.bulletinboard.model.Ad
import com.example.bulletinboard.model.AdFilter
import kotlin.text.StringBuilder

object FilterManager {
    fun createFilter(ad: Ad): AdFilter {
        return AdFilter(
            ad.time,
            "${ad.category}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.city}_${ad.time}",
            "${ad.category}_${ad.city}_${ad.time}",

            "${ad.country}_${ad.time}",
            "${ad.country}_${ad.city}_${ad.time}",
            "${ad.city}_${ad.time}"

        )
    }

    fun getFilter(filter: String): String{
        val sBuilderNode = StringBuilder()
        val sBuilderFilter = StringBuilder()

        val tempArray = filter.split("_")
        if(tempArray[0] != "empty") {
            sBuilderNode.append("country_")
            sBuilderFilter.append("${tempArray[0]}_")
        }
        if(tempArray[1] != "empty") {
            sBuilderNode.append("city_")
            sBuilderFilter.append(tempArray[1])
        }
        sBuilderNode.append("time")
        return "$sBuilderNode|$sBuilderFilter"
    }
}