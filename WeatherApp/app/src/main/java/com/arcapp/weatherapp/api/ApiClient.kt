package com.arcapp.retrofitexample.api

import com.arcapp.weatherapp.constant.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object{

        private lateinit var apiService: ApiService

        fun getApiService() : ApiService{

            val interceptor = Interceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .url(chain.request().url.newBuilder().addQueryParameter(Constants.API_KEY_NAME, Constants.API_KEY).build())
                    .build()
                chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            if(!::apiService.isInitialized){
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.API_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

                apiService = retrofit.create(ApiService::class.java)

            }

            return apiService

        }

    }

}