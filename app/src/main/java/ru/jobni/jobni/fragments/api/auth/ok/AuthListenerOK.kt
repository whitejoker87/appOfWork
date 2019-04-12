package ru.jobni.jobni.fragments.api.auth.ok

interface AuthListenerOK {
    fun onTokenReceived(code: String)
}
