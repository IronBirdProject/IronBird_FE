package com.example.greetingcard.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen(route = "splash_screen")

    object Login : Screen(route = "login")
    object LoginFinder : Screen(route = "loginFinder/{viewType}")
    object LoginJoin : Screen(route = "loginJoin")

    object Home : Screen(route = "home")

    object PlanCreate : Screen(route = "plan_create")
    object Calender : Screen(route = "plan_calendar")
    object SelectDestination : Screen(route = "plan_destination")
    object PlanTitle : Screen(route = "plan_title")

    object CreatePost : Screen(route = "create_post")

    object MyPlan : Screen(route = "my_plan") // 내 플랜 스크린
    object PlanDetail : Screen(route = "plan_detail/{id}")

    object MapTest : Screen(route = "map_test")
}

