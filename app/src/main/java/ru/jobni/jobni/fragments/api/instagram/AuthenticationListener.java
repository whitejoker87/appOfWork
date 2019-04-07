package ru.jobni.jobni.fragments.api.instagram;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}
