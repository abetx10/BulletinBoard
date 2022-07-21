package com.example.bulletinboard.frag


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bulletinboard.R
import com.example.bulletinboard.databinding.ListImageFragBinding
import com.example.bulletinboard.utils.ImagePicker
import com.example.bulletinboard.utils.ItemTouchMoveCallback


class ImageListFrag(val fragCloseInterface : FragmentCloseInterface, private  val newList : ArrayList<String>) : Fragment(){

    lateinit var binding : ListImageFragBinding
    val adapter = SelectImageRvAdapter()
    val dragCallBack = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallBack)

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
        val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }
        adapter.updateAdapter(updateList, true)




    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)

    }

    private fun setUpToolBar(){
        binding.tb.inflateMenu(R.menu.menu_chose_image)
        val deleteItem = binding.tb.menu.findItem(R.id.id_delete_image)
        val addImageItem = binding.tb.menu.findItem(R.id.id_add_image)

        binding.tb.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()

        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            true
        }


        addImageItem.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity, imageCount)
            true
        }

    }

    fun updateAdapter(newList: ArrayList<String>) {
        val updateList = ArrayList<SelectImageItem>()
        for (n in adapter.mainArray.size until newList.size + adapter.mainArray.size){
            updateList.add(SelectImageItem(n.toString(), newList[n - adapter.mainArray.size]))
        }
        adapter.updateAdapter(updateList, false)

    }
}