package com.example.greetingcard.presentation.view.plan.plandetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.greetingcard.presentation.view.plan.createplan.MonthView
import com.example.greetingcard.presentation.view.plan.createplan.generateMonths
import com.example.greetingcard.presentation.viewModel.plan.createplan.SelectedDates
import com.example.greetingcard.presentation.viewModel.plan.createplan.updateWith
import java.time.LocalDate

@Composable
fun CalendarEditDialog(
    initialStartDate: LocalDate,
    initialEndDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit
) {
    var selectedDates by remember {
        mutableStateOf(
            SelectedDates(
                startDate = initialStartDate,
                endDate = initialEndDate
            )
        )
    }

    val currentMonth = remember { LocalDate.now().withDayOfMonth(1) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "여행 날짜 수정",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(generateMonths(currentMonth, 13)) { month ->
                        MonthView(
                            yearMonth = month,
                            today = LocalDate.now(),
                            selectedDates = selectedDates,
                            onDateSelected = { date ->
                                selectedDates = selectedDates.updateWith(date)
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("취소")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        enabled = selectedDates.startDate != null && selectedDates.endDate != null,
                        onClick = {
                            onConfirm(
                                selectedDates.startDate!!,
                                selectedDates.endDate!!
                            )
                        }
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}
