package com.helic.foodexy.presentation.screens.recipes.bottom_sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.ui.theme.ButtonColor
import com.helic.foodexy.presentation.ui.theme.CardBorderColor
import com.helic.foodexy.presentation.ui.theme.backgroundColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(mainViewModel: MainViewModel, snackbar: (String, SnackbarDuration) -> Unit) {

    var selectedMealType by mainViewModel.selectedMealType
    var selectedDietType by mainViewModel.selectedDietType

    val scope = rememberCoroutineScope()
    val listOfMealTypes = listOf(
        "Main Course",
        "Side dish",
        "Dessert",
        "Appetizer",
        "Salad",
        "Bread",
        "Breakfast",
        "Soup",
        "Beverage",
        "Sauce",
        "Marinade",
        "Fingerfood",
        "Snack",
        "Drink"
    )
    val listOfDietTypes = listOf(
        "Gluten Free",
        "Ketogenic",
        "Vegetarian",
        "Lacto-Vegetarian",
        "Ovo-Vegetarian",
        "Vegan",
        "Pescetarian",
        "Paleo",
        "Primal",
        "Low FODMAP",
        "Whole30"
    )

    Column(Modifier.background(MaterialTheme.colors.backgroundColor)) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Meal type",
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                listOfMealTypes.forEach {
                    BottomChip(
                        title = it,
                        selected = selectedMealType,
                        onSelectedCategoryChanged = {
                            selectedMealType = it
                            mainViewModel.selectedMealType.value = it
                        })
                }
            }
        }
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Diet type",
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                listOfDietTypes.forEach {
                    BottomChip(
                        title = it,
                        selected = selectedDietType,
                        onSelectedCategoryChanged = {
                            selectedDietType = it
                            mainViewModel.selectedDietType.value = it
                        })
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    mainViewModel.getFoodRecipesList(
                        queries = mainViewModel.applyQueries(),
                        snackbar = snackbar
                    )
                    scope.launch {
                        mainViewModel.modalBottomSheetState.hide()
                    }
                    mainViewModel.saveMealAndDietType(
                        mealType = selectedMealType,
                        dietType = selectedDietType
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.ButtonColor)
            ) {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    color = Color.White
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomChip(
    title: String,
    selected: String,
    onSelectedCategoryChanged: () -> Unit,
//    onExecuteSearch: () -> Unit
) {
    Chip(
        modifier = Modifier
            .padding(2.dp),
        onClick = {
            onSelectedCategoryChanged()
        },
        border = BorderStroke(
            ChipDefaults.OutlinedBorderSize,
            MaterialTheme.colors.CardBorderColor
        ),
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary
        ),
        leadingIcon = {
            if (title == selected)
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colors.primary
                )
        }
    ) {
        Text(title)
    }
}