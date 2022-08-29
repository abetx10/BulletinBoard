package com.example.bulletinboard.utils

import com.example.bulletinboard.model.Ad
import com.example.bulletinboard.model.AdFilter
import java.lang.StringBuilder

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
        val sBuilder = StringBuilder()
        val tempArray = filter.split("_")
        if(tempArray[0] != "empty") sBuilder.append("country_")
        if(tempArray[1] != "empty") sBuilder.append("city_")
        sBuilder.append("time")
        return sBuilder.toString()
    }
}