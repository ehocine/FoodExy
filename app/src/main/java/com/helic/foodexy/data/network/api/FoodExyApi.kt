package com.helic.foodexy.data.network.api

import com.helic.foodexy.data.network.models.FoodRecipesList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodExyApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipesList(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipesList>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipesList>
}