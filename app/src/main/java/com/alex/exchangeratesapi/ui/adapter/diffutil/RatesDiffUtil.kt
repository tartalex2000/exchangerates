package com.alex.exchangeratesapi.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.alex.exchangeratesapi.data.models.Rate

class RatesDiffUtil  : DiffUtil.ItemCallback<Rate?>() {
    override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return oldItem.name.equals(newItem.name)

    }

    override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return (oldItem.name.equals(newItem.name)&&oldItem.isFavourite.equals(newItem.isFavourite))
    }
}