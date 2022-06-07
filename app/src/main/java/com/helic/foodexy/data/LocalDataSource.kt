package com.helic.foodexy.data

import com.helic.foodexy.data.database.FavoriteRecipesEntity
import com.helic.foodexy.data.database.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {


    fun readFavoriteRecipes(): Flow<List<FavoriteRecipesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }


    suspend fun insertFavoriteRecipes(favoriteRecipesEntity: FavoriteRecipesEntity) {
        recipesDao.insertFavoriteRecipe(favoriteRecipesEntity)
    }

    suspend fun deleteFavoriteRecipe(favoriteRecipesEntity: FavoriteRecipesEntity) {
        recipesDao.deleteFavoriteRecipe(favoriteRecipesEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

}