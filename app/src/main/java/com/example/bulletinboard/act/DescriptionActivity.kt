package com.example.bulletinboard.act

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.example.bulletinboard.adapters.ImageAdapter
import com.example.bulletinboard.databinding.ActivityDescriptionBinding
import com.example.bulletinboard.model.Ad
import com.example.bulletinboard.utils.ImageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.fbPhone.setOnClickListener{call()}
        binding.fbEmail.setOnClickListener { sendEmail() }

    }

    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            viewPager.adapter = adapter
        }
        getIntentFromMainAct()
        imageChangeCounter()
    }

    private fun getIntentFromMainAct(){
        ad = intent.getSerializableExtra("AD") as Ad
        if (ad != null)
            updateUI(ad!!)
     }

    private fun updateUI(ad:Ad){
        ImageManager.fillImageArray(ad, adapter)
        fillTextViews(ad)
    }

    private fun fillTextViews(ad: Ad) = with(binding){
        tvTItle.text = ad.title
        tvDescription.text = ad.description
        tvEmail.text = ad.email
        tvPrice.text = ad.price
        tvPhone.text = ad.phone
        tvCountry.text = ad.country
        tvCity.text = ad.city


    }

    private fun call(){
        val callUri = "tel:${ad?.phone}"
        val iCall = Intent(Intent.ACTION_DIAL)
        iCall.data = callUri.toUri()
        startActivity(iCall)
    }

    private fun sendEmail(){
        val iSendEmail = Intent(Intent.ACTION_SEND)
        iSendEmail.type = "message/rfc822"
        iSendEmail.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(ad?.email))
            putExtra(Intent.EXTRA_SUBJECT, ad?.title)
            putExtra(Intent.EXTRA_TEXT, "Меня интересует ваше обьявление")
        }
        try {
            startActivity(Intent.createChooser(iSendEmail, "Октрыть с помощью"))
        } catch (e: ActivityNotFoundException){
            "У вас нет приложения для отправки эмейлов"

        }
    }

    private fun imageChangeCounter(){
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCounter = "${position + 1}/${binding.viewPager.adapter?.itemCount}"
                binding.tvImageCounter.text = imageCounter
            }
        })
    }
}