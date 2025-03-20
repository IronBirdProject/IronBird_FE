package com.example.greetingcard.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen(route = "login")
    object LoginFinder : Screen(route = "loginFinder/{viewType}")
    object LoginJoin : Screen(route = "loginJoin")

    object Splash : Screen(route = "splash_screen")
    object Home : Screen(route = "home")
    object CreatePlan : Screen(route = "create_plan")
    object CreatePost : Screen(route = "create_post")
}

