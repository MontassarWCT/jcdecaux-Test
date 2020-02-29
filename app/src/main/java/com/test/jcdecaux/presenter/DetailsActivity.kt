package com.test.jcdecaux.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.jcdecaux.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val contractName: String = intent.getStringExtra("contract_name")
        title_text.text = contractName
        val address: String = intent.getStringExtra("address")
        address_text.text = address


    }
}