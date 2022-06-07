package com.helic.foodexy.presentation.screens.recipes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.helic.foodexy.R
import com.helic.foodexy.data.network.models.FoodRecipeResult
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.navigation.MainAppScreens
import com.helic.foodexy.presentation.ui.theme.ButtonColor
import com.helic.foodexy.presentation.ui.theme.TOP_APP_BAR_HEIGHT
import com.helic.foodexy.presentation.ui.theme.topAppBarBackgroundColor
import com.helic.foodexy.presentation.ui.theme.topAppBarContentColor
import com.helic.foodexy.utils.ErrorLoadingResults
import com.helic.foodexy.utils.LoadingList
import com.helic.foodexy.utils.LoadingState
import com.helic.foodexy.utils.SearchAppBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Recipes(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val scope = rememberCoroutineScope()
    val getFoodRecipesListLoadingState by mainViewModel.getFoodRecipesListLoadingState.collectAsState()

    val searchAppBarState: SearchAppBarState by mainViewModel.searchAppBarState

    val foodRecipesList by mainViewModel.foodRecipesListResponse

    val searchFoodRecipesList by mainViewModel.searchFoodRecipesListResponse

    val searchText by mainViewModel.searchTextState

    val filteredList: List<FoodRecipeResult> = if (searchText.isEmpty()) {
        foodRecipesList
    } else {
        searchFoodRecipesList
    }

    LaunchedEffect(key1 = true) {
        mainViewModel.getFoodRecipesList(
            queries = mainViewModel.applyQueries(),
            snackbar = snackbar
        )
    }

    Scaffold(
        topBar = {
            when (searchAppBarState) {
                SearchAppBarState.CLOSED -> {
                    RecipeTopAppBar(
                        navController = navController,
                        onSearchClicked = {
                            mainViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                        }
                    )
                }
                else -> {
                    SearchAppBar(
                        mainViewModel = mainViewModel,
                        onSearch = { query ->
                            mainViewModel.searchTextState.value = query
                            mainViewModel.searchRecipes(
                                query = mainViewModel.applySearchQuery(query),
                                snackbar = snackbar
                            )
                        }
                    )
                    {
                        mainViewModel.searchAppBarState.value =
                            SearchAppBarState.CLOSED
                        mainViewModel.searchTextState.value = ""

                    }
                }

            }
        },
        floatingActionButton = {
            when (getFoodRecipesListLoadingState) {
                LoadingState.ERROR -> {
                    ExtendedFloatingActionButton(
                        onClick = {
                            mainViewModel.getFoodRecipesList(
                                queries = mainViewModel.applyQueries(),
                                snackbar = snackbar
                            )
                        },
                        backgroundColor = MaterialTheme.colors.ButtonColor,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Button",
                                tint = Color.White
                            )
                        },
                        text = { Text(text = "Refresh", color = Color.White) }
                    )
                }
                else -> {
                    ExtendedFloatingActionButton(
                        onClick = {
                            scope.launch {
                                mainViewModel.modalBottomSheetState.show()
                            }
                        },
                        backgroundColor = MaterialTheme.colors.ButtonColor,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = "Filter Button",
                                tint = Color.White
                            )
                        },
                        text = { Text(text = "Filter recipes", color = Color.White) }
                    )
                }
            }
        }) {
        when (getFoodRecipesListLoadingState) {
            LoadingState.LOADING -> {
                LoadingList()
            }
            LoadingState.ERROR -> {
                ErrorLoadingResults()
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredList) { foodRecipeResult ->
                        RecipeItem(
                            navController = navController,
                            mainViewModel = mainViewModel,
                            foodRecipeResult = foodRecipeResult
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun RecipeTopAppBar(navController: NavController, onSearchClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.recipe))
        },
        actions = {
            TopBarActions(
                onSearchClicked = { onSearchClicked() },
                onFavoriteRecipesClicked = {
                    navController.navigate(MainAppScreens.Favorite.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }

                }
            )

        }, backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}


@Composable
fun SearchAppBar(
    mainViewModel: MainViewModel,
    onSearch: (String) -> Unit,
//    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = query,
            onValueChange = {
//                onTextChange(it)
                query = it
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = stringResource(R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                }
            ),
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.disabled),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (query.isNotEmpty()) {
//                            onTextChange("")
                            query = ""
                            mainViewModel.searchTextState.value = ""
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
fun TopBarActions(
    onSearchClicked: () -> Unit,
    onFavoriteRecipesClicked: () -> Unit
) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Default.Search, contentDescription = "Search Button",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
    DropMenu {
        onFavoriteRecipesClicked()
    }
}

@Composable
fun DropMenu(onFavoriteRecipesClicked: () -> Unit) {
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
                onFavoriteRecipesClicked()
            }) {
                Text(
                    text = "Favorite recipes",
                    modifier = Modifier.padding(start = 5.dp),
//                    fontSize = MaterialTheme.typography.subtitle2.fontSize
                )
            }
        }
    }
}
