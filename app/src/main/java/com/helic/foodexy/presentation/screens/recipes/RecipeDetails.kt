package com.helic.foodexy.presentation.screens.recipes

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.helic.foodexy.R
import com.helic.foodexy.data.database.FavoriteRecipesEntity
import com.helic.foodexy.data.network.models.FoodRecipeResult
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.navigation.MainAppScreens
import com.helic.foodexy.presentation.ui.theme.*
import com.helic.foodexy.utils.showInterstitial

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecipeDetails(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val context = LocalContext.current
    val selectedRecipe by mainViewModel.selectedRecipe

    mainViewModel.readFavoriteRecipes()

    val favoriteRecipesList by mainViewModel.favoriteRecipesList.collectAsState()

    var recipeSaved by remember { mutableStateOf(false) }

    val filteredList = favoriteRecipesList.filter {
        it.foodRecipeResult.recipeId == selectedRecipe.recipeId
    }

    if (filteredList.isNotEmpty()) recipeSaved = true

    Scaffold(topBar = {
        RecipeDetailsTopAppBar(
            navController = navController,
            mainViewModel = mainViewModel,
            selectedRecipe = selectedRecipe,
            recipeSaved = recipeSaved,
            snackbar = snackbar
        )
    }) {
        RecipeDetailsContent(foodRecipeResult = selectedRecipe)
        showInterstitial(context)
    }
}

@Composable
fun RecipeDetailsContent(foodRecipeResult: FoodRecipeResult) {
    val darkTheme = isSystemInDarkTheme()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(foodRecipeResult.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Recipe Image"
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.clip(RectangleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Row(
                Modifier
                    .align(Alignment.TopCenter)
                    .background(Transparent)
            ) {
                // shows a traditional banner test ad
                AndroidView(
                    factory = { context ->
                        AdView(context).apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = context.getString(R.string.ad_id_banner)
                            loadAd(AdRequest.Builder().build())
                        }
                    }
                )
            }
            Row(
                Modifier
                    .align(Alignment.BottomEnd)
                    .background(BlackWithAlpha)
                    .padding(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Recipe's likes",
                        tint = White
                    )
                    Text(
                        text = "${foodRecipeResult.aggregateLikes}",
                        fontSize = MaterialTheme.typography.subtitle2.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Recipe's time",
                        tint = White
                    )
                    Text(
                        text = "${foodRecipeResult.readyInMinutes}",
                        fontSize = MaterialTheme.typography.subtitle2.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Column(
                Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = foodRecipeResult.title,
                    fontFamily = fancyFont,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.CardTitleColor
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    val vegetarianColor: Color
                    val iconVegetarian: ImageVector

                    if (foodRecipeResult.vegetarian) {
                        vegetarianColor = Green
                        iconVegetarian = Icons.Default.CheckCircle
                    } else {
                        vegetarianColor = Red
                        iconVegetarian = Icons.Default.Cancel
                    }

                    val glutenFreeColor: Color
                    val iconGlutenFree: ImageVector

                    if (foodRecipeResult.glutenFree) {
                        glutenFreeColor = Green
                        iconGlutenFree = Icons.Default.CheckCircle
                    } else {
                        glutenFreeColor = Red
                        iconGlutenFree = Icons.Default.Cancel
                    }

                    val healthyColor: Color
                    val iconHealthy: ImageVector

                    if (foodRecipeResult.veryHealthy) {
                        healthyColor = Green
                        iconHealthy = Icons.Default.CheckCircle
                    } else {
                        healthyColor = Red
                        iconHealthy = Icons.Default.Cancel
                    }

                    val veganColor: Color
                    val iconVegan: ImageVector

                    if (foodRecipeResult.vegan) {
                        veganColor = Green
                        iconVegan = Icons.Default.CheckCircle
                    } else {
                        veganColor = Red
                        iconVegan = Icons.Default.Cancel
                    }

                    val dairyFreeColor: Color
                    val iconDairyFree: ImageVector

                    if (foodRecipeResult.dairyFree) {
                        dairyFreeColor = Green
                        iconDairyFree = Icons.Default.CheckCircle
                    } else {
                        dairyFreeColor = Red
                        iconDairyFree = Icons.Default.Cancel
                    }

                    val cheapColor: Color
                    val iconCheap: ImageVector

                    if (foodRecipeResult.cheap) {
                        cheapColor = Green
                        iconCheap = Icons.Default.CheckCircle
                    } else {
                        cheapColor = Red
                        iconCheap = Icons.Default.Cancel
                    }

                    Column {
                        Row {
                            Icon(
                                imageVector = iconVegetarian,
                                contentDescription = "",
                                tint = vegetarianColor
                            )
                            Text(text = "Vegetarian", color = vegetarianColor)
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row {
                            Icon(
                                imageVector = iconVegan,
                                contentDescription = "",
                                tint = veganColor
                            )
                            Text(text = "Vegan", color = veganColor)
                        }
                    }
                    Column {
                        Row {
                            Icon(
                                imageVector = iconGlutenFree,
                                contentDescription = "",
                                tint = glutenFreeColor
                            )
                            Text(text = "Gluten free", color = glutenFreeColor)
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row {
                            Icon(
                                imageVector = iconDairyFree,
                                contentDescription = "",
                                tint = dairyFreeColor
                            )
                            Text(text = "Dairy free", color = dairyFreeColor)
                        }
                    }
                    Column {
                        Row {
                            Icon(
                                imageVector = iconHealthy,
                                contentDescription = "",
                                tint = healthyColor
                            )
                            Text(text = "Healthy", color = healthyColor)
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row {
                            Icon(
                                imageVector = iconCheap,
                                contentDescription = "",
                                tint = cheapColor
                            )
                            Text(text = "Cheap", color = cheapColor)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    // Used instead of Text to properly handle the HTML tags
                    AndroidView(
                        factory = { context -> TextView(context) },
                        update = {
                            it.text = HtmlCompat.fromHtml(
                                foodRecipeResult.summary,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            )
                            it.setTextColor(if (darkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
                            it.textSize = 16f
                        }
                    )
//                    Text(
//                        text = foodRecipeResult.summary,
//                        fontSize = 16.sp,
//                        color = MaterialTheme.colors.CardDesColor
//                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailsTopAppBar(
    navController: NavController,
    mainViewModel: MainViewModel,
    selectedRecipe: FoodRecipeResult,
    recipeSaved: Boolean,
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
        title = { Text(text = stringResource(R.string.recipe_details)) },
        actions = {
            IconButton(onClick = {
                if (!recipeSaved) {
                    mainViewModel.insertFavoriteRecipe(
                        FavoriteRecipesEntity(0, selectedRecipe)
                    )
                    snackbar("Added successfully", SnackbarDuration.Short)
                } else {
                    snackbar("Recipe already saved", SnackbarDuration.Short)
                }

            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = if (recipeSaved) Yellow else MediumGray
                )
            }
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}