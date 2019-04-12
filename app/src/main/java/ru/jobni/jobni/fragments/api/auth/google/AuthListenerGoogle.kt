package ru.jobni.jobni.fragments.api.auth.google

interface AuthListenerGoogle {
    fun onTokenReceived(code: String)
}
