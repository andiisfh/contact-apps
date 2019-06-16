package com.andiinsanudin.jenius.testapp.retorfit

import com.andiinsanudin.jenius.testapp.model.ContactAPI
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Andi Insanudin on 2019-06-12.
 */
object RetrofitFactory {

    private const val baseUrl = "https://simple-contact-crud.herokuapp.com"

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val service : ContactAPI = retrofit().create(ContactAPI::class.java)
}