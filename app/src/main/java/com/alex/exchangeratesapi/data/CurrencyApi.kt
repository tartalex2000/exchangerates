package com.alex.exchangeratesapi.data

import com.alex.exchangeratesapi.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    val  api_key: String
        get() = "9c5e43d77efd445388b9471dadc1671f"
    val  api_key1: String
        get() = "17b5525934ab2b81908855a2532d7c8c"

    //https://currencyfreaks.com/
    val   api_key2: String
        get() = "e894d152af4f4bd2adcbe3dce1b173c2"
//    https://fixer.io/dashboard

    val   api_key3: String
        get() = "0354fa502c94684a05dc88313992e8f2"
//    https://exchangerate.host/

    val   api_key4: String
        get() = "aac1b66f9962f62c167ff7014ffad84c"
    //
  @GET("latest?format=1")
    suspend fun getRates (
        @Query("base") base:String, @Query("symbols") symbols:String?
    ) : Response<CurrencyResponse>
  /*  @GET("latest.json?app_id=9c5e43d77efd445388b9471dadc1671f&format=1")
    suspend fun getRates1 (
        @Query("symbols") symbols:String?
    ) : Response<CurrencyResponse>*/
/*    @GET("latest?apikey=e894d152af4f4bd2adcbe3dce1b173c2&format=json")
    suspend fun getRates (
        @Query("base") base:String
    ) : Response<CurrencyResponse>*/
}