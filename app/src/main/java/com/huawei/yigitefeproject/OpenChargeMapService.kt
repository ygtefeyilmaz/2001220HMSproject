package com.huawei.yigitefeproject

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenChargeMapService {
    @GET("/api/v3/poi?key=1111")
    suspend fun getCountries(@Query("countrycode")code: String): List<Country>
}