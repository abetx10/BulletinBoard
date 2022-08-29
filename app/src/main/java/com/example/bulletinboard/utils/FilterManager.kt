package com.example.bulletinboard.utils

import com.example.bulletinboard.model.Ad
import com.example.bulletinboard.model.AdFilter

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
}