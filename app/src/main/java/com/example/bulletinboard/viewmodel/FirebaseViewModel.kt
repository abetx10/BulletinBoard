package com.example.bulletinboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulletinboard.model.Ad
import com.example.bulletinboard.model.DbManager

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>>()
    fun loadAllAds(){
        dbManager.getAllAds(object : DbManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })

    }
    fun adViewed(ad: Ad){
        dbManager.adViewed(ad)
    }

    fun loadMyAds(){
        dbManager.getMyAds(object : DbManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })

    }

    fun deleteItem(ad: Ad){
        dbManager.deleteAdd(ad, object : DbManager.FinishWorkListener{
            override fun onFinish() {
                val updatedList = liveAdsData.value
                updatedList?.remove(ad)
                liveAdsData.postValue(updatedList!!)
            }
        })
    }

}