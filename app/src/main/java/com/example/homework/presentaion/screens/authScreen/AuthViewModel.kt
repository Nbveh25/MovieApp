package com.example.homework.presentaion.screens.authScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    fun setAuthorized(context: Context, authorized: Boolean) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit() { putBoolean("is_authorized", authorized) }
    }

    fun isAuthorized(context: Context): Boolean {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_authorized", false)
    }
}