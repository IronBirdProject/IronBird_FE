package com.example.greetingcard.presentation.ui.home.posting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel


// 포스팅 탭
@Composable
fun PostingTab(homeViewModel: HomeViewModel, listState: LazyListState) {
    val myTravelList = homeViewModel.myTravelList
    val selectedDestination = homeViewModel.selectedDestination
    val searchQuery = homeViewModel.searchQuery

    val postList by homeViewModel.allPostList.observeAsState(emptyList())

    LazyColumn(
        state = listState,
    ) {
        println("포스팅 탭 listState: $listState")
        item {
            MyTravelList(
                destinations = myTravelList,
                selectedDestination = selectedDestination,
                onDestinationSelected = { homeViewModel.selectDestination(it) },
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        homeViewModel.changeQuery(it)
                    },
                )
            }
        }
        items(postList) { post ->
            PostItem(
                post = post,
                onClickUserProfile = {},
                onClickLikeIcon = {},
                onClickBubbleIcon = {},
                commentList = homeViewModel.commentList,
            )
        }
    }
}

@Composable
@Preview
fun PreviewSearchBar() {
    var query by remember { mutableStateOf("") }

    SearchBar(
        query = query,
        onQueryChange = { newQuery ->
            query = newQuery
        }
    )
}

// 여행지 검색창
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(45.dp)
            .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 12.dp),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                if (query.isEmpty()) {
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
