package com.example.greetingcard.data.source.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //    private const val BASE_URL = "http://10.0.2.2:8080"
//    private const val BASE_URL = "http://192.168.0.5:8080"
//    private const val BASE_URL = "http://172.20.10.6:8080"
    private const val BASE_URL = "http://192.168.0.31:8080"


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun providePlanApi(retrofit: Retrofit): PlanApi {
        return retrofit.create(PlanApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
