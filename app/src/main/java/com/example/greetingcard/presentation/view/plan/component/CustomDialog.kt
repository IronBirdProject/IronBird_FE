package com.example.greetingcard.presentation.view.plan.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PlanDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { onDismiss() },
        title = { Text("플랜 삭제") },
        text = { Text("정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.") },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("삭제", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("취소")
            }
        }
    )
}

@Composable
fun EditTitleDialog(
    isDialogOpen: Boolean,
    currentTitle: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val editedTitle = remember { mutableStateOf(currentTitle) }
    if (isDialogOpen) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { onDismiss() },
            title = { Text("여행 제목 수정") },
            text = {
                OutlinedTextField(
                    value = editedTitle.value,
                    onValueChange = { editedTitle.value = it },
                    label = { Text("여행 제목") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(editedTitle.value)
                }) {
                    Text("완료")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("취소")
                }
            }
        )
    }
}