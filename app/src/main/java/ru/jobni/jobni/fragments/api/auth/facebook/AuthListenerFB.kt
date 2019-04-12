package ru.jobni.jobni.fragments.api.auth.facebook

interface AuthListenerFB {
    fun onTokenReceived(code: String)
}
