package com.alex.exchangeratesapi.ui.adapter.diffold

import androidx.recyclerview.widget.DiffUtil
import com.alex.exchangeratesapi.data.models.Rate

class OldRatesDiffUtil constructor(
    private val oldList: List<Rate>,
    private val newList: List<Rate>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].isFavourite == newList[newItemPosition].isFavourite)
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].value == newList[newItemPosition].value)
    }
}