package com.huawei.yigitefeproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url

class RetrofitClient {

companion object {

    val baseUrl = "https://api.openchargemap.io/v3/poi?key=1111"

    fun getClient(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}
   /* val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openchargemap.io/v3")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(OpenChargeMapService::class.java) */

/*companion object {
    fun getClient(baseUrl: "https://api.openchargemap.io/v3"): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.openchargemap.io/v3")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/
}


