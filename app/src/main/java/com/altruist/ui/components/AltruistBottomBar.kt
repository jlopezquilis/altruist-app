package com.altruist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.altruist.R
import com.altruist.ui.theme.BottomMenuTextStyle
import com.altruist.ui.theme.White

@Composable
fun AltruistBottomBar(
    selected: String,
    onMainMenuClick: () -> Unit,
    onDonateClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column {
        Divider(color = Color.Black.copy(alpha = 0.15f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(vertical = 12.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                iconId = R.drawable.ic_home,
                text = "Inicio",
                isSelected = selected == "Inicio",
                onClick = onMainMenuClick
            )

            BottomBarItem(
                iconId = R.drawable.ic_donate,
                text = "Donar",
                isSelected = selected == "Donar",
                onClick = onDonateClick
            )

            BottomBarItem(
                iconId = R.drawable.ic_search_selected,
                text = "Buscar",
                isSelected = selected == "Buscar",
                onClick = onSearchClick
            )
        }
    }
}

@Composable
fun BottomBarItem(
    iconId: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
            .padding(horizontal =35.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            style = BottomMenuTextStyle,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = Color.Black
        )
    }
}
