package com.example.greetingcard.presentation.view.login.component

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.greetingcard.data.model.dto.user.UserDTO
import com.example.greetingcard.presentation.view.login.view.CustomInputField
import com.example.greetingcard.presentation.viewModel.login.LoginViewModel

@Composable
fun LoginJoin(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val loginViewModel: LoginViewModel = viewModel()

    Column {
        LoginTitle("회원가입")
        JoinForm(navController, loginViewModel)
    }
}

@Composable
fun JoinForm(
    navController: NavController,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var userId by remember { mutableStateOf("") }
        var userName by remember { mutableStateOf("") }
        var userEmail by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var shouldShowPwd by remember { mutableStateOf(false) }

        // Validation
        var isUserIdVaild by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isNameVaild by remember { mutableStateOf(true) }
        var isEmailVaild by remember { mutableStateOf(true) }

        val focusRequester = remember { FocusRequester() }



        Column(
            modifier = modifier
                .padding(top = 30.dp)
                .fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomInputField(
                value = userId,
                onValueChange = {
                    userId = it
                    isUserIdVaild = userId.length > 4
                },
                placeholderText = "아이디",
                leadingIcon = Icons.Default.Person,
                modifier = modifier.focusRequester(focusRequester)
            )

            if (!isUserIdVaild) {
                Text("아이디는 최소 4자 이상이어야 합니다.", color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordValid = password.length >= 6
                },
                placeholder = { Text("비밀번호") },
                maxLines = 1,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { shouldShowPwd = !shouldShowPwd }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                },
                modifier = modifier.fillMaxWidth(0.9f),
                visualTransformation = if (shouldShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            if (!isPasswordValid) {
                Text("비밀번호는 최소 6자 이상이어야 합니다.", color = MaterialTheme.colorScheme.error)
            }

            CustomInputField(
                value = userName,
                onValueChange = {
                    userName = it
                    isNameVaild = userName.length >= 2
                },
                placeholderText = "이름",
                leadingIcon = Icons.Default.Person,
            )

            if (!isNameVaild) {
                Text("비밀번호는 최소 6자 이상이여")
            }

            CustomInputField(
                value = userEmail,
                onValueChange = {
                    userEmail = it
                    isEmailVaild = Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
                },
                placeholderText = "[선택] 이메일주소 (비밀번호 찾기 등 본인 확인용)",
                leadingIcon = Icons.Default.Email
            )
            if (!isEmailVaild) {
                Text("유요한 이메일 주소를 입력하세요.", color = MaterialTheme.colorScheme.error)
            }
        }


        SetProfile()


        Button(
            modifier = modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.9f)
                .height(50.dp), // 버튼 크기 조정
            onClick = {
                if (isUserIdVaild && isPasswordValid && isNameVaild) {
                    // UserDTO 객체 생성
                    Log.d("isUserIdVaild", isUserIdVaild.toString())
                    val userDTO = UserDTO.from(userId, userName, password, userEmail)
                    /// TODO: join API 호출
//                    loginViewModel.join(userDTO)
                } else {
                    focusRequester.requestFocus()
                }
            },
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "회원가입",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }


        Button(
            modifier = modifier.padding(top = 30.dp),
            onClick = { navController.navigate("login") }) {
            Text("Go to Home")
        }
    }
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetProfile() {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        showBottomSheet = false
    }



    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 15.dp)
            .height(50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            ) {
                selectedImageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: run {
                    Icon(
                        imageVector = Icons.Filled.AddAPhoto,
                        contentDescription = "Add Profile Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        tint = Color.Gray
                    )
                }
            }

            OutlinedButton(
                onClick = { showBottomSheet = !showBottomSheet },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "프로필 선택"
                )
            }
        }


        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text("이미지 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("갤러리 열기")
                }
            }
        }
    }
}

