package com.helic.foodexy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helic.foodexy.data.network.models.FoodRecipeResult
import com.helic.foodexy.utils.Constants.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteRecipesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var foodRecipeResult: FoodRecipeResult = FoodRecipeResult()
)