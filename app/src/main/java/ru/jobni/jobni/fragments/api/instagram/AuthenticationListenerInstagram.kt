package ru.jobni.jobni.fragments.api.instagram

interface AuthenticationListenerInstagram {
    fun onTokenReceived(code: String)
}
