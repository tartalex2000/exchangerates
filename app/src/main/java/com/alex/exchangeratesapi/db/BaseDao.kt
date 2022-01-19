package com.alex.exchangeratesapi.db

import androidx.room.*

interface BaseDao<T> {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg onj: T)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg onj: T)

    @Delete
    fun delete(obj: T)
}