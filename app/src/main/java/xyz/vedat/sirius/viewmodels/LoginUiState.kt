package xyz.vedat.sirius.viewmodels

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val verificationFragmentId: Int? = null,
    val manualVerificationReference: String? = null,
)
