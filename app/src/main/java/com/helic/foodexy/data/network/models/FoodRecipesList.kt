package com.helic.foodexy.data.network.models


import com.google.gson.annotations.SerializedName

data class FoodRecipesList(
    @SerializedName("results")
    val foodRecipeResults: List<FoodRecipeResult> = listOf()
)