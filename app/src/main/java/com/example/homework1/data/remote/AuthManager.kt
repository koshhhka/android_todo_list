package com.example.homework1.data.remote

import android.content.Context
import android.content.SharedPreferences

class AuthManager(private val context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val TOKEN_KEY = "bearer_token"
    
    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }
    
    fun setToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
        NetworkModule.setBearerToken(token)
    }
    
    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
        NetworkModule.setBearerToken("")
    }
    
    fun initializeToken() {
        getToken()?.let { token ->
            NetworkModule.setBearerToken(token)
        }
    }
}

