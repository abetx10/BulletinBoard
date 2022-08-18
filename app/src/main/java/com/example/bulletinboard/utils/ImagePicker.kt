package com.example.bulletinboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bulletinboard.R
import com.example.bulletinboard.act.EditAdsAct
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGES = 998
    private fun getOptions(imageCounter: Int): Options {
        val options = Options().apply {
            count = imageCounter
            isFrontFacing = false
            mode = Mode.Picture
            path = "/pix/images"
        }
        return options
    }


    fun getMultiImages(edAct: EditAdsAct, imageCounter: Int) {
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    getMultiSelectImages(edAct, result.data)
                }
                else -> {}
            }

        }

    }

    fun addImages(edAct: EditAdsAct, imageCounter: Int) {
        val f = edAct.chooseImageFrag
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    edAct.chooseImageFrag = f
                    openChooseImageFrag(edAct, f!!)
                    edAct.chooseImageFrag?.updateAdapter(result.data as ArrayList<Uri>, edAct /* = java.util.ArrayList<android.net.Uri> */)
                }
                else -> {}
            }

        }

    }

    fun getSingleImage(edAct: EditAdsAct,) {
        val f = edAct.chooseImageFrag
        edAct.addPixToActivity(R.id.place_holder, getOptions(1)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    edAct.chooseImageFrag = f
                    openChooseImageFrag(edAct, f!!)
                    getSingleImage(edAct, result.data[0])
                }
                else -> {}
            }

        }

    }

    private fun openChooseImageFrag(edAct: EditAdsAct, f:Fragment){
        edAct.supportFragmentManager.beginTransaction().replace(R.id.place_holder, f).commit()
    }



    private fun closePixFrag(edAct: EditAdsAct) {
        val fList = edAct.supportFragmentManager.fragments
        fList.forEach {
            if (it.isVisible) edAct.supportFragmentManager.beginTransaction().remove(it).commit()

        }
    }



    fun getMultiSelectImages(edAct: EditAdsAct, uris: List<Uri>){

                    if (uris.size!! > 1 && edAct.chooseImageFrag == null) {
                        edAct.openChoseImageFragment(uris as ArrayList<Uri> )

                    } else if (uris.size!! == 1 && edAct.chooseImageFrag == null){
                        CoroutineScope(Dispatchers.Main).launch{
                            edAct.binding.pBarLoad.visibility = View.VISIBLE
                            val bitMapArray = ImageManager.imageResize(uris, edAct) as ArrayList<Bitmap>
                            edAct.binding.pBarLoad.visibility = View.GONE
                            edAct.imageAdapter.updateAdapter(bitMapArray)
                            closePixFrag(edAct)
                        }
        }

    }

    private fun getSingleImage(edAct: EditAdsAct, uri: Uri){
                    edAct.chooseImageFrag?.setSingleImage(uri, edAct.editImagepos)
    }
}










