package com.helic.foodexy.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainAppScreens(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Recipes : MainAppScreens(
        route = "recipes",
        title = "Recipes",
        icon = Icons.Default.Book
    )

    object Favorite : MainAppScreens(
        route = "favorite",
        title = "Favorite",
        icon = Icons.Default.Star
    )

    object RecipeDetails : MainAppScreens(
        route = "recipe_details",
        title = "Recipe details",
        icon = Icons.Default.Details
    )

    object FavoriteRecipeDetails : MainAppScreens(
        route = "favorite_recipe_details",
        title = "Favorite recipe details",
        icon = Icons.Default.Details
    )

}