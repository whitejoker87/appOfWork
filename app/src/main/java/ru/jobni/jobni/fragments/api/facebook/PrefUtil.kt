package ru.jobni.jobni.fragments.api.facebook

import android.app.Activity
import android.preference.PreferenceManager
import android.util.Log

class PrefUtil(private val activity: Activity) {

    val token: String?
        get() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            return prefs.getString("fb_access_token", null)
        }

    fun saveAccessToken(token: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = prefs.edit()
        editor.putString("fb_access_token", token)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun clearToken() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = prefs.edit()
        editor.clear()
        editor.apply() // This line is IMPORTANT !!!
    }

    fun saveFacebookUserInfo(first_name: String, last_name: String, email: String, gender: String, profileURL: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = prefs.edit()
        editor.putString("fb_first_name", first_name)
        editor.putString("fb_last_name", last_name)
        editor.putString("fb_email", email)
        editor.putString("fb_gender", gender)
        editor.putString("fb_profileURL", profileURL)
        editor.apply() // This line is IMPORTANT !!!
        Log.d("MyApp", "Shared Name : $first_name\nLast Name : $last_name\nEmail : $email\nGender : $gender\nProfile Pic : $profileURL")
    }

    fun getFacebookUserInfo() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        Log.d("MyApp", "Name : " + prefs.getString("fb_name", null) + "\nEmail : " + prefs.getString("fb_email", null))
    }
}
