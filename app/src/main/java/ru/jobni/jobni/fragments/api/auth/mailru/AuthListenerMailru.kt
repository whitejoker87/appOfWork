package ru.jobni.jobni.fragments.api.auth.mailru

interface AuthListenerMailru {
    fun onTokenReceived(code: String)
}
