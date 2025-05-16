package com.altruist.ui.previews

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.altruist.ui.theme.AltruistTheme
import com.altruist.ui.screens.WelcomeScreen
import com.altruist.ui.components.PrimaryButton
import com.altruist.ui.components.SecondaryButton
import com.altruist.ui.screens.LoginScreen

@Preview(showBackground = true, name = "Welcome Screen")
@Composable
fun WelcomeScreenPreview() {
    AltruistTheme {
        WelcomeScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
/*
@Preview(showBackground = true, name = "Primary Button")
@Composable
fun PrimaryButtonPreview() {
    AltruistTheme {
        PrimaryButton(text = "Iniciar sesión", onClick = {})
    }
}

@Preview(showBackground = true, name = "Secondary Button")
@Composable
fun SecondaryButtonPreview() {
    AltruistTheme {
        SecondaryButton(text = "Registrarse", onClick = {})
    }
}
*/

