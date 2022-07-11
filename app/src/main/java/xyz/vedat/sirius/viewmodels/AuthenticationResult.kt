package xyz.vedat.sirius.viewmodels

import srs.ManualVerificationIntermediary
import srs.SRSSession
import xyz.vedat.sirius.SessionManager

class AuthenticationResult {
    var session: SRSSession? = null
        private set

    var manualVerificationIntermediary: ManualVerificationIntermediary? = null
        private set

    val success: Boolean
        get() = failureReason == null

    var failureReason: String? = null
        private set

    constructor(session: SRSSession) {
        this.session = session
        SessionManager.session = session
    }

    constructor(manualVerificationIntermediary: ManualVerificationIntermediary) {
        this.manualVerificationIntermediary = manualVerificationIntermediary
    }

    constructor(failureReason: String) {
        this.failureReason = failureReason
    }
}
