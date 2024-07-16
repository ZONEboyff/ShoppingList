package com.example.shoppinglist

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingAPIService {
    @GET("maps/api/geocode/json")
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng:String,
        @Query("key") apiKey: String
    ):GeocodingResponse
}