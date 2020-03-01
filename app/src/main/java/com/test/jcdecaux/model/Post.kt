package com.test.jcdecaux.model

import java.io.Serializable

data class Post(
        val status: String,
        val name: String,
        val contract_name: String,
        val address: String,
        val bike_stands:Int,
        val available_bike_stands:Int,
        val bonus:Boolean,
        val banking:Boolean,
        val last_update:Long,
        val position: Position
): Serializable {
    data class Position(
            val lat: Double,
            val lng: Double
    )
}