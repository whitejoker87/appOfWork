package ru.jobni.jobni.fragments.api.mailru

interface AuthenticationListenerMailru {
    fun onTokenReceived(accessToken: String, vid: String)
}
