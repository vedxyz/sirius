package xyz.vedat.sirius

import srs.SRSSession

object SessionManager {
    var session: SRSSession? = null
    val hasSession: Boolean
        get() = session != null
}
