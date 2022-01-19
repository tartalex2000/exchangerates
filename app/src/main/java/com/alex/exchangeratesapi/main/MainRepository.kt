package com.alex.exchangeratesapi.main

import com.alex.exchangeratesapi.data.models.CurrencyResponse
import com.alex.exchangeratesapi.data.models.Rates
import com.alex.exchangeratesapi.util.Resource
import java.util.concurrent.Flow

interface MainRepository {

    suspend fun getRates(base: String, symbols: String?=null): Resource<CurrencyResponse>

}