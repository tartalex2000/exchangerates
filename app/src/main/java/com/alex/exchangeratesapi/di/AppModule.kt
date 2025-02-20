package com.alex.exchangeratesapi.di

import android.content.Context
import androidx.room.Room
import com.alex.exchangeratesapi.data.CurrencyApi
import com.alex.exchangeratesapi.db.AppDatabase
import com.alex.exchangeratesapi.db.RatesDao
import com.alex.exchangeratesapi.repository.NetworkRepository
import com.alex.exchangeratesapi.repository.MainRepository
import com.alex.exchangeratesapi.repository.RoomRepository
import com.alex.exchangeratesapi.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton


private const val BASE_URL = "https://api.exchangeratesapi.io/v1/"

private const val BASE_URL1 = "https://openexchangerates.org/api/"

private const val BASE_URL2 = "https://api.currencyfreaks.com/v2.0/rates/"

private const val BASE_URL3 = "https://api.frankfurter.dev/v1/"
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "exchange-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRateDao(database: AppDatabase): RatesDao {
        return database.rateDao()
    }
    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi {

        val httpLogInterceptor = HttpLoggingInterceptor()
        httpLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient().newBuilder()
                    .addInterceptor(httpLogInterceptor)
                    .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL3)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CurrencyApi::class.java)

    }
    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi) : MainRepository = NetworkRepository(
        api
    )
    @Singleton
    @Provides
    fun provideRoomRepository(ratesDao: RatesDao) : RoomRepository = RoomRepository(ratesDao)

    @Singleton
    @Provides
    fun provideDispatchers() : DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

}