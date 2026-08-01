package com.example.kidsmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.CompositionLocalProvider

private val Purple = Color(0xFF6750A4)
private val LightPurple = Color(0xFFEADDFF)
private val BalanceGreen = Color(0xFF18864B)
private val WithdrawalRed = Color(0xFFC62828)
private val ScreenBackground = Color(0xFFFAF8FF)

private data class Child(val name: String, val balance: Int)

private val children = listOf(
    Child(name = "אגם", balance = 450),
    Child(name = "בן", balance = 320),
    Child(name = "יאיר", balance = 180),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KidsMoneyTheme { KidsMoneyHomeScreen() } }
    }
}

@Composable
private fun KidsMoneyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Purple,
            primaryContainer = LightPurple,
            background = ScreenBackground,
            surface = Color.White,
        ),
        content = content,
    )
}

@Composable
fun KidsMoneyHomeScreen() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = ScreenBackground,
            bottomBar = { HomeBottomBar() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            ) {
                Text(
                    text = "הילדים שלי",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF211A2D),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.height(24.dp))
                children.forEach { child ->
                    ChildCard(child)
                    Spacer(Modifier.height(14.dp))
                }
                Spacer(Modifier.height(10.dp))
                ActionButton(text = "הוספת כסף", color = Purple)
                Spacer(Modifier.height(12.dp))
                ActionButton(text = "משיכת כסף", color = WithdrawalRed)
            }
        }
    }
}

@Composable
private fun ChildCard(child: Child) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(LightPurple, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = child.name.take(1),
                    color = Purple,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = child.name,
                modifier = Modifier.weight(1f),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${child.balance} ₪",
                color = BalanceGreen,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ActionButton(text: String, color: Color) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun HomeBottomBar() {
    NavigationBar(containerColor = Color.White) {
        BottomBarItem(label = "בית", icon = Icons.Outlined.Home, selected = true)
        BottomBarItem(label = "היסטוריה", icon = Icons.Outlined.History, selected = false)
        BottomBarItem(label = "הגדרות", icon = Icons.Outlined.Settings, selected = false)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.BottomBarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
) {
    NavigationBarItem(
        selected = selected,
        onClick = {},
        icon = { Icon(imageVector = icon, contentDescription = label) },
        label = { Text(label) },
    )
}

@Preview(showBackground = true, locale = "iw")
@Composable
private fun HomeScreenPreview() {
    KidsMoneyTheme { KidsMoneyHomeScreen() }
}
