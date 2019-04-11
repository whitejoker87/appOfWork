package ru.jobni.jobni.fragments.api.facebook

interface AuthenticationListenerFB {
    fun onTokenReceived(code: String)
}
