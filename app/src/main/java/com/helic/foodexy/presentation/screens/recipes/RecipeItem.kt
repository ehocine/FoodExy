package com.helic.foodexy.presentation.screens.recipes

import android.text.TextUtils
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.helic.foodexy.data.network.models.FoodRecipeResult
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.navigation.MainAppScreens
import com.helic.foodexy.presentation.ui.theme.*

@Composable
fun RecipeItem(
    navController: NavController,
    mainViewModel: MainViewModel,
    foodRecipeResult: FoodRecipeResult
) {
    val darkTheme = isSystemInDarkTheme()
    Card(
        modifier = Modifier
            .padding(top = 5.dp, end = 10.dp, start = 10.dp, bottom = 5.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.CardBorderColor,
                shape = RoundedCornerShape(5.dp)
            )
            .fillMaxWidth()
            .height(cardHeight)
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                //Navigate to recipe details
                mainViewModel.selectedRecipe.value = foodRecipeResult
                navController.navigate(MainAppScreens.RecipeDetails.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
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
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = foodRecipeResult.title,
                        fontFamily = fancyFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.CardTitleColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
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
                            it.maxLines = 4
                            it.ellipsize = TextUtils.TruncateAt.END
                        }
                    )
//                    Text(
//                        text = foodRecipeResult.summary,
//                        fontSize = MaterialTheme.typography.body1.fontSize,
//                        color = MaterialTheme.colors.CardDesColor,
//                        maxLines = 4,
//                        overflow = TextOverflow.Ellipsis
//                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Recipe's likes",
                            tint = Red
                        )
                        Text(
                            text = "${foodRecipeResult.aggregateLikes}",
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            color = Red
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Recipe's time",
                            tint = Yellow
                        )
                        Text(
                            text = "${foodRecipeResult.readyInMinutes}",
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            color = Yellow
                        )
                    }
                    val veganColor = if (foodRecipeResult.vegan) Green else MediumGray
                    val isVegan = if (foodRecipeResult.vegan) "Vegan" else "Not vegan"
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Recipe's vegan",
                            tint = veganColor
                        )
                        Text(
                            text = isVegan,
                            fontSize = MaterialTheme.typography.subtitle2.fontSize,
                            color = veganColor
                        )
                    }
                }
            }
        }
    }
}