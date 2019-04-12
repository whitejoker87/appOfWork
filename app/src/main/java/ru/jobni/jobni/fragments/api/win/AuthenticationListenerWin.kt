package ru.jobni.jobni.fragments.api.win

interface AuthenticationListenerWin {
    fun onTokenReceived(code: String)
}
