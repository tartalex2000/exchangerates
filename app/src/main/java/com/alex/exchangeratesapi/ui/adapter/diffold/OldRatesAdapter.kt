package com.alex.exchangeratesapi.ui.adapter.diffold

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alex.exchangeratesapi.R
import com.alex.exchangeratesapi.data.models.Rate

class OldRatesAdapter (private val rates: ArrayList<Rate>, private val onItemClicked: (name: String) -> Unit) : RecyclerView.Adapter<RateViewHolder>() {
     val new_rates: ArrayList<Rate>
    init {
        new_rates= ArrayList()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val a = RateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rate_layout, parent, false)
        )
        a.favour.setOnClickListener {
            onItemClicked( new_rates[a.adapterPosition].name)

        }
        return a
    }

    override fun getItemCount(): Int {
        return new_rates.size
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.setup( new_rates[position], position % 2 == 0)
    }

    fun updateList(newRates: List<Rate>) {

        val diffResult = DiffUtil.calculateDiff(OldRatesDiffUtil(this.new_rates, newRates))
        this.new_rates.clear()
        this.new_rates.addAll(newRates)
        diffResult.dispatchUpdatesTo(this)
    }
}

class RateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

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