package com.example.kidsmoney

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Stable
class AddMoneyState {
    var child by mutableStateOf<String?>(null)
    var moneyType by mutableStateOf<String?>(null)
    var source by mutableStateOf<String?>(null)
    var client by mutableStateOf<String?>(null)
    var amount by mutableStateOf("")

    fun selectMoneyType(value: String) {
        if (moneyType != value) source = null
        moneyType = value
    }

    fun clear() {
        child = null; moneyType = null; source = null; client = null; amount = ""
    }
}

fun NavGraphBuilder.addMoneyGraph(
    navController: NavHostController,
    state: AddMoneyState,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    composable("add/child") {
        SelectionStep("למי מוסיפים כסף?", 1, listOf("אגם", "בן", "יאיר"), state.child,
            onSelect = { state.child = it }, onBack = { navController.popBackStack() },
            onContinue = { navController.navigate("add/type") })
    }
    composable("add/type") {
        SelectionStep("איזה סוג כסף?", 2, listOf("עבודה", "מתנה"), state.moneyType,
            onSelect = state::selectMoneyType, onBack = { navController.popBackStack() },
            onContinue = { navController.navigate("add/source") })
    }
    composable("add/source") {
        val options = if (state.moneyType == "עבודה")
            listOf("בייביסיטר", "דוג ווקר", "שיעורים פרטיים")
        else listOf("יום הולדת", "חג", "סיום בית ספר")
        SelectionStep("מה מקור הכסף?", 3, options, state.source,
            onSelect = { state.source = it }, onBack = { navController.popBackStack() },
            onContinue = { navController.navigate("add/client") })
    }
    composable("add/client") {
        SelectionStep("ממי התקבל הכסף?", 4, listOf("אמא", "אבא", "סבתא", "סבא", "ג'ודי", "דובי"), state.client,
            onSelect = { state.client = it }, onBack = { navController.popBackStack() },
            onContinue = { navController.navigate("add/amount") })
    }
    composable("add/amount") {
        AmountStep(state.amount, onAmountChange = { state.amount = it },
            onBack = { navController.popBackStack() }, onContinue = { navController.navigate("add/review") })
    }
    composable("add/review") {
        ReviewStep(state, onBack = { navController.popBackStack() }, onSave = onSave,
            onEdit = { navController.popBackStack() }, onCancel = onCancel)
    }
}

@Composable
private fun StepScaffold(
    title: String, step: Int, onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(containerColor = ScreenBackground) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "חזרה")
            }
            Text("שלב $step מתוך 6", color = Purple, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(title, Modifier.fillMaxWidth(), fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start)
            Spacer(Modifier.height(24.dp))
            content()
        }
    }
}

@Composable
private fun SelectionStep(
    title: String, step: Int, options: List<String>, selected: String?,
    onSelect: (String) -> Unit, onBack: () -> Unit, onContinue: () -> Unit,
) {
    StepScaffold(title, step, onBack) {
        options.forEach { option ->
            SelectionCard(option, selected == option) { onSelect(option) }
            Spacer(Modifier.height(12.dp))
        }
        Spacer(Modifier.height(16.dp))
        ContinueButton(enabled = selected != null, onClick = onContinue)
    }
}

@Composable
private fun SelectionCard(text: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(64.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) LightPurple else Color.White),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) Purple else Color(0xFFE1DCE8)),
    ) {
        Row(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 19.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Purple else Color(0xFF211A2D))
        }
    }
}

@Composable
private fun AmountStep(amount: String, onAmountChange: (String) -> Unit, onBack: () -> Unit, onContinue: () -> Unit) {
    val isValid = amount.toLongOrNull()?.let { it > 0 } == true
    StepScaffold("כמה כסף להוסיף?", 5, onBack) {
        OutlinedTextField(
            value = amount,
            onValueChange = { value -> if (value.all(Char::isDigit)) onAmountChange(value) },
            modifier = Modifier.fillMaxWidth(), label = { Text("סכום") }, suffix = { Text("₪") },
            singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp), isError = amount.isNotEmpty() && !isValid,
            supportingText = if (amount.isNotEmpty() && !isValid) ({ Text("יש להזין סכום גדול מאפס") }) else null,
        )
        Spacer(Modifier.height(24.dp))
        ContinueButton(isValid, onContinue)
    }
}

@Composable
private fun ContinueButton(enabled: Boolean, onClick: () -> Unit) {
    Button(onClick, Modifier.fillMaxWidth().height(56.dp), enabled = enabled, shape = RoundedCornerShape(16.dp)) {
        Text("המשך", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ReviewStep(
    state: AddMoneyState, onBack: () -> Unit, onSave: () -> Unit, onEdit: () -> Unit, onCancel: () -> Unit,
) {
    StepScaffold("בדיקת הפרטים", 6, onBack) {
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ReviewRow("ילד", state.child.orEmpty())
                ReviewRow("סוג כסף", state.moneyType.orEmpty())
                ReviewRow("מקור", state.source.orEmpty())
                ReviewRow("ממי התקבל", state.client.orEmpty())
                ReviewRow("סכום", "${state.amount} ₪")
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onSave, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) {
            Text("שמירה", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onEdit, Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) { Text("חזרה לעריכה") }
        Spacer(Modifier.height(10.dp))
        Button(
            onCancel, Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Purple),
        ) { Text("ביטול") }
    }
}

@Composable
private fun ReviewRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold)
    }
}
