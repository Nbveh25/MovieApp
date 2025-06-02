package com.example.homework.presentaion.screens.authScreen

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthScreen(
    context: Context,
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit
) {
    Button(onClick = {
        viewModel.setAuthorized(context, true)
        onAuthSuccess()
    }) {
        Text("Войти")
    }
}