package com.example.bulletinboard.model


import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishWorkListener: FinishWorkListener) {
        if (auth.uid != null) db.child(ad.key ?: "empty")
            .child(auth.uid!!).child(AD_NODE)
            .setValue(ad).addOnCompleteListener {
                finishWorkListener.onFinish()
            }
    }

    fun adViewed(ad: Ad){
        var counter = ad.viewsCounter.toInt()
        counter++
        if (auth.uid != null) db.child(ad.key ?: "empty")
            .child(INFO_NODE).setValue(InfoItem(counter.toString(), ad.emailsCounter, ad.callsCounter))

    }

    fun getMyAds(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAds(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/price")
        readDataFromDb(query, readDataCallback)
    }

    fun deleteAdd(ad: Ad, listener: FinishWorkListener) {
        if (ad.key == null || ad.uid == null) return
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener.onFinish()
        }
    }


    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?) {
        val adArray = ArrayList<Ad>()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {

                    var ad: Ad? = null
                    item.children.forEach{
                        if (ad == null) ad = it.child(AD_NODE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.emailsCounter = infoItem?.emailsCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null) adArray.add(ad!!)
                }
                readDataCallback?.readData(adArray)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    interface ReadDataCallback {
        fun readData(list: ArrayList<Ad>)
    }

    interface FinishWorkListener{
        fun onFinish()
    }

    companion object{
        const val AD_NODE = "ad"
        const val MAIN_NODE = "main"
        const val INFO_NODE = "info"
    }

}