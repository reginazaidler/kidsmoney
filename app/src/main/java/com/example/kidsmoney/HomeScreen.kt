package com.example.kidsmoney

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kidsmoney.data.ChildBalance
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BalanceGreen = Color(0xFF18864B)
private val WithdrawalRed = Color(0xFFC62828)
@Composable
fun KidsMoneyHomeScreen(viewModel: HomeViewModel, onAddMoney: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(containerColor = ScreenBackground, bottomBar = { HomeBottomBar() }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Text(
                text = "הילדים שלי", modifier = Modifier.fillMaxWidth(), color = Color(0xFF211A2D),
                fontSize = 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start,
            )
            Spacer(Modifier.height(24.dp))
            when {
                uiState.isLoading -> Text("טוען נתונים…", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                uiState.children.isEmpty() -> Text("אין ילדים להצגה", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                else -> uiState.children.forEach { child -> ChildCard(child); Spacer(Modifier.height(14.dp)) }
            }
            Spacer(Modifier.height(10.dp))
            ActionButton("הוספת כסף", Purple, enabled = !uiState.isLoading && uiState.children.isNotEmpty(), onClick = onAddMoney)
            Spacer(Modifier.height(12.dp))
            ActionButton("משיכת כסף", WithdrawalRed, enabled = false)
        }
    }
}

@Composable
private fun ChildCard(child: ChildBalance) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(56.dp).background(LightPurple, CircleShape), contentAlignment = Alignment.Center) {
                Text(child.name.take(1), color = Purple, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(16.dp))
            Text(child.name, Modifier.weight(1f), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("${child.balance} ₪", color = BalanceGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick, enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
    ) { Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun HomeBottomBar() {
    NavigationBar(containerColor = Color.White) {
        BottomBarItem("בית", Icons.Outlined.Home, true)
        BottomBarItem("היסטוריה", Icons.Outlined.History, false)
        BottomBarItem("הגדרות", Icons.Outlined.Settings, false)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.BottomBarItem(
    label: String, icon: ImageVector, selected: Boolean,
) {
    NavigationBarItem(
        selected = selected, onClick = {},
        icon = { Icon(icon, contentDescription = label) }, label = { Text(label) },
    )
}

@Preview(showBackground = true, locale = "iw")
@Composable
private fun HomeScreenPreview() {
    KidsMoneyTheme {
        ChildCard(ChildBalance(1, "אגם", 0))
    }
}
