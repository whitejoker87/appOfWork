package ru.jobni.jobni.fragments.api.instagram

interface AuthenticationListener {
    fun onTokenReceived(auth_token: String)
}
