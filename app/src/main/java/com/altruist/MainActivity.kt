package com.altruist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.altruist.ui.screens.LoginScreen
import com.altruist.ui.theme.AltruistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //AltruistTheme {
                LoginScreen()
            //}
        }
    }
}
