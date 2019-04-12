package ru.jobni.jobni.fragments.api.auth.vk

interface AuthListenerVK {
    fun onTokenReceived(code: String)
}
