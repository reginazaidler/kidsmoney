package com.example.kidsmoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kidsmoney.data.ChildBalance
import com.example.kidsmoney.data.KidsMoneyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(val isLoading: Boolean = true, val children: List<ChildBalance> = emptyList())

class HomeViewModel(repository: KidsMoneyRepository) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = repository.childrenWithBalances
        .map { HomeUiState(isLoading = false, children = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    class Factory(private val repository: KidsMoneyRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
    }
}
