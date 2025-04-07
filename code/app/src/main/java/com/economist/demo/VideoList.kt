package com.economist.demo

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun VideoList(
    viewModel: VideoPlayerViewModel
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val videoList = viewModel.videoUris

    val focusedIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val viewportCenter = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

            visibleItems.minByOrNull {
                val itemCenter = it.offset + it.size / 2
                kotlin.math.abs(itemCenter - viewportCenter)
            }?.index
        }
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(videoList) { index, uri ->
            val isFocused = index == focusedIndex

            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    .border(width = 2.dp, color = Color.Black)
                    .background(if(isFocused){Color.Black} else {Color.White}),
                contentAlignment = Alignment.Center
            ) {
                if (isFocused) {
                    val position by viewModel.currentPosition.collectAsState()
                    val duration by viewModel.duration.collectAsState()

                    Box(modifier = Modifier.fillMaxSize()) {
                        VideoPlayer(
                            videoUri = uri,
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )

                        IconButton(
                            onClick = {
                                val intent = Intent(context, VideoPlayerFullScreenActivity::class.java)
                                intent.putExtra("video_uri", uri.toString())
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_full_screen),
                                contentDescription = "Fullscreen",
                                tint = Color.White
                            )
                        }

                        LinearProgressIndicator(
                            progress = { position.toFloat() / duration.coerceAtLeast(1L) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .align(Alignment.BottomCenter),
                            color = Color.Red
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.economist),
                        contentDescription = null,
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}




