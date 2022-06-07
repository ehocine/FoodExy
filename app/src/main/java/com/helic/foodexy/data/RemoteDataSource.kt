package com.helic.foodexy.data

import com.helic.foodexy.data.network.api.FoodExyApi
import com.helic.foodexy.data.network.models.FoodRecipesList
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodExyApi: FoodExyApi) {

    suspend fun getRecipesList(queries: Map<String, String>): Response<FoodRecipesList> {
        return foodExyApi.getRecipesList(queries = queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipesList> {
        return foodExyApi.searchRecipes(searchQuery)
    }
}