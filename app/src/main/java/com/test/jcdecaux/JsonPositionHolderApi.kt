@file:Suppress("unused")

package com.test.jcdecaux

import com.test.jcdecaux.model.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface JsonPositionHolderApi {

    @GET("/vls/v1/stations?apiKey=9368411c46563d0e4ff94dec0c938697260d0867")
    fun getPosts(): Deferred<Response<List<Post>>>

    companion object {
        private const val BASE_URL = "https://api.jcdecaux.com"
        fun getApi(): JsonPositionHolderApi {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(getClient())
                    .build()
                    .create(JsonPositionHolderApi::class.java)
        }

        private fun getClient(): OkHttpClient {
            val client = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }
            return client.build()
        }
    }
}