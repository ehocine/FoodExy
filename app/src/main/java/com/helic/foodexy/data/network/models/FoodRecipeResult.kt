package com.helic.foodexy.data.network.models


import com.google.gson.annotations.SerializedName

data class FoodRecipeResult(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int = 0,
    @SerializedName("cheap")
    val cheap: Boolean = false,
    @SerializedName("dairyFree")
    val dairyFree: Boolean = false,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient> = listOf(),
    @SerializedName("glutenFree")
    val glutenFree: Boolean = false,
    @SerializedName("id")
    val recipeId: Int = 0,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int = 0,
    @SerializedName("sourceName")
    val sourceName: String? = "",
    @SerializedName("sourceUrl")
    val sourceUrl: String = "",
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("vegan")
    val vegan: Boolean = false,
    @SerializedName("vegetarian")
    val vegetarian: Boolean = false,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean = false,
)