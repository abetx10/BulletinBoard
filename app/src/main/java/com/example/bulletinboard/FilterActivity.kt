package com.example.bulletinboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.bulletinboard.databinding.ActivityFilterBinding
import com.example.bulletinboard.dialogs.DialogSpinnerHelper
import com.example.bulletinboard.utils.CityHelper
import java.lang.StringBuilder

class FilterActivity : AppCompatActivity() {
    lateinit var binding: ActivityFilterBinding
    var dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        onClickSelectCity()
        onClickDone()
        onClickSelectCountry()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun onClickSelectCountry() = with(binding){
        tvCountry.setOnClickListener{
            val listCountry = CityHelper.getAllCountries(this@FilterActivity)
            dialog.showSpinnerDialog(this@FilterActivity, listCountry, tvCountry)
            if (tvCity.text.toString() != getString(R.string.select_city)) {
               tvCity.text = getString(R.string.select_city)
            }
        }

    }

   private fun onClickSelectCity() = with(binding){
       tvCity.setOnClickListener{
           val selectedCountry = tvCountry.text.toString()
           if (selectedCountry != getString(R.string.select_country)) {
               val listCity = CityHelper.getAllCities(selectedCountry, this@FilterActivity)
               dialog.showSpinnerDialog(this@FilterActivity, listCity, tvCity)
           } else {
               Toast.makeText(this@FilterActivity, "No country selected", Toast.LENGTH_LONG).show()
           }
       }
    }

    private fun onClickDone() = with(binding){
        btDone.setOnClickListener{
            Log.d("MyLog", "Filter: ${createFilter()}")

        }
    }

    private fun createFilter(): String = with(binding){
        val sBuilder = StringBuilder()
        val arrayTempFilter = listOf(tvCountry.text, tvCity.text)
        for ((i, s) in arrayTempFilter.withIndex()){
            if (s != getString(R.string.select_country) && s != getString(R.string.select_city)){
                sBuilder.append(s)
                if(i != arrayTempFilter.size-1) sBuilder.append("_")
            }
        }
        return sBuilder.toString()
    }

    fun actionBarSettings(){
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }


}