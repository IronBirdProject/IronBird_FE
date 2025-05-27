package com.example.greetingcard

import SetUpNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.presentation.ui.theme.GreetingCardTheme
import com.example.greetingcard.presentation.viewModel.login.kakao.KakaoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val kakaoAuthViewModel: KakaoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GreetingCardTheme {
                val navController = rememberNavController()
                SetUpNavGraph(navController = navController)

            }
        }
    }
}

