package ru.jobni.jobni.fragments.api.ok

interface AuthenticationListenerOK {
    fun onTokenReceived(accessToken: String, vid: String)
}
