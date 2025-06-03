package com.example.greetingcard.presentation.view.plan.plandetail.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.data.model.response.Schedule

/**
 * 스케쥴 아이템 컴포저블
 * */
@Composable
fun ScheduleItem(schedule: Schedule, onScheduleClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 시간
        Text(
            textAlign = TextAlign.Center,
            text = schedule.time,
            modifier = Modifier.padding(start = 16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 카드
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .clickable {
                    onScheduleClicked()
                }, colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Column {
                    Text(text = schedule.description, fontWeight = FontWeight.Bold)

                    schedule.memo?.let {
                        Text(text = it, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    if (schedule.cost != null) {
                        Text(
                            text = "금액 ${"%,d원".format(schedule.cost)}",
                            color = Color(0xFF333333),
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "💰 미정원", color = Color(0xFF999999), fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * 스케쥴 상세 BottomSheet
 * */
@Composable
fun ScheduleDetailBottomSheet(schedule: Schedule?, onSaveMemo: (String) -> Unit) {
    var memo by remember { mutableStateOf(schedule?.memo ?: "") }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .background(color = Color.White)
            .fillMaxHeight()
    ) {
        // 스케쥴 description
        Text(text = schedule!!.description, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ChecklistRtl,
                tint = if (schedule.memo != null) Color.Black else Color.Gray,
                contentDescription = "memo_icon"
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = schedule.memo ?: "메모",
                fontSize = 13.sp,
                color = if (schedule.memo != null) Color.Black else Color.Gray
            )

        }
        Spacer(modifier = Modifier.height(50.dp))
//        TextField(
//            value = memo,
//            onValueChange = { memo = it },
//            label = { Text("메모 작성") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { onSaveMemo(memo) }, modifier = Modifier.align(Alignment.End)) {
//            Text("저장")
//        }
    }
}
