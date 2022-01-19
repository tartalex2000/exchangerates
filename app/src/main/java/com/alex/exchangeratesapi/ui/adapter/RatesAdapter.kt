package com.alex.exchangeratesapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alex.exchangeratesapi.R
import com.alex.exchangeratesapi.data.models.Rate
import com.alex.exchangeratesapi.di.AppModule.database
import com.alex.exchangeratesapi.ui.adapter.diffutil.RatesDiffUtil


class RatesAdapter( private val onItemClicked: (name: String) -> Unit ) : ListAdapter<Rate, RatesAdapter.RateViewHolder>(RatesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val a = RateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rate_layout, parent, false)
        )
        a.favour.setOnClickListener {
            onItemClicked(currentList[a.adapterPosition].name)

        }
        return a
    }

  /*  override fun getItemCount(): Int {
        val count = super.getItemCount()
        return count
    }*/

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.setup(currentList[position], position % 2 == 0)


    }

/*
    fun updateList(newRates: List<Rate>) {
        val diffResult = DiffUtil.calculateDiff(RatesDiffUtil(this.rates, newRates))
        this.rates.clear()
        this.rates.addAll(newRates)
        diffResult.dispatchUpdatesTo(this)
    }
*/


    inner class RateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val tvCurrency = view.findViewById<TextView>(R.id.tv_currency)
        private val tvValue = view.findViewById<TextView>(R.id.tv_value)
        val favour = view.findViewById<ImageView>(R.id.favour)

        fun setup(rate: Rate, darkColor: Boolean) {
            tvCurrency.text = rate.name
            tvValue.text = rate.value.toString()
            if (rate.isFavourite) favour.setImageResource(R.drawable.ic_star_foreground) else
                favour.setImageResource(R.drawable.ic_star_empty_foreground)
            if (darkColor)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.design_default_color_secondary_variant
                    )
                )
            else
                view.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))


        }


    }
}