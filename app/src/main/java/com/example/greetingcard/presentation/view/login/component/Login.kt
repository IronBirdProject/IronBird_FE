package com.example.greetingcard.presentation.view.login.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    )

    ButtonDefaults.buttonColors(Color(0xFF87CEEB))

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 로그인 폼
        LoginTitle("철새")

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

        LoginCheckBox(
            checkboxStatus,
            onCheckedValue = { checkboxStatus = it }
        )

        LoginButtonContainer(
            navController = navController,
            userInput = userInput,
            passwordInput = passwordInput,
            authViewModel = authViewModel
        )

        LoginActionRow(navController)
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
        value = userInput,
        onValueChange = onUserInputChange,
        placeholder = {
            Text(
                text = "이메일 또는 아이디",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        },
        singleLine = true,
        modifier = paddingModifier.padding(top = 30.dp),
        colors = textFieldColors
    )

    OutlinedTextField(
        value = passwordInput,
        onValueChange = onPasswordInputChange,
        placeholder = {
            Text(
                text = "패스워드",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        },
        maxLines = 1,
        modifier = paddingModifier.padding(top = 12.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
        visualTransformation = if (shouldShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = textFieldColors
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
                onCheckedValue
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
            .padding(top = 30.dp)
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "회원가입",
            modifier = Modifier.clickable {
                navController.navigate("loginJoin")
            }
        )
        VerticalDivider(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.height(25.dp)
        )
        Text(
            text = "아이디 찾기",
            modifier = Modifier.clickable {
                navController.navigate("loginFInder/findId")
            })
        VerticalDivider(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.height(25.dp)
        )
        Text(
            text = "비밀번호 재설정",
            modifier = Modifier.clickable {
                navController.navigate("loginFinder/findPwd")
            })
    }
}