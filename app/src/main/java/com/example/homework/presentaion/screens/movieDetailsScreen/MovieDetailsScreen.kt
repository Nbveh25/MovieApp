package com.example.homework.presentaion.screens.movieDetailsScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.app.R
import com.example.homework.presentaion.component.ErrorScreen
import com.example.homework.presentaion.component.MovieDetailsContent
import com.example.homework.presentaion.component.ShimmerDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val uiMessage = viewModel.uiMessage.collectAsState().value

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val currentState = state.value) {
            is MovieDetailsState.Loading -> ShimmerDetails()
            is MovieDetailsState.Success -> MovieDetailsContent(
                movie = currentState.result.details,
                modifier = Modifier.padding(paddingValues)
            )
            is MovieDetailsState.Error -> ErrorScreen(
                message = currentState.message,
                onRetry = viewModel::loadDetails
            )
        }
    }
}