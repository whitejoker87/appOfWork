package ru.jobni.jobni.fragments.api.instagram

interface AuthenticationListener {
    fun onTokenReceived(code: String)
}
