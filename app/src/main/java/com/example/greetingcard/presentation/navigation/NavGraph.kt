import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.greetingcard.presentation.navigation.PlanCreateNavGraph
import com.example.greetingcard.presentation.navigation.Screen
import com.example.greetingcard.presentation.view.home.HomePage
import com.example.greetingcard.presentation.view.home.planning.MapTestScreen
import com.example.greetingcard.presentation.view.home.posting.CreatePostPage
import com.example.greetingcard.presentation.view.login.component.Login
import com.example.greetingcard.presentation.view.login.component.LoginFinder
import com.example.greetingcard.presentation.view.login.component.LoginJoin
import com.example.greetingcard.presentation.view.my_info.MyPlanScreen
import com.example.greetingcard.presentation.view.plan.plandetail.PlanDetailScreen
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel
import com.example.greetingcard.presentation.viewModel.home.PostViewModel
import com.example.greetingcard.presentation.viewModel.login.AuthViewModel
import com.example.greetingcard.presentation.viewModel.plan.PlanPreviewViewModel
import com.example.greetingcard.presentation.viewModel.plan.plandetail.PlanDetailViewModel

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController, startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            Login(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.LoginFinder.route) { backStackEntry ->
            val viewType = backStackEntry.arguments?.getString("viewType") ?: "findId"
            LoginFinder(navController = navController, viewType = viewType)
        }
        composable(Screen.LoginJoin.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginJoin(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomePage(navController = navController, homeViewModel = homeViewModel)
        }
//        composable(Screen.Calender.route) {
//            val planCreateViewModel: PlanCreateViewModel = viewModel()
//            CalendarScreen(navController = navController, planCreateViewModel = planCreateViewModel)
//        }
//        composable(Screen.SelectDestination.route) {
//            val planCreateViewModel: PlanCreateViewModel = viewModel()
//            TravelDestinationScreen(
//                navController = navController,
//                planViewModel = planCreateViewModel
//            )
//        }
        composable(Screen.PlanCreate.route) {
            PlanCreateNavGraph(rootNavController = navController)
        }


        composable(Screen.CreatePost.route) {
            val postViewModel: PostViewModel = hiltViewModel()
            CreatePostPage(
                navController = navController,
                postViewModel = postViewModel,
            )
        }
        // 내 플랜 화면
        composable(Screen.MyPlan.route) {
            val planPreviewViewModel: PlanPreviewViewModel = hiltViewModel()
            MyPlanScreen(navController = navController, planPreviewViewModel = planPreviewViewModel)
        }
        // 플랜 상세 화면
        composable(
            Screen.DetailPlan.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val planDetailViewModel: PlanDetailViewModel = hiltViewModel()
            val planId: Int? = backStackEntry.arguments?.getInt("id")
            PlanDetailScreen(
                planId = planId,
                planDetailViewModel = planDetailViewModel,
                navController = navController
            )
        }

        composable(
            Screen.MapTest.route
        ) { MapTestScreen() }
    }
}
