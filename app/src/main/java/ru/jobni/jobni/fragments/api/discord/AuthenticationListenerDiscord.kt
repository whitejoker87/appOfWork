package ru.jobni.jobni.fragments.api.discord

interface AuthenticationListenerDiscord {
    fun onTokenReceived(code: String)
}
