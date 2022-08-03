package com.example.bulletinboard.database

import android.util.Log
import com.example.bulletinboard.data.Ad
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager(val readDataCallback : ReadDataCallback?) {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(ad : Ad){
       if (auth.uid != null) db.child(ad.key ?: "empty").child(auth.uid!!).child("ad").setValue(ad)
    }

    fun readDataFromDb(){
         val adArray = ArrayList<Ad>()
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children){
                    val ad = item.children.iterator().next().child("ad").getValue(Ad ::class.java)
                    if (ad != null) adArray.add(ad)
                }
                readDataCallback?.readData(adArray)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}