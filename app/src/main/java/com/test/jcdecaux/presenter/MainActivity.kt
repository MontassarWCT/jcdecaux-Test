package com.test.jcdecaux.presenter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.test.jcdecaux.JsonPositionHolderApi
import com.test.jcdecaux.adapter.MainAdapter
import com.test.jcdecaux.R
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : Activity() {

    var TAG: String = "MainActivity"
    var FASTEST_INTERVAL: Long = 8 * 1000 // 8 SECOND
    var UPDATE_INTERVAL: Long = 2000 // 2 SECOND
    var FINE_LOCATION_REQUEST: Int = 888
    lateinit var locationRequest: LocationRequest

    private val jsonPlaceholderApi = JsonPositionHolderApi.getApi()
    private val adapter = MainAdapter(listOf())
     var myCurrentLongitude: Double = 0.0
    var myCurrentLatitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermissions()) {
            initLocationUpdate()
        }
        recycler.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            progress.visibility = View.VISIBLE
            val postsResponse = jsonPlaceholderApi.getPosts().await()
            progress.visibility = View.GONE
            if (postsResponse.isSuccessful) {
                adapter.items = postsResponse.body() ?: listOf()
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MainActivity, "Error ${postsResponse.code()}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    //Start Location update as define intervals
    fun initLocationUpdate() {

        //init location request to start retrieving location update
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        //Create LocationSettingRequest object using locationRequest
        val locationSettingBuilder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        locationSettingBuilder.addLocationRequest(locationRequest)
        val locationSetting: LocationSettingsRequest = locationSettingBuilder.build()

        //Need to check whether location settings are satisfied
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSetting)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                //super.onLocationResult(p0)
                if (locationResult != null) {
                    onLocationChanged(locationResult.lastLocation)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        },
                Looper.myLooper())

    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        myCurrentLongitude = location.longitude
        myCurrentLatitude = location.latitude
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions()
            return false
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_LOCATION_REQUEST)
    }

    companion object {
        fun newInstance() = MainActivity()
    }
}