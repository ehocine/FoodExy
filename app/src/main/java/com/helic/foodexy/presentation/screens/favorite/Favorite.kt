package com.helic.foodexy.presentation.screens.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.helic.foodexy.R
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.navigation.MainAppScreens
import com.helic.foodexy.presentation.ui.theme.topAppBarBackgroundColor
import com.helic.foodexy.presentation.ui.theme.topAppBarContentColor
import com.helic.foodexy.utils.DisplayAlertDialog
import com.helic.foodexy.utils.NoResults

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Favorite(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    LaunchedEffect(key1 = true) {
        mainViewModel.readFavoriteRecipes()
    }
    val favoriteRecipesEntityList by mainViewModel.favoriteRecipesList.collectAsState()

    Scaffold(topBar = {
        FavoriteTopAppBar(
            navController = navController,
            mainViewModel = mainViewModel,
            snackbar = snackbar
        )
    }) {
        if (favoriteRecipesEntityList.isEmpty()) {
            NoResults()
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(favoriteRecipesEntityList) { foodRecipeEntityResult ->
                    FavoriteRecipeItem(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        foodRecipeResultEntity = foodRecipeEntityResult
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteTopAppBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(MainAppScreens.Recipes.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back Arrow",
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        title = {
            Text(text = stringResource(R.string.favorite_recipes))
        }, actions = {
            DeleteAllAction(
                mainViewModel = mainViewModel,
                snackbar = snackbar
            )
        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun DeleteAllAction(
    mainViewModel: MainViewModel, snackbar: (String, SnackbarDuration) -> Unit
) {
    var openDeleteAllDialog by remember { mutableStateOf(false) }
    DropMenu(
        onDeleteAllClicked = { openDeleteAllDialog = true },
    )

    DisplayAlertDialog(
        title = "Delete all recipes",
        message = {
            Column {
                Text(
                    text = "Are you sure you want to delete all your favorite recipes?",
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )
            }
        },
        openDialog = openDeleteAllDialog,
        closeDialog = { openDeleteAllDialog = false },
        onYesClicked = {
            mainViewModel.deleteAllFavoriteRecipesFromDB()
            snackbar("All recipes were deleted", SnackbarDuration.Short)
        }
    )
}

@Composable
fun DropMenu(onDeleteAllClicked: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            painterResource(id = R.drawable.ic_vert),
            contentDescription = "Menu",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteAllClicked()
            }) {
                Text(
                    text = "Delete all",
                    modifier = Modifier.padding(start = 5.dp),
//                    fontSize = MaterialTheme.typography.subtitle2.fontSize
                )
            }
        }
    }
}