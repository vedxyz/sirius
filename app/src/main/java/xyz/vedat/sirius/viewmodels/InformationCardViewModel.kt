package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.vedat.sirius.SessionManager

class InformationCardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(InformationCardUiState())
    val uiState: StateFlow<InformationCardUiState> = _uiState.asStateFlow()

    fun fetch() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getInformationCard() ?: throw Exception("No session")
            }

            _uiState.update {
                it.copy(isLoading = false, informationCard = result)
            }
        }
    }
}
