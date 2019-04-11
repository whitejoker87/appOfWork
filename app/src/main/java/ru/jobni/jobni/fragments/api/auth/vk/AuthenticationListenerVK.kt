package ru.jobni.jobni.fragments.api.auth.vk

interface AuthenticationListenerVK {
    fun onTokenReceived(code: String)
}
