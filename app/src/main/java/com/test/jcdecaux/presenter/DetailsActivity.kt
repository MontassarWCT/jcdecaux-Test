package com.test.jcdecaux.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.jcdecaux.R
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //get city name
        val contractName: String = intent.getStringExtra("contract_name")
        title_city.text = contractName
        //get address
        val address: String = intent.getStringExtra("address")
        address_.text = address
        //get bike stands
        val bikeStands: Int = intent.getIntExtra("bike_stands", -1)
        bike_stands.text = bikeStands.toString()
        //get available bike stands
        val availableBikeStands: Int = intent.getIntExtra("available_bike_stands", -1)
        available_bike_stands.text = availableBikeStands.toString()
        //get bonus
        val bonus: Boolean = intent.getBooleanExtra("bike_stands", false)
        if (!bonus)
            bonus_.text = "Non"
        else
            bonus_.text = "Oui"
        //get banking
        val banking: Boolean = intent.getBooleanExtra("banking", false)
        if (!bonus)
            banking_.text = "Non"
        else
            banking_.text = "Oui"
        //get last update
        val lastUpdate: Long = intent.getLongExtra("last_update", 0)
        //convert Long to Date
        val date = Date(lastUpdate)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val lastUpdateSt = format.format(date)
        last_update.text = lastUpdateSt


    }
}