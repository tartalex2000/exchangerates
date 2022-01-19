package com.alex.exchangeratesapi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alex.exchangeratesapi.data.models.Rate

/**
 * This app doesn't use migrations.
 * If you need to change db structure just increment the db version and make sure fallbackToDestructiveMigration()
 */
@Database(entities = [Rate::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rateDao(): RatesDao

}