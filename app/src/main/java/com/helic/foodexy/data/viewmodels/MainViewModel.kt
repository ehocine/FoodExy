package com.helic.foodexy.data.viewmodels

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.helic.foodexy.R
import com.helic.foodexy.data.database.FavoriteRecipesEntity
import com.helic.foodexy.data.network.models.FoodRecipeResult
import com.helic.foodexy.data.repository.DataStoreRepository
import com.helic.foodexy.data.repository.Repository
import com.helic.foodexy.utils.Constants.API_KEY
import com.helic.foodexy.utils.Constants.DEFAULT_DIET_TYPE
import com.helic.foodexy.utils.Constants.DEFAULT_MEAL_TYPE
import com.helic.foodexy.utils.Constants.DEFAULT_RECIPES_NUMBER
import com.helic.foodexy.utils.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.helic.foodexy.utils.Constants.QUERY_API_KEY
import com.helic.foodexy.utils.Constants.QUERY_DIET
import com.helic.foodexy.utils.Constants.QUERY_FILL_INGREDIENTS
import com.helic.foodexy.utils.Constants.QUERY_NUMBER
import com.helic.foodexy.utils.Constants.QUERY_SEARCH
import com.helic.foodexy.utils.Constants.QUERY_TYPE
import com.helic.foodexy.utils.Constants.TIMEOUT_IN_MILLIS
import com.helic.foodexy.utils.LoadingState
import com.helic.foodexy.utils.SearchAppBarState
import com.helic.foodexy.utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE */

    val selectedFavoriteRecipeEntity: MutableState<FavoriteRecipesEntity> = mutableStateOf(
        FavoriteRecipesEntity()
    )

    private var _favoriteRecipesList = MutableStateFlow<List<FavoriteRecipesEntity>>(emptyList())
    val favoriteRecipesList: StateFlow<List<FavoriteRecipesEntity>> = _favoriteRecipesList

    fun readFavoriteRecipes() {
        viewModelScope.launch {
            repository.local.readFavoriteRecipes().collect {
                _favoriteRecipesList.emit(it)
            }
        }
    }

    fun insertFavoriteRecipe(favoritesEntity: FavoriteRecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoriteRecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {

            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipesFromDB() {
        viewModelScope.launch {
            repository.local.deleteAllFavoriteRecipes()
        }
    }


    var selectedMealType: MutableState<String> = mutableStateOf(DEFAULT_MEAL_TYPE)
    var selectedDietType: MutableState<String> = mutableStateOf(DEFAULT_DIET_TYPE)

    private val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(
        mealType: String,
        dietType: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealType = mealType,
                dietType = dietType
            )
        }
    }
    init {
        viewModelScope.launch {
            readMealAndDietType.collect {
                selectedMealType.value = it.selectedMealType
                selectedDietType.value = it.selectedDietType
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    val modalBottomSheetState: ModalBottomSheetState =
        ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    val foodRecipesListResponse: MutableState<List<FoodRecipeResult>> =
        mutableStateOf(listOf())

    val searchFoodRecipesListResponse: MutableState<List<FoodRecipeResult>> =
        mutableStateOf(listOf())

    val selectedRecipe: MutableState<FoodRecipeResult> = mutableStateOf(FoodRecipeResult())

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_TYPE] = selectedMealType.value.lowercase(Locale.getDefault())
        queries[QUERY_DIET] = selectedDietType.value.lowercase(Locale.getDefault())
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "false"
        queries[QUERY_API_KEY] = API_KEY
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    var getFoodRecipesListLoadingState = MutableStateFlow(LoadingState.IDLE)
    fun getFoodRecipesList(
        queries: Map<String, String>,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        viewModelScope.launch {
            if (hasInternetConnection(getApplication<Application>())) {
                try {
                    withTimeoutOrNull(TIMEOUT_IN_MILLIS) {
                        this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.LOADING)
                        val response = repository.remote.getRecipesList(queries = queries)
                        if (response.isSuccessful) {
                            foodRecipesListResponse.value = response.body()!!.foodRecipeResults
                            this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.LOADED)
                        } else {
                            this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    } ?: withContext(Dispatchers.Main) {
                        this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                        snackbar(
                            getApplication<Application>().getString(R.string.connection_time_out),
                            SnackbarDuration.Short
                        )
                    }
                } catch (e: Exception) {
                    this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                    snackbar(
                        getApplication<Application>().getString(R.string.error_occurred),
                        SnackbarDuration.Short
                    )
                }
            } else {
                this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                snackbar(
                    getApplication<Application>().getString(R.string.device_not_connected_internet),
                    SnackbarDuration.Short
                )
            }
        }
    }

    fun searchRecipes(
        query: Map<String, String>,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        viewModelScope.launch {
            if (hasInternetConnection(getApplication<Application>())) {
                try {
                    withTimeoutOrNull(TIMEOUT_IN_MILLIS) {
                        this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.LOADING)
                        val response = repository.remote.searchRecipes(searchQuery = query)
                        if (response.isSuccessful) {
                            searchFoodRecipesListResponse.value =
                                response.body()!!.foodRecipeResults
                            this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.LOADED)
                        } else {
                            this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    } ?: withContext(Dispatchers.Main) {
                        this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                        snackbar(
                            getApplication<Application>().getString(R.string.connection_time_out),
                            SnackbarDuration.Short
                        )
                    }
                } catch (e: Exception) {
                    this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                    snackbar(
                        getApplication<Application>().getString(R.string.error_occurred),
                        SnackbarDuration.Short
                    )
                }
            } else {
                this@MainViewModel.getFoodRecipesListLoadingState.emit(LoadingState.ERROR)
                snackbar(
                    getApplication<Application>().getString(R.string.device_not_connected_internet),
                    SnackbarDuration.Short
                )
            }
        }
    }
}