package com.example.greetingcard.presentation.view.login.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.greetingcard.R
import com.example.greetingcard.presentation.viewModel.login.AuthViewModel

@Composable
fun Login(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
//    val loginViewModel: LoginViewModel = viewModel()

    authViewModel.isLoading.value
    authViewModel.loginResult.value
    authViewModel.errorMessage.value

    var userInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var shouldShowPwd by remember { mutableStateOf(false) }
    var checkboxStatus by remember { mutableStateOf(false) }
    val paddingModifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),

        )

//    ButtonDefaults.buttonColors(Color(0xFF87CEEB))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Test(
//            onClick = {
//                navController.navigate("home")
//            }
//        )
//        Spacer(Modifier.height(100.dp))
        Box {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.4f),
                contentScale = ContentScale.Fit
            )
        }

        LoginForm(
            userInput = userInput,
            passwordInput = passwordInput,
            shouldShowPwd = shouldShowPwd,
            onUserInputChange = { userInput = it },
            onPasswordInputChange = { passwordInput = it },
            onPasswordVisibilityToggle = { shouldShowPwd = !shouldShowPwd },
            textFieldColors = textFieldColors,
            paddingModifier = paddingModifier
        )

        Spacer(Modifier.height(10.dp))

        // 아이디, 비밀번호 찾기 버튼
        LoginActionRow(navController)

        Spacer(Modifier.height(20.dp))

        // 로그인 버튼
        LoginButtonContainer(
            navController = navController,
            userInput = userInput,
            passwordInput = passwordInput,
            authViewModel = authViewModel
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f),
                color = Color(0xff575757).copy(alpha = 0.1f),
                thickness = 1.dp
            )
            Text(
                "계정이 없으세요?",
                color = Color(0xff575757).copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f),
                color = Color(0xff575757).copy(alpha = 0.1f),
                thickness = 1.dp
            )
        }

        Spacer(Modifier.height(20.dp))

        // 회원가입 파트
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            LoginPageButton(
                modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(Color.White),
                text = "Sign Up",
                textColor = Color(0xff575757).copy(alpha = 0.6f),
                textSize = 17,
                border = BorderStroke(
                    width = 0.7.dp,
                    color = Color(0xff575757).copy(alpha = 0.5f)
                ),
                onClickLoginBtn = {
                    navController.navigate("loginJoin")
                }
            )
            Button(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .height(75.dp)
                    .width(75.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
//                    border = BorderStroke(
//                        width = 1.dp,
//                        Color(0xff575757).copy(alpha = 0.1f)
//                    ),
                shape = RoundedCornerShape(50.dp),
                onClick = {
                    authViewModel.login(
                        username = userInput,
                        password = passwordInput,
                        onSuccess = {
                            navController.navigate("home")
                        },
                        onFailure = { message ->
                            // 로그인 실패 처리
                            // 예: Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            ) {
                Image(
                    modifier = Modifier
                        .width(55.dp)
                        .height(55.dp),
                    painter = painterResource(id = R.drawable.kakao_logo), // 예: 카카오톡 아이콘
                    contentDescription = "Kakao Login",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
fun LoginForm(
    userInput: String,
    passwordInput: String,
    shouldShowPwd: Boolean,
    onUserInputChange: (String) -> Unit,
    onPasswordInputChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit, // 함수를 의미한다.
    textFieldColors: TextFieldColors,
    paddingModifier: Modifier
) {
    OutlinedTextField(
        label = { Text("e-mail") },
        value = userInput,
        onValueChange = onUserInputChange,
        singleLine = true,
        modifier = paddingModifier
            .padding(top = 30.dp)
            .height(70.dp),
        colors = textFieldColors,
        shape = RoundedCornerShape(50),

        )

    OutlinedTextField(
        label = { Text("비밀번호") },
        value = passwordInput,
        onValueChange = { onPasswordInputChange(it) },
        maxLines = 1,
        modifier = paddingModifier
            .padding(top = 5.dp)
            .height(70.dp),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(imageVector = Icons.Default.RemoveRedEye, contentDescription = null)
            }
        },
        visualTransformation = if (shouldShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = textFieldColors,
        shape = RoundedCornerShape(50),
    )
}


@Composable
fun LoginCheckBox(
    checkboxStatus: Boolean,
    onCheckedValue: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkboxStatus,
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Black,
                checkedColor = Color(0xFF87CEEB)
            ),
            onCheckedChange = {
                onCheckedValue(it)
            }
        )
        Text(
            text = "자동 로그인",
            fontSize = 16.sp
        )
    }

}


@Composable
fun LoginActionRow(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
//        Text(
//            text = "회원가입",
//            modifier = Modifier.clickable {
//                navController.navigate("loginJoin")
//            }
//        )
//        VerticalDivider(
//            color = MaterialTheme.colorScheme.secondary,
//            modifier = Modifier.height(25.dp)
//        )
        Text(
            fontSize = 13.sp,
            color = Color(0xff595959),
            text = "아이디 찾기",
            modifier = Modifier.clickable {
                navController.navigate("loginFInder/findId")
            })
        Spacer(modifier = Modifier.width(10.dp))
        VerticalDivider(
            color = Color(0xff595959),
            modifier = Modifier.height(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            fontSize = 14.sp,
            color = Color(0xff595959),
            text = "비밀번호 찾기",
            modifier = Modifier.clickable {
                navController.navigate("loginFinder/findPwd")
            })
    }
}
