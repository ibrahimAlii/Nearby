package ib.project.nearby.network

import ib.project.nearby.data.model.Data
import kotlinx.coroutines.Deferred
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query


interface NearbyAPI {


    @GET("explore")
    fun getNearbyPlacesAsync(
        @Query("client_id") clientId: String, @Query("client_secret") clientSecret: String,
        @Query("ll") location: String, @Query("v") version: String
    ): Deferred<Data>
}
//v=20180323&limit=1&ll=40.7243,-74.0018&query=coffee