package com.example.greetingcard.presentation.ui.home.planning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// 플래닝 화면
@Composable
fun PlanningScreen(listState: LazyListState) {
    println("플래닝탭 listState: $listState")
    LazyColumn(
        state = listState,
    ) {
        item {
            SearchBar()
            Spacer(modifier = Modifier.height(400.dp))
        }
        item {
            FeaturesGrid()
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
            AdvertisementSection()
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

// 검색창
@Composable
fun SearchBar() {
    var text by remember {
        mutableStateOf("")
    }
    BasicTextField(

        value = text,
        onValueChange = { text = it },
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp, vertical = 8.dp)
            .height(60.dp)
            .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    tint = Color.Gray,
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
                if (text.isEmpty()) {
                    Text(
                        text = "원하는 여행지를 검색해보세요!",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                    )
                }
                innerTextField()
            }
        }
    )
}
