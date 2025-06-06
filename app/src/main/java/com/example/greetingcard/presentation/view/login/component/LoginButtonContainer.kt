package com.example.greetingcard.presentation.view.login.component

import CustomAlertDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greetingcard.presentation.viewModel.login.AuthViewModel
import com.example.greetingcard.presentation.viewModel.login.kakao.KakaoViewModel


@Composable
fun LoginButtonContainer(
    navController: NavController,
    userInput: String,
    passwordInput: String,
    authViewModel: AuthViewModel,

    ) {
    val kakaoViewModel: KakaoViewModel = viewModel()
    val context = LocalContext.current

//    val loginButtonColor = ButtonDefaults.buttonColors(Col
//    or(0xFF87CEEB))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // 자체 로그인
        LoginPageButton(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xff6ec4ee)),
            text = "로그인",
            textColor = Color.White,
            textSize = 20,
            border = null,
            onClickLoginBtn = {
                authViewModel.login(
                    username = userInput,
                    password = passwordInput,
                    onSuccess = {
                        navController.navigate("home")
                    },
                    onFailure = { message ->
                        // 로그인 실패 처리
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    })
            })

//        KakaoButton(
//            kakaoViewModel = kakaoViewModel,
//            navController = navController,
//        )


    }
}

@Composable
fun Test(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .width(100.dp)
            .height(30.dp)
            .padding(top = 10.dp)
            .padding(start = 20.dp, end = 20.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),
        shape = RoundedCornerShape(15.dp),
        onClick = { onClick() }) {

    }
}

@Composable
fun LoginPageButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    onClickLoginBtn: () -> Unit,
    text: String,
    textColor: Color,
    textSize: Int,
    border: BorderStroke?
) {

    OutlinedButton(
        onClick = {
            onClickLoginBtn()
        },
        modifier
//            .fillMaxWidth()
            .height(75.dp),
        colors = colors,
        border = border,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = text, fontSize = textSize.sp, color = textColor
        )
    }
}


@Composable
fun KakaoButton(
    kakaoViewModel: KakaoViewModel, navController: NavController
) {
    var loginSuccess by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    Button(
        onClick = {
            loginSuccess = kakaoViewModel.handleKakaoLogin(navController = navController)
            if (loginSuccess) {
                showDialog = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 10.dp)
            .padding(start = 20.dp, end = 20.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFFFEE500)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(
            text = "카카오 로그인 하기", fontSize = 20.sp, color = Color.Black
        )
    }

    // 로그인 성공 시 다이얼로그 표시
    CustomAlertDialog(showDialog = showDialog, onDismiss = { showDialog = false }, navController)
}

@Composable
fun NaverButton(
    viewModel: KakaoViewModel, navController: NavController
) {
    Column {
        Button(onClick = {
            viewModel.handleKakaoLogin(navController = navController)
        }) {
            Text("카카오 로그인 하기")
        }
        Button(onClick = {}) {
            Text("카카오 로그아웃 하기")
        }
    }
}