package com.alex.exchangeratesapi.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Rate(
    @PrimaryKey
    val name: String,
    val value: Double,
    var isFavourite:Boolean

)