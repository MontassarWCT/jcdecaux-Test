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
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.test.jcdecaux.JsonPositionHolderApi
import com.test.jcdecaux.R
import com.test.jcdecaux.adapter.MainAdapter
import com.test.jcdecaux.model.Post
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

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

                val items = postsResponse.body() ?: listOf()

                val lngLatList = mutableListOf<Post>()
                val nearestPositionsList = mutableListOf<Post>()

                items.forEach {

                    val latitude = it.position.lat
                    val longitude = it.position.lng
                    LatLng(latitude, longitude)
                    lngLatList.add(it)

                    // I will change it dynamic
                    var currentLatitude = 48.923030
                    var currentLongitude = 2.032140
                    if (getDistanceBetweenPoints(currentLatitude, currentLongitude, latitude, longitude) < 10) {
                        nearestPositionsList.add(it)
                    }
                }
                adapter.items = nearestPositionsList
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

    private fun getDistanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    companion object {
        fun newInstance() = MainActivity()
    }
}