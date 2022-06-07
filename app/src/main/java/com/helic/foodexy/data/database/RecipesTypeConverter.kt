package com.helic.foodexy.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.helic.foodexy.data.network.models.FoodRecipeResult

class RecipesTypeConverter {

    var gson = Gson()


    @TypeConverter
    fun resultToString(foodRecipeResult: FoodRecipeResult): String {
        return gson.toJson(foodRecipeResult)
    }

    @TypeConverter
    fun stringToResult(data: String): FoodRecipeResult {
        val listType = object : TypeToken<FoodRecipeResult>() {}.type
        return gson.fromJson(data, listType)
    }

}