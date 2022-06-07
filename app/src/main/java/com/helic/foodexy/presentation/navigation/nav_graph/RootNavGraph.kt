package com.helic.foodexy.presentation.navigation.nav_graph

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.navigation.MainAppScreens
import com.helic.foodexy.presentation.screens.favorite.Favorite
import com.helic.foodexy.presentation.screens.favorite.FavoriteRecipeDetails
import com.helic.foodexy.presentation.screens.recipes.RecipeDetails
import com.helic.foodexy.presentation.screens.recipes.Recipes
import com.helic.foodexy.utils.Constants.ROOT_ROUTE

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = MainAppScreens.Recipes.route,
        route = ROOT_ROUTE
    ) {
        composable(route = MainAppScreens.Recipes.route) {
            Recipes(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.Favorite.route) {
            Favorite(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.RecipeDetails.route) {
            RecipeDetails(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
        composable(route = MainAppScreens.FavoriteRecipeDetails.route) {
            FavoriteRecipeDetails(
                navController = navController,
                mainViewModel = mainViewModel,
                snackbar = showSnackbar
            )
        }
    }
}