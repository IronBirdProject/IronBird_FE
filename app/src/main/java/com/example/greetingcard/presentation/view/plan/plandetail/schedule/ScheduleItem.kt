package com.example.greetingcard.presentation.view.plan.plandetail.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = schedule.time,
                textAlign = TextAlign.Center,
                color = Color(0xFF888888),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 스케쥴 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.7f)
                )
                .clickable { onScheduleClicked() },
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = schedule.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF222222)
                )

                schedule.memo?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (schedule.cost != null) {
                        Text(
                            text = "💰 ${"%,d원".format(schedule.cost)}",
                            fontSize = 12.sp,
                            color = Color(0xFF444444),
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = "💰 금액 미정",
                            fontSize = 12.sp,
                            color = Color(0xFFBBBBBB)
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
