package com.example.bulletinboard.model

import java.io.Serializable

data class Ad(
    val country: String? = null,
    val city: String? = null,
    val phone: String? = null,
    val category: String? = null,
    val title: String? = null,
    val price: String? = null,
    val description: String? = null,
    val key: String? = null,
    val uid: String? = null,

    var viewsCounter: String = "0",
    var emailsCounter: String = "0",
    var callsCounter: String = "0"
) : Serializable