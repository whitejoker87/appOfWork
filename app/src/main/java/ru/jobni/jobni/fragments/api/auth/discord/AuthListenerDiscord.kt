package ru.jobni.jobni.fragments.api.auth.discord

interface AuthListenerDiscord {
    fun onTokenReceived(code: String)
}
