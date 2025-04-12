package com.altruist.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.altruist.R
import com.altruist.data.model.Category
import com.altruist.ui.components.AltruistSnackbarHost
import com.altruist.ui.components.CategoryButton
import com.altruist.ui.components.DoubleTitle
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.components.SecondaryGrayButton
import com.altruist.ui.theme.White
import com.altruist.utils.AltruistScreenWrapper
import com.altruist.viewmodel.SharedViewModel

@Composable
fun LocationPermissionScreen(
    onPermissionResult: (Boolean) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onPermissionResult(isGranted)
        }
    )

    val snackbarHostState = remember { SnackbarHostState() }


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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "¿Nos permites conocer tu ubicación?",
                        style = MaterialTheme.typography.titleLarge
                    )


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_map), // tu imagen local
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )

                        Spacer(modifier = Modifier.height(120.dp))

                        SecondaryButton(
                            text = "Sí",
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        SecondaryGrayButton(
                            text = "No",
                            onClick = {
                                onPermissionResult(false)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
