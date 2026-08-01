package com.example.kidsmoney

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kidsmoney.data.KidsMoneyRepository
import kotlinx.coroutines.launch

class AddMoneyViewModel(private val repository: KidsMoneyRepository) : ViewModel() {
    val form = AddMoneyState()
    var isSaving by mutableStateOf(false); private set
    var errorMessage by mutableStateOf<String?>(null); private set

    fun save(onSuccess: () -> Unit) {
        if (isSaving) return
        val childId = form.childId
        val moneyType = form.moneyType
        val source = form.source
        val client = form.client
        val amount = form.amount.toLongOrNull()
        if (childId == null || moneyType.isNullOrBlank() || source.isNullOrBlank() ||
            client.isNullOrBlank() || amount == null || amount <= 0
        ) {
            errorMessage = "יש למלא את כל הפרטים ולהזין סכום גדול מאפס"
            return
        }
        isSaving = true
        errorMessage = null
        viewModelScope.launch {
            runCatching { repository.addIncome(childId, moneyType, source, client, amount) }
                .onSuccess {
                    form.clear()
                    isSaving = false
                    onSuccess()
                }
                .onFailure {
                    isSaving = false
                    errorMessage = "שמירת הכסף נכשלה. אפשר לנסות שוב"
                }
        }
    }

    fun clear() {
        if (!isSaving) {
            form.clear()
            errorMessage = null
        }
    }

    class Factory(private val repository: KidsMoneyRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AddMoneyViewModel(repository) as T
    }
}
