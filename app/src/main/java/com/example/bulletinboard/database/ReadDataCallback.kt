package com.example.bulletinboard.database

import com.example.bulletinboard.data.Ad

interface ReadDataCallback {
    fun readData(list: List<Ad>)
}