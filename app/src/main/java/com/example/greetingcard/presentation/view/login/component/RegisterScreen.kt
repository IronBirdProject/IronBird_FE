package com.example.greetingcard.presentation.view.login.component

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.greetingcard.presentation.viewModel.login.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 로고
//        Image(
//            painter = painterResource(id = R.drawable.app_logo),
//            contentDescription = "App Logo",
//            modifier = Modifier
//                .fillMaxWidth(0.4f)
//                .padding(bottom = 20.dp),
//            contentScale = ContentScale.Fit
//        )

        // 프로필 사진 선택
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "프로필 이미지 선택",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

//        // 입력 필드들
//        JoinTextField(
//            label = "아이디",
//            value = userId,
//            onValueChange = { userId = it },
//            colors = textFieldColors
//        )
        JoinTextField(
            label = "이메일",
            value = email,
            onValueChange = { email = it },
            colors = textFieldColors
        )

        JoinTextField(
            label = "비밀번호",
            value = password,
            onValueChange = { password = it },
            colors = textFieldColors,
            password = true
        )
        JoinTextField(
            label = "이름",
            value = name,
            onValueChange = { name = it },
            colors = textFieldColors
        )


        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                Log.d(
                    "RegisterScreen",
                    "회원가입 시도: 아이디=$userId, 비밀번호=$password, 이름=$name, 이메일=$email, 프로필 이미지=${profileImageUri?.toString()}"
                )
                // 회원가입 로직 처리
                authViewModel.register(
                    email = email,
                    password = password,
                    name = name,
                    defaultProfilePic = profileImageUri?.toString() ?: "",
                    onSuccess = {
                        // 회원가입 성공 후 처리
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                        Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { error ->
                        // 회원가입 실패 처리
                        // 예: Toast 메시지 표시
                        println("회원가입 실패: $error")
                        Toast.makeText(context, "회원가입 실패: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF87CEEB))
        ) {
            Text("회원가입", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("로그인 화면으로 돌아가기", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun JoinTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    password: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(65.dp),
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (password) KeyboardType.Password else KeyboardType.Text
        ),
        shape = RoundedCornerShape(50.dp),
        colors = colors
    )
}


//@Composable
//fun LoginJoin(
//    modifier: Modifier = Modifier,
//    navController: NavController,
//    authViewModel: AuthViewModel
//) {
//
//    Column {
//        LoginTitle("회원가입")
//        JoinForm(navController, authViewModel)
//    }
//}
//
//@Composable
//fun JoinForm(
//    navController: NavController,
//    authViewModel: AuthViewModel,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier
//            .fillMaxWidth()
//            .fillMaxHeight(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        var userId by remember { mutableStateOf("") }
//        var userName by remember { mutableStateOf("") }
//        var userEmail by remember { mutableStateOf("") }
//        var password by remember { mutableStateOf("") }
//        var shouldShowPwd by remember { mutableStateOf(false) }
//
//        // Validation
//        var isUserIdVaild by remember { mutableStateOf(true) }
//        var isPasswordValid by remember { mutableStateOf(true) }
//        var isNameVaild by remember { mutableStateOf(true) }
//        var isEmailVaild by remember { mutableStateOf(true) }
//
//        val focusRequester = remember { FocusRequester() }
//
//
//
//        Column(
//            modifier = modifier
//                .padding(top = 30.dp)
//                .fillMaxWidth(1f),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            CustomInputField(
//                value = userId,
//                onValueChange = {
//                    userId = it
//                    isUserIdVaild = userId.length > 4
//                },
//                placeholderText = "아이디",
//                leadingIcon = Icons.Default.Person,
//                modifier = modifier.focusRequester(focusRequester)
//            )
//
//            if (!isUserIdVaild) {
//                Text("아이디는 최소 4자 이상이어야 합니다.", color = MaterialTheme.colorScheme.error)
//            }
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = {
//                    password = it
//                    isPasswordValid = password.length >= 6
//                },
//                placeholder = { Text("비밀번호") },
//                maxLines = 1,
//                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                trailingIcon = {
//                    IconButton(onClick = { shouldShowPwd = !shouldShowPwd }) {
//                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
//                    }
//                },
//                modifier = modifier.fillMaxWidth(0.9f),
//                visualTransformation = if (shouldShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
//            )
//
//            if (!isPasswordValid) {
//                Text("비밀번호는 최소 6자 이상이어야 합니다.", color = MaterialTheme.colorScheme.error)
//            }
//
//            CustomInputField(
//                value = userName,
//                onValueChange = {
//                    userName = it
//                    isNameVaild = userName.length >= 2
//                },
//                placeholderText = "이름",
//                leadingIcon = Icons.Default.Person,
//            )
//
//            if (!isNameVaild) {
//                Text("비밀번호는 최소 6자 이상이여")
//            }
//
//            CustomInputField(
//                value = userEmail,
//                onValueChange = {
//                    userEmail = it
//                    isEmailVaild = Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
//                },
//                placeholderText = "[선택] 이메일주소 (비밀번호 찾기 등 본인 확인용)",
//                leadingIcon = Icons.Default.Email
//            )
//            if (!isEmailVaild) {
//                Text("유요한 이메일 주소를 입력하세요.", color = MaterialTheme.colorScheme.error)
//            }
//        }
//
//
//        SetProfile()
//
//
//        Button(
//            modifier = modifier
//                .padding(top = 30.dp)
//                .fillMaxWidth(0.9f)
//                .height(50.dp), // 버튼 크기 조정
//            onClick = {
//                if (isUserIdVaild && isPasswordValid && isNameVaild) {
//                    // UserDTO 객체 생성
//                    Log.d("isUserIdVaild", isUserIdVaild.toString())
//                    val userDTO = UserDTO.from(userId, userName, password, userEmail)
//                    /// TODO: join API 호출
////                    loginViewModel.join(userDTO)
//                } else {
//                    focusRequester.requestFocus()
//                }
//            },
//            shape = RoundedCornerShape(12.dp),
//        ) {
//            Text(
//                text = "회원가입",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//
//        Button(
//            modifier = modifier.padding(top = 30.dp),
//            onClick = { navController.navigate("login") }) {
//            Text("Go to Home")
//        }
//    }
//}
//
//
//@Preview
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SetProfile() {
//    var showBottomSheet by remember { mutableStateOf(false) }
//    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        selectedImageUri = uri
//        showBottomSheet = false
//    }
//
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(0.9f)
//            .padding(top = 15.dp)
//            .height(50.dp)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(2.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(50.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//            ) {
//                selectedImageUri?.let { uri ->
//                    Image(
//                        painter = rememberAsyncImagePainter(uri),
//                        contentDescription = "Selected Image",
//                        modifier = Modifier.fillMaxSize()
//                    )
//                } ?: run {
//                    Icon(
//                        imageVector = Icons.Filled.AddAPhoto,
//                        contentDescription = "Add Profile Image",
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .align(Alignment.Center),
//                        tint = Color.Gray
//                    )
//                }
//            }
//
//            OutlinedButton(
//                onClick = { showBottomSheet = !showBottomSheet },
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//                    .fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text(
//                    text = "프로필 선택"
//                )
//            }
//        }
//
//
//        selectedImageUri?.let { uri ->
//            Image(
//                painter = rememberAsyncImagePainter(uri),
//                contentDescription = "Selected Image",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//                    .clip(RoundedCornerShape(16.dp))
//            )
//        }
//    }
//
//    if (showBottomSheet) {
//        ModalBottomSheet(
//            onDismissRequest = {
//                showBottomSheet = false
//            }
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//                    .background(Color.White)
//                    .padding(16.dp)
//            ) {
//                Text("이미지 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                Spacer(modifier = Modifier.height(10.dp))
//                Button(
//                    onClick = { galleryLauncher.launch("image/*") },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("갤러리 열기")
//                }
//            }
//        }
//    }
//}
//

