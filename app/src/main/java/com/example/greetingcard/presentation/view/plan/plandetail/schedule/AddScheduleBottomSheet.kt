package com.example.greetingcard.presentation.view.plan.plandetail.schedule

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 스케쥴 추가 BottomSheet
 * */
@Composable
fun AddScheduleBottomSheet(
    selectedDay: Int,
    onSave: (desc: String, cost: Int, memo: String, time: String) -> Unit,
    onCancel: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf(0) }
    var memo by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    val context = LocalContext.current

    val timePickerDialog = remember {
        TimePickerDialog(context, { _, hour: Int, minute: Int ->
            time = String.format("%02d:%02d", hour, minute)
        }, 9, 0, true)
    }

    Column(
        modifier = Modifier
//            .fillMaxHeight()
            .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(horizontal = 24.dp)

    ) {
        Text(
            text = "DAY $selectedDay",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )

        Text(
            text = "할 일",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        LabeledInputField(label = "설명") {
            RoundedGrayInputField(
                value = description,
                onValueChange = { description = it },
                placeholder = "장소 설명"
            )
        }

        LabeledInputField(label = "필요 금액") {
            RoundedGrayInputField(
                value = if (cost == 0) "" else cost.toString(),
                onValueChange = { cost = it.toIntOrNull() ?: 0 },
                placeholder = "예: 20000",
                keyboardType = KeyboardType.Number
            )
        }

        LabeledInputField(label = "시간") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .clickable { timePickerDialog.show() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (time.isNotEmpty()) time else "시간 선택 (예: 10:00)",
                    fontSize = 16.sp,
                    color = if (time.isNotEmpty()) Color.Black else Color.Gray
                )
            }
        }

        LabeledInputField(label = "메모") {
            RoundedGrayInputField(
                value = memo,
                onValueChange = { memo = it },
                placeholder = "필요시 간단한 메모 작성",
                singleLine = false,
                height = 100.dp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            OutlinedButton(
                onClick = onCancel,
                shape = RoundedCornerShape(20.dp),
//                border = ButtonDefaults.outlinedBorder.copy(width = 1.dp, color = Color(0xFFBBBBBB))
            ) {
                Text("취소", color = Color(0xFF333333))
            }

            Button(
                onClick = { onSave(description, cost, memo, time) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1C1C1E),
                    contentColor = Color.White
                )
            ) {
                Text("저장")
            }
        }
    }
}


@Composable
private fun LabeledInputField(label: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(bottom = 16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        content()
    }
}

@Composable
fun RoundedGrayInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    height: Dp = 56.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier.fillMaxSize(),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(text = placeholder, color = Color.Gray, fontSize = 16.sp)
                }
                innerTextField()
            }
        )
    }
}


@Composable
private fun AddScheduleInfoInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    height: Dp = 56.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Color(0xFFF2F2F7), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp), contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
            singleLine = singleLine,
            modifier = Modifier.fillMaxSize(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(text = placeholder, color = Color.Gray, fontSize = 16.sp)
                }
                innerTextField()
            })
    }
}