package com.altruist.ui.screens.post_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.PostDetailViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import com.altruist.R
import com.altruist.data.model.Post
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.DotsIndicator
import com.altruist.ui.components.SmallSecondaryButton
import com.altruist.ui.theme.BackgroundTop
import com.altruist.ui.theme.BackgroundTopLighter
import com.altruist.ui.theme.DarkGray
import com.altruist.ui.theme.DarkYellowTransparent
import com.altruist.ui.theme.LightYellowSearchScreen
import com.altruist.ui.theme.TitleMediumTextStyle
import com.altruist.ui.theme.YellowLightTransparent
import com.altruist.ui.theme.YellowSearchScreen
import com.altruist.utils.enums.RequestStatus
import com.altruist.utils.getTimeAgoText
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PostDetailScreen(
    post: Post,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val requestSuccessMessage by viewModel.requestSuccessMessage.collectAsState()
    val requestStatus by viewModel.requestStatus.collectAsState()

    val isOwnPost = viewModel.isOwnPost(post)

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(requestSuccessMessage) {
        if (!requestSuccessMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = requestSuccessMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
        viewModel.clearRequestSuccess()
    }

    LaunchedEffect(post.id_post) {
        viewModel.getRequestStatus(post.id_post, post.user.id_user)
    }


    AltruistScreenWrapper(
        statusBarColor = White,
        navigationBarColor = White
    ) {
        Scaffold(
            snackbarHost = {
                AltruistSnackbarHost(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        LatLng(post.latitude, post.longitude),
                        13f
                    )
                }

                val imageUrls = post.imageUrls
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageUrls.size })

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(White)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        color = YellowSearchScreen,
                        shadowElevation = 8.dp
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            ) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUrls[page]),
                                        contentDescription = "Imagen $page",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp), // altura pequeña solo para el indicador
                                color = White
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DotsIndicator(
                                        totalDots = imageUrls.size,
                                        selectedIndex = pagerState.currentPage
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Text(
                                    text = post.title,
                                    style = TitleMediumTextStyle,
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "${post.category.name} · ${post.quality ?: "Estado desconocido"}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_clock),
                                            contentDescription = " Publicado hace",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = getTimeAgoText(post.date_created),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                    }

                                    if (!isOwnPost) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_location_marker),
                                                contentDescription = "Ubicación",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = String.format(Locale.US, "%.1f km", post.distanceFromFilter),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = post.description ?: "No hay descripción.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Ubicación",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )


                    Spacer(modifier = Modifier.height(20.dp))

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        cameraPositionState = cameraPositionState
                    ) {
                        Circle(
                            center = LatLng(post.latitude, post.longitude),
                            radius = 300.0,
                            strokeColor = DarkYellowTransparent,
                            fillColor = YellowLightTransparent
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter(post.user.profile_picture_url),
                                contentDescription = "Foto del usuario",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = post.user.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (!isOwnPost) {
                            if (requestStatus == RequestStatus.REQUESTED) {
                                SmallSecondaryButton(
                                    text = "Solicitado",
                                    onClick = {},
                                    enabled = false,
                                    containerColor = Color.LightGray,
                                    contentColor = Color.DarkGray
                                )
                            } else {
                                SmallSecondaryButton(
                                    text = "Solicitar",
                                    onClick = {
                                        viewModel.sendRequest(post.id_post)
                                    },
                                    enabled = true,
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }


                    }
                }
            }
        }
    }
}
