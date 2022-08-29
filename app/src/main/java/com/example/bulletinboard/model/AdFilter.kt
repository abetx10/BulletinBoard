package com.example.bulletinboard.model

data class AdFilter(
    val time: String? = null,
    val cat_time: String? = null,

//Filter with category
    val cat_country_time: String? = null,
    val cat_country_city_time: String? = null,
    val cat_city_time: String? = null,

//Filter without category
    val country_time: String? = null,
    val country_city_time: String? = null,
    val city_time: String? = null
)
