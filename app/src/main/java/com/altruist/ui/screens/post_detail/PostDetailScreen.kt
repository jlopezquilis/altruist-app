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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PostDetailScreen(
    postId: Long,
    viewModel: PostDetailViewModel = hiltViewModel(),
    onRequestClick: () -> Unit
) {
    val post by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage!!,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(postId) {
        viewModel.loadPostById(postId)
    }

    AltruistScreenWrapper(
        statusBarColor = White,
        navigationBarColor = White
    ) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (post != null) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        LatLng(post!!.latitude, post!!.longitude),
                        12f
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(White)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 30.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(post!!.imageUrls),
                            contentDescription = "Imagen del post",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = post!!.title,
                            fontSize = 24.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${post!!.category.name} \u2022 ${post!!.distanceFromFilter.toInt()} km",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = post!!.description ?: "No hay descripción.",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 30.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        cameraPositionState = cameraPositionState
                    ) {
                        Circle(
                            center = LatLng(post!!.latitude, post!!.longitude),
                            radius = 300.0,
                            strokeColor = Color(0x550000FF),
                            fillColor = Color(0x220000FF)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(post!!.user.profile_picture_url),
                            contentDescription = "Foto del usuario",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = post!!.user.username,
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(bottom = 40.dp)
                    ) {
                        SecondaryButton(
                            text = "Solicitar",
                            onClick = onRequestClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage ?: "Error desconocido", color = Color.Red)
                }
            }
        }
    }
}
