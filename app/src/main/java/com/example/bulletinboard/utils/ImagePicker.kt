package com.example.bulletinboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bulletinboard.act.EditAdsAct
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGES = 998
    private fun getOptions(imageCounter : Int): Options {
        val options = Options . init ()
            .setCount(imageCounter)                                                   //Number of images to restict selection count
            .setFrontfacing(false)                                         //Front Facing camera on start
            .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
            .setPath("/pix/images")                                      //Custom Path For media Storage
        return options
    }

    fun launcher(edAct: EditAdsAct, launcher: ActivityResultLauncher<Intent>?, imageCount : Int){
        PermUtil.checkForCamaraWritePermissions(edAct){

        }
        val intent = Intent(edAct, Pix::class.java).apply {
            putExtra("options", getOptions(imageCount))
        }
        launcher?.launch(intent)

    }

    fun getLauncherForMultiSelectImages(edAct: EditAdsAct) : ActivityResultLauncher<Intent>{
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val returnValues = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
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
            }
        }

    }

    fun getLauncherForSingleImage(edAct: EditAdsAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edAct.chooseImageFrag?.setSingleImage(uris?.get(0)!!, edAct.editImagepos)
                }

            }
        }
    }
}


