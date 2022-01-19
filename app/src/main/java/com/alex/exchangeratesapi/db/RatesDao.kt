package com.alex.exchangeratesapi.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.alex.exchangeratesapi.data.models.Rate
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RatesDao : BaseDao<Rate> {

    @Query("SELECT * FROM Rate")
    abstract fun getRates(): LiveData<List<Rate>>

    @Query("DELETE FROM Rate")
    abstract fun clearRates()

    @Query("DELETE FROM Rate WHERE name=:rate")
    abstract fun clearRate(rate:String)

    @Query("SELECT * FROM Rate")
    abstract fun getFavRates(): Flow<List<Rate>>

}