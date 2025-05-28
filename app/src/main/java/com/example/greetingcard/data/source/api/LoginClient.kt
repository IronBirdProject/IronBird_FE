//package com.example.greetingcard.data.source.api
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
//
//object LoginClient {
//    private val BASE_URL = "http://10.0.2.2:8080/api/auth/"
//
//    val apiService: AuthApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(AuthApi::class.java)
//    }
//
//    val apiSuccessTest: AuthApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(AuthApi::class.java)
//    }
//
//}