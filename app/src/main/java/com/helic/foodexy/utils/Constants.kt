package com.helic.foodexy.utils

object Constants {

    const val ROOT_ROUTE = "root"

    const val TIMEOUT_IN_MILLIS = 10000L

    //API Query
    const val QUERY_SEARCH = "query"
    const val QUERY_NUMBER = "number"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_TYPE = "type"
    const val QUERY_DIET = "diet"
    const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
    const val QUERY_FILL_INGREDIENTS = "fillIngredients"

    // ROOM Database
    const val DATABASE_NAME = "recipes_database"
    const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"

    // Bottom Sheet and Preferences
    const val DEFAULT_RECIPES_NUMBER = "50"
    const val DEFAULT_MEAL_TYPE = "Main Course"
    const val DEFAULT_DIET_TYPE = "Gluten Free"

    const val PREFERENCES_NAME = "foodExy_preferences"
    const val PREFERENCES_MEAL_TYPE = "mealType"
    const val PREFERENCES_DIET_TYPE = "dietType"

    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY = "133483b1029f4a3caa9331817c5cc076"
}