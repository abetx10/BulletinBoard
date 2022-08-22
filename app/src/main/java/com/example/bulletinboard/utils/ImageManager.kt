package com.example.bulletinboard.utils


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.example.bulletinboard.adapters.ImageAdapter
import com.example.bulletinboard.model.Ad
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.File
import java.io.InputStream
import kotlin.contracts.contract

object ImageManager {
    const val  MAX_IMAGE_SIZE = 1000

    fun getImageSize(uri: Uri, act : Activity): List<Int>{
        val inStream = act.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inStream,null,  options)
        return listOf(options.outWidth, options.outHeight)
    }


    fun chooseScaleType( im : ImageView, bitMap : Bitmap){
        if (bitMap.width > bitMap.height){
            im.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            im.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }

   suspend fun imageResize(uris: List<Uri>, act: Activity) : List<Bitmap> = withContext(Dispatchers.IO){
        val tempList = ArrayList<List<Int>>()
        val bimapList = ArrayList<Bitmap>()
        for (n in uris.indices){
            val size = getImageSize(uris[n], act)
            val imageRatio = size[0].toFloat() / size[1].toFloat()

            if (imageRatio > 1){
                if (size[0] > MAX_IMAGE_SIZE){
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))

                } else {
                    tempList.add(listOf(size[0],  size[1]))

                }

            } else {
                if (size[1] > MAX_IMAGE_SIZE){
                    tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))

                } else {
                    tempList.add(listOf(size[0],  size[1]))
                }
            }

        }
       for (i in uris.indices) {

           bimapList.add(Picasso.get().load(uris[i]).resize(tempList[i][0], tempList[i][1]).get())
       }


        return@withContext bimapList
    }

    private suspend fun getBitmapFromUris(uris: List<String?>) : List<Bitmap> = withContext(Dispatchers.IO){
        val bimapList = ArrayList<Bitmap>()

        for (i in uris.indices) {
                bimapList.add(Picasso.get().load(uris[i]).get())

        }

        return@withContext bimapList
    }

    fun fillImageArray(ad: Ad, adapter: ImageAdapter){
        val listUris = listOf(ad.mainImage, ad.image2, ad.image3)
        CoroutineScope(Dispatchers.Main).launch {
            val bitMapList = getBitmapFromUris(listUris)
            adapter.updateAdapter(bitMapList as ArrayList<Bitmap> /* = java.util.ArrayList<android.graphics.Bitmap> */)
        }
    }
}
