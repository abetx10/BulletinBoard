package com.example.bulletinboard.frag


import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bulletinboard.R
import com.example.bulletinboard.act.EditAdsAct
import com.example.bulletinboard.databinding.ListImageFragBinding
import com.example.bulletinboard.dialoghelper.ProgressDialog
import com.example.bulletinboard.utils.AdapterCallback
import com.example.bulletinboard.utils.ImageManager
import com.example.bulletinboard.utils.ImagePicker
import com.example.bulletinboard.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



class ImageListFrag(val fragCloseInterface : FragmentCloseInterface) : Fragment(), AdapterCallback{

    lateinit var binding : ListImageFragBinding
    val adapter = SelectImageRvAdapter(this)
    val dragCallBack = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallBack)
    private var job : Job? = null
    private var addImageItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListImageFragBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        touchHelper.attachToRecyclerView(binding.rcViewSelectImage)
        binding.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        binding.rcViewSelectImage.adapter = adapter

    }

    override fun onItemDelete() {
        addImageItem?.isVisible = true

    }

    fun updateAdapterFromEdit(bitmapList :List<Bitmap>) {
        adapter.updateAdapter(bitmapList, true)

    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel()

    }

//    override fun onClose(){
//        super.on
//    }
    


    

    fun resizeSelectedImage(newList: ArrayList<Uri>, needClear : Boolean, activity: Activity){
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity)
            val bitmapList = ImageManager.imageResize(newList, activity)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.mainArray.size > 2) addImageItem?.isVisible = false
        }

    }

    private fun setUpToolBar(){
        binding.tb.inflateMenu(R.menu.menu_chose_image)
        val deleteItem = binding.tb.menu.findItem(R.id.id_delete_image)
        addImageItem = binding.tb.menu.findItem(R.id.id_add_image)
        if (adapter.mainArray.size > 2) addImageItem?.isVisible = false

        binding.tb.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()

        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            addImageItem?.isVisible = true
            true
        }


        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.addImages(activity as EditAdsAct,imageCount)
            true
        }

    }

    fun updateAdapter(newList: ArrayList<Uri>, activity: Activity) {
        resizeSelectedImage(newList, false, activity)

    }

    fun setSingleImage(uri : Uri, pos : Int){
        val pBar = binding.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pbar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(arrayListOf(uri), activity as Activity)
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0 ]
            adapter.notifyItemChanged(pos)
        }


    }


}