package com.creativegarage.dreamcity.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object Api {
    private var retrofit: Retrofit? = null

    private fun getRetrofit(baseUrl: String): Retrofit? {
        val interceptor = Interceptor { chain ->
            val request = chain.request()
            val accept = request.newBuilder()
                .addHeader("ACCEPT", "application/json")
                .build()
            chain.proceed(accept)
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create()) //important
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
        return retrofit


//        retrofit = Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//
//        val client = OkHttpClient.Builder().addInterceptor(logging).build()
//        Retrofit.Builder().baseUrl(Urls.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client).build()

        return retrofit

    }



    fun getService(baseUrl: String): Service {
        return getRetrofit(baseUrl)!!.create(Service::class.java)
    }




}