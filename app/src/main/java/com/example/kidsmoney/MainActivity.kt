package com.example.kidsmoney

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

val Purple = Color(0xFF6750A4)
val LightPurple = Color(0xFFEADDFF)
val ScreenBackground = Color(0xFFFAF8FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KidsMoneyTheme { KidsMoneyApp() } }
    }
}

@Composable
private fun KidsMoneyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as KidsMoneyApplication
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(application.container.repository))
    val addMoneyViewModel: AddMoneyViewModel = viewModel(factory = AddMoneyViewModel.Factory(application.container.repository))
    val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            KidsMoneyHomeScreen(
                viewModel = homeViewModel,
                onAddMoney = {
                    addMoneyViewModel.clear()
                    navController.navigate("add/child")
                },
            )
        }
        addMoneyGraph(
            navController = navController,
            state = addMoneyViewModel.form,
            children = homeState.children,
            onCancel = {
                addMoneyViewModel.clear()
                navController.popBackStack("home", inclusive = false)
            },
            isSaving = addMoneyViewModel.isSaving,
            errorMessage = addMoneyViewModel.errorMessage,
            onSave = {
                addMoneyViewModel.save {
                    Toast.makeText(context, "הכסף נוסף בהצלחה", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
        )
    }
}

@Composable
fun KidsMoneyTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
}
