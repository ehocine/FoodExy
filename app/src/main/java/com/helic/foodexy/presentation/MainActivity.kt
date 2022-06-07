package com.helic.foodexy.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.helic.foodexy.data.viewmodels.MainViewModel
import com.helic.foodexy.presentation.screens.MainScreen
import com.helic.foodexy.presentation.ui.theme.FoodExyTheme
import com.helic.foodexy.presentation.ui.theme.Purple700
import com.helic.foodexy.utils.MSnackbar
import com.helic.foodexy.utils.loadInterstitial
import com.helic.foodexy.utils.rememberSnackbarState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize the mobile ads sdk
        MobileAds.initialize(this) {}

        setContent {
            FoodExyTheme {
                val systemUiController = rememberSystemUiController()
                val darkTheme = isSystemInDarkTheme()
                navController = rememberNavController()
                val appState: MSnackbar = rememberSnackbarState()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = if (darkTheme) Color.Black else Purple700
                    )
                }
                Scaffold(
                    scaffoldState = appState.scaffoldState
                ) {
                    MainScreen(
                        mainViewModel = mainViewModel,
                        showSnackbar = { message, duration ->
                            appState.showSnackbar(message = message, duration = duration)
                        })
                }
            }
        }
        loadInterstitial(this)
    }
}