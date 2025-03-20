package com.example.greetingcard.presentation.ui.home.posting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.greetingcard.R
import com.example.greetingcard.data.model.response.Comment
import com.example.greetingcard.data.model.response.Post
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun calculateElapsedTime(startDate: String): String {
    val dateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()) // API 날짜 형식에 맞게 수정 필요
    val startDateParsed: Date? = try {
        dateFormat.parse(startDate)
    } catch (e: Exception) {
        null
    }

    if (startDateParsed == null) {
        return "잘못된 날짜 형식"
    }

    val now = Date()
    val diffMillis = now.time - startDateParsed.time

    val seconds = diffMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        years > 0 -> "${years}년 전"
        months > 0 -> "${months}달 전"
        weeks > 0 -> "${weeks}주 전"
        days > 0 -> "${days}일 전"
        hours > 0 -> "${hours}시간 전"
        minutes > 0 -> "${minutes}분 전"
        else -> "방금 전"
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    commentList: List<Comment>,
    post: Post,
    onClickUserProfile: () -> Unit,
    onClickLikeIcon: () -> Unit,
    onClickBubbleIcon: () -> Unit,
) {
    // 캡션 펼침 여부
    var isExpanded by remember { mutableStateOf(false) }
    // 바텀시트 표시 여부
    var showBottomSheet by remember { mutableStateOf(false) }

    val elapsedTime = calculateElapsedTime(post.createTime)
    Column {
        // 유저 프로필 섹션
        ProfileSection(
            post.userName.toString(), post.userProfileImgUrl, onClickUserProfile, elapsedTime
        )

        // 이미지 섹션
        PostImgSection(post.safeUploadImage, onClickPostImg = { Log.d("PostItem", "게시물 이미지 클릭") })
        Spacer(modifier = Modifier.height(4.dp))

        // 좋아요, 댓글 섹션
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    modifier = Modifier.size(25.dp),
                    contentDescription = "좋아요 아이콘"
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = post.likeCount.toString(), style = MaterialTheme.typography.bodyLarge)
            }
            if (showBottomSheet) {
                CommentListBottomSheet(commentList, onDismissRequest = { showBottomSheet = false })
            }
            Row(
                modifier = Modifier.clickable { showBottomSheet = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "댓글 아이콘",
                    modifier = Modifier.size(25.dp),
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "13", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // 캡션 섹션
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = post.userName.toString() + "번 게스트",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = post.detail,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis, // 넘칠 경우 "..." 처리
                )
                Text(text = if (isExpanded) "접기" else "더보기",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.clickable { isExpanded = !isExpanded })
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}

// 유저 프로필 섹션
@Composable
fun ProfileSection(
    userNickName: String,
    userProfileImgUrl: String?,
    onClickUserProfile: () -> Unit,
    postUploadDate: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onClickUserProfile() }) {
        AsyncImage(
            model = userProfileImgUrl,
            error = painterResource(R.drawable.user_profile),
            contentDescription = "유저 프로필 이미지",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = userNickName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.LightGray)) {
                    append("⦁ ")
                }
                append(postUploadDate)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )
    }
}

// 게시물 이미지 섹션
@Composable
fun PostImgSection(postImgUrl: List<String?>?, onClickPostImg: () -> Unit) {
    // 각 페이지별 이미지 로딩 실패 여부
    val errorStates = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(postImgUrl?.size ?: 0) { add(false) }
        }
    }

    val pagerState = rememberPagerState(pageCount = { postImgUrl?.size ?: 0 })

    HorizontalPager(
        state = pagerState, modifier = Modifier.fillMaxSize()
    ) { page ->
        if (errorStates[page]) {
            // 에러가 발생한 경우
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.2f)
                    .background(Color.LightGray)
                    .clickable { onClickPostImg() },
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(R.drawable.refresh_icon),
                    contentDescription = "로딩 실패 아이콘",
                )
            }
        } else {
            // 정상적인 이미지 로딩
            AsyncImage(
                model = postImgUrl?.get(page),
                onError = { errorStates[page] = true }, // 해당 페이지의 상태만 변경
                onSuccess = { errorStates[page] = false }, // 성공 시 에러 상태 초기화
                contentDescription = "게시물 사진",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f),
                contentScale = ContentScale.FillBounds
            )
        }
    }

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }
}

// TODO: 댓글리스트 커스텀으로 스크롤 개선
// 댓글 리스트 바텀시트
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListBottomSheet(
    commentList: List<Comment>,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        dragHandle = { CommentBottomSheetDragHandle() },
        containerColor = Color.White,
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        content = {
            // 스크롤 가능한 리스트
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 12.dp),
            ) {
                items(commentList) { comment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                    ) {
                        AsyncImage(
                            model = comment.userProfileImgUrl,
                            error = painterResource(R.drawable.user_profile),
                            contentDescription = "유저 프로필 이미지",
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .size(35.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = comment.userNickName,
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = comment.comment,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        },
    )
}


// 댓글 바텀시트 드래그 핸들
@Composable
fun CommentBottomSheetDragHandle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .width(45.dp)
                .padding(top = 12.dp, bottom = 8.dp)
                .clip(CircleShape),
            color = Color.LightGray,
            thickness = 5.dp,
        )
        Text(
            text = "댓글",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape),
            color = Color.LightGray,
            thickness = 0.5.dp,
        )
    }
}

