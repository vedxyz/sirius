package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.ManualVerificationIntermediary
import srs.SRSSession

class LoginViewModel : ViewModel() {
    private val _authenticationResult = MutableLiveData<AuthenticationResult>()
    val authenticationResult: LiveData<AuthenticationResult>
        get() = _authenticationResult

    fun beginManualVerification(bilkentId: String, password: String): LiveData<AuthenticationResult> {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SRSSession.withManualVerification(bilkentId, password)
            }

            _authenticationResult.value = AuthenticationResult(result)
        }
        return authenticationResult
    }

    fun completeManualVerification(verificationCode: String): LiveData<AuthenticationResult> {
        if (_authenticationResult.value == null)
            throw IllegalStateException("Cannot verify without having started login")

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                _authenticationResult.value?.manualVerificationIntermediary?.let { it.verify(verificationCode) }
            }

            if (result == null) {
                _authenticationResult.value = AuthenticationResult("Couldn't verify code")
                return@launch
            }

            _authenticationResult.value = AuthenticationResult(result)
        }
        return authenticationResult
    }

    fun beginAutomaticVerification(
        bilkentId: String,
        password: String,
        email: String,
        emailPassword: String
    ): LiveData<AuthenticationResult> {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SRSSession.withAutomatedVerification(bilkentId, password, email, emailPassword)
            }

            _authenticationResult.value = AuthenticationResult(result)
        }
        return authenticationResult
    }
}
