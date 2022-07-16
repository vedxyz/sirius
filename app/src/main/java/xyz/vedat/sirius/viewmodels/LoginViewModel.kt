package xyz.vedat.sirius.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.ManualVerificationIntermediary
import srs.SRSSession
import xyz.vedat.sirius.R
import xyz.vedat.sirius.SessionManager
import xyz.vedat.sirius.defaultLogTag

class LoginViewModel : ViewModel() {
    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isLoggedIn: Boolean = false,
        val verificationFragmentId: Int? = null,
        val manualVerificationReference: String? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var manualVerificationIntermediary: ManualVerificationIntermediary? = null

    fun beginManualVerification(bilkentId: String, password: String) {
        Log.v(defaultLogTag, "Manual verification in process")

        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SRSSession.withManualVerification(bilkentId, password)
            }

            manualVerificationIntermediary = result
            _uiState.update {
                it.copy(
                    isLoading = false,
                    verificationFragmentId = R.id.action_login_bnav_item_to_manual_verification_navfragment,
                    manualVerificationReference = manualVerificationIntermediary!!.reference
                )
            }
        }
    }

    fun completeManualVerification(verificationCode: String) {
        Log.v(defaultLogTag, "Manual verification completion in process")

        if (manualVerificationIntermediary == null)
            throw IllegalStateException("Cannot verify without having started login")

        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                manualVerificationIntermediary?.let { it.verify(verificationCode) }
            }

            if (result == null) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Couldn't verify code")
                }
                return@launch
            }

            SessionManager.session = result
            _uiState.update {
                it.copy(isLoading = false, isLoggedIn = true)
            }
        }
    }

    fun beginAutomaticVerification(
        bilkentId: String,
        password: String,
        email: String,
        emailPassword: String
    ) {
        Log.v(defaultLogTag, "Automatic verification in process")

        _uiState.update {
            it.copy(
                isLoading = true,
                verificationFragmentId = R.id.action_login_bnav_item_to_automatic_verification_navfragment
            )
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SRSSession.withAutomatedVerification(bilkentId, password, email, emailPassword)
            }

            SessionManager.session = result
            _uiState.update {
                it.copy(isLoading = false, isLoggedIn = true)
            }
        }
    }

    fun navigationConsumed() {
        _uiState.update {
            it.copy(verificationFragmentId = null)
        }
    }
}
