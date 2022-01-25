package com.alex.exchangeratesapi.repository

import androidx.lifecycle.viewModelScope
import com.alex.exchangeratesapi.data.models.Rate
import com.alex.exchangeratesapi.db.AppDatabase
import com.alex.exchangeratesapi.db.RatesDao
import kotlinx.coroutines.launch
import javax.inject.Inject


class RoomRepository @Inject constructor(private val ratesDao: RatesDao) {


    suspend fun add(rate: Rate) {

          return ratesDao.insert(rate)


    }
    suspend  fun delete(rate: Rate) {

          return  ratesDao.clearRate(rate.name)

    }
}