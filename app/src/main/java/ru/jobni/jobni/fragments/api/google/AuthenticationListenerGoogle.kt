package ru.jobni.jobni.fragments.api.google

interface AuthenticationListenerGoogle {
    fun onTokenReceived(code: String)
}
