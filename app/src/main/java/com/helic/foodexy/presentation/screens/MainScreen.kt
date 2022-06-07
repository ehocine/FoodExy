package com.helic.foodexy.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.navigation.nav_graph.RootNavGraph
import com.helic.foodexy.presentation.screens.recipes.bottom_sheet.BottomSheetContent

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(mainViewModel: MainViewModel, showSnackbar: (String, SnackbarDuration) -> Unit) {
    val navController = rememberNavController()


    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(mainViewModel = mainViewModel, snackbar = showSnackbar)
        },
        sheetState = mainViewModel.modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Scaffold(
        ) {
            RootNavGraph(
                navController = navController,
                mainViewModel = mainViewModel,
                showSnackbar = showSnackbar
            )
        }
    }
}