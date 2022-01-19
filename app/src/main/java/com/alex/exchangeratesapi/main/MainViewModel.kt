package com.alex.exchangeratesapi.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alex.exchangeratesapi.data.models.Rate
import com.alex.exchangeratesapi.data.models.Rates
import com.alex.exchangeratesapi.db.AppDatabase
import com.alex.exchangeratesapi.util.DispatcherProvider
import com.alex.exchangeratesapi.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcher: DispatcherProvider
): ViewModel() {

    sealed class CurrencyEvent {
        class Success(val rates: List<Rate>) : CurrencyEvent()
        class Failiure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    @Inject  lateinit var database: AppDatabase
    private val  _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion : StateFlow<CurrencyEvent> = _conversion

    val _conversionrates : MutableStateFlow<ArrayList<Rate>> =MutableStateFlow(ArrayList<Rate>())
    val conversionrates : StateFlow<ArrayList<Rate>> =_conversionrates

    private val  _favourtates= MutableStateFlow(ArrayList<Rate>())
    val favourtates : StateFlow<ArrayList<Rate>> = _favourtates

    private val  _combinedrates= MutableStateFlow(ArrayList<Rate>())
    val combinedrates : StateFlow<ArrayList<Rate>> = _combinedrates
    //val conversion1 : LiveData<CurrencyEvent> = _conversion.asLiveData()

    fun add(rate:Rate) {
        viewModelScope.launch(dispatcher.io) {
            database.rateDao().insert(rate)
        }

    }
    fun delete(rate:Rate) {
        viewModelScope.launch(dispatcher.io) {
            database.rateDao().clearRate(rate.name)
        }

    }
    fun <T> merge(vararg flows: Flow<T>): Flow<T> = flowOf(*flows).flattenMerge(1)

    fun getRates(fromCurrency: String, symbols:String?=null) {

        viewModelScope.launch(dispatcher.io) {
            _conversion.value = CurrencyEvent.Loading
            when (val ratesResponse = repository.getRates(fromCurrency, symbols)) {
                is Resource.Error<*> -> _conversion.value =
                    CurrencyEvent.Failiure(ratesResponse.message!!)
                is Resource.Success<*> -> {
                    val rates = ratesResponse.data!!.rates
                    /*      val rate = getRateForCurrency(toCurrency, rates)*/
                    if (rates == null) {
                        _conversion.value = CurrencyEvent.Failiure("Unexpected Error")
                    } else {
                        val arrayrate: ArrayList<Rate>
                        arrayrate = ArrayList()
                        rates.forEach { (key, value) ->
                            arrayrate.add(
                                Rate(key,
                                    value.toDouble(),
                                    false )
                            )
                        }
                        _conversion.value = CurrencyEvent.Success(arrayrate)
                        _conversionrates.value = arrayrate
                    }
                }

            }

        }
    }
    fun observerFavourRates() {
        viewModelScope.launch(dispatcher.io) {
            database.rateDao().getFavRates().collect {
                _favourtates.value=ArrayList(it)
            }
        }
    }

    fun favoriteModels() : Flow<List<Rate>> =
         _conversionrates.combine(_favourtates) { rates, favorites ->

            rates.map {
                if (favorites.map { it.name }.contains(it.name)) {
                    Rate(it.name,it.value,true)

                } else {
                    Rate(it.name,it.value,false)

                }

            }

        }



    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }
}