import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.greetingcard.presentation.navigation.Screen
import com.example.greetingcard.presentation.view.home.HomePage
import com.example.greetingcard.presentation.view.home.posting.CreatePostPage
import com.example.greetingcard.presentation.view.login.component.Login
import com.example.greetingcard.presentation.view.login.component.LoginFinder
import com.example.greetingcard.presentation.view.login.component.LoginJoin
import com.example.greetingcard.presentation.view.my_info.MyPlanScreen
import com.example.greetingcard.presentation.view.plan.createplan.CalendarScreen
import com.example.greetingcard.presentation.view.plan.plandetail.PlanDetailScreen
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel
import com.example.greetingcard.presentation.viewModel.home.PostViewModel
import com.example.greetingcard.presentation.viewModel.plandetail.PlanDetailViewModel

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            Login(navController = navController)
        }
        composable(Screen.LoginFinder.route) { backStackEntry ->
            val viewType = backStackEntry.arguments?.getString("viewType") ?: "findId"
            LoginFinder(navController = navController, viewType = viewType)
        }
        composable(Screen.LoginJoin.route) {
            LoginJoin(navController = navController)
        }
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomePage(navController = navController, homeViewModel = homeViewModel)
        }
        composable(Screen.Calender.route) {
            CalendarScreen(navController = navController)
        }
        composable(Screen.SelectDestination.route) {
            val postViewModel: PostViewModel = viewModel()
            TravelDestinationScreen(navController = navController, postViewModel = postViewModel)
        }
        composable(Screen.CreatePost.route) {
            val postViewModel: PostViewModel = viewModel()
            CreatePostPage(
                navController = navController,
                postViewModel = postViewModel,
            )
        }
        // 내 플랜 화면
        composable(Screen.MyPlan.route) {
            MyPlanScreen(navController = navController)
        }
        // 플랜 상세 화면
        composable(
            Screen.DetailPlan.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val planDetailViewModel: PlanDetailViewModel = viewModel()
            val planId: Int? = backStackEntry.arguments?.getInt("id")
            PlanDetailScreen(planId = planId, planDetailViewModel = planDetailViewModel)
        }
    }
}
