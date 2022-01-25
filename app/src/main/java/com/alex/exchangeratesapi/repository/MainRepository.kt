package com.alex.exchangeratesapi.repository

import com.alex.exchangeratesapi.data.models.CurrencyResponse
import com.alex.exchangeratesapi.util.Resource

interface MainRepository {

    suspend fun getRates(base: String, symbols: String?=null): Resource<CurrencyResponse>

}