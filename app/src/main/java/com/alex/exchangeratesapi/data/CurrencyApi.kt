package com.alex.exchangeratesapi.data

import com.alex.exchangeratesapi.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("latest?access_key=17b5525934ab2b81908855a2532d7c8c&format=1")
    suspend fun getRates (
        @Query("base") base:String, @Query("symbols") symbols:String?
    ) : Response<CurrencyResponse>

}