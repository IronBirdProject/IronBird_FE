import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.greetingcard.presentation.navigation.Screen
import com.example.greetingcard.presentation.ui.home.HomePage
import com.example.greetingcard.presentation.ui.home.planning.CreatePlanPage
import com.example.greetingcard.presentation.ui.home.posting.CreatePostPage
import com.example.greetingcard.presentation.ui.login.component.Login
import com.example.greetingcard.presentation.ui.login.component.LoginFinder
import com.example.greetingcard.presentation.ui.login.component.LoginJoin
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel
import com.example.greetingcard.presentation.viewModel.home.PostViewModel

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
        composable(Screen.CreatePlan.route) {
            CreatePlanPage()
        }
        composable(Screen.CreatePost.route) {
            val postViewModel: PostViewModel = viewModel()
            CreatePostPage(
                navController = navController,
                postViewModel = postViewModel,
            )
        }
    }
}
