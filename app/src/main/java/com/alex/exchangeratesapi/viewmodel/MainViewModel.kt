package com.alex.exchangeratesapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.exchangeratesapi.data.models.Rate
import com.alex.exchangeratesapi.db.AppDatabase
import com.alex.exchangeratesapi.repository.MainRepository
import com.alex.exchangeratesapi.repository.RoomRepository
import com.alex.exchangeratesapi.util.DispatcherProvider
import com.alex.exchangeratesapi.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val roomrepository: RoomRepository,
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

    private val  _favourtates= MutableStateFlow(ArrayList<Rate>())
    val favourtates : StateFlow<ArrayList<Rate>> = _favourtates


    fun add(rate:Rate) {
        viewModelScope.launch(dispatcher.io) {
            roomrepository.add(rate)
        }

    }
    fun delete(rate:Rate) {
        viewModelScope.launch(dispatcher.io) {
            roomrepository.delete(rate)
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

}