package com.alex.exchangeratesapi.main

import com.alex.exchangeratesapi.data.CurrencyApi
import com.alex.exchangeratesapi.data.models.CurrencyResponse
import com.alex.exchangeratesapi.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api : CurrencyApi
) : MainRepository {

    override suspend fun getRates(base: String, symbols:String?): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base, symbols)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error occured")
        }
    }
}