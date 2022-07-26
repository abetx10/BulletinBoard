package com.example.bulletinboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bulletinboard.act.EditAdsAct
import com.fxn.pix.Options
import com.fxn.pix.Pix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGES = 998
    fun getImages(context:AppCompatActivity, imageCounter : Int, rCode : Int) {
        val options = Options . init ()
            .setRequestCode(rCode)                                           //Request code for activity results
            .setCount(imageCounter)                                                   //Number of images to restict selection count
            .setFrontfacing(false)                                         //Front Facing camera on start
            .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
            .setPath("/pix/images")                                      //Custom Path For media Storage

        Pix.start(context, options)
    }

    fun showSelectedImages(resultCode : Int, requestCode : Int, data : Intent?, edAct : EditAdsAct){
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {
            if (data != null) {
                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (returnValues?.size!! > 1 && edAct.chooseImageFrag == null) {
                    edAct.openChoseImageFragment(returnValues)
//                } else if (returnValues.size == 1 && edAct.chooseImageFrag == null) {
//                    //imageAdapter.updateAdapter(returnValues)
//                    val tempList = ImageManager.getImageSize(returnValues[0])
//                    Log.d("MyLog", "width ${tempList[0]}")

                } else if (edAct.chooseImageFrag != null) {
                    edAct.chooseImageFrag?.updateAdapter(returnValues)

                } else if (returnValues?.size!! == 1 && edAct.chooseImageFrag == null){
                    CoroutineScope(Dispatchers.Main).launch{
                        edAct.binding.pBarLoad.visibility = View.VISIBLE
                        val bitMapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                        edAct.binding.pBarLoad.visibility = View.GONE
                        edAct.imageAdapter.updateAdapter(bitMapArray)
                    }

                }
            }

        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGES) {
            if (data != null) {
                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                edAct.chooseImageFrag?.setSingleImage(uris?.get(0)!!, edAct.editImagepos, )
            }

        }
    }
}