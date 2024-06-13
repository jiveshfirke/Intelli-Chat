package com.dedsec.intellichat.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.components.viewModel


enum class State {
    INITIAL, ACTIVE, COMPLETED
}

@Composable
fun SingleStatusScreen(navHostController: NavHostController, vm: viewModel, userId: String) {
    val status = vm.statusList.value.filter {
        it.user.userId == userId
    }

    if (status.isNotEmpty()) {
        val currentStatus = remember {
            mutableStateOf(0)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = status[currentStatus.value].imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Log.i("singelstatus", "${status[currentStatus.value].imageUrl}")
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                status.forEachIndexed { index, statusData ->
                    CustomProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .height(7.dp)
                            .padding(1.dp),
                        state = if (currentStatus.value < index) State.INITIAL else if (currentStatus.value == index) State.ACTIVE else State.COMPLETED
                    ) {
                        if (currentStatus.value < status.size - 1) currentStatus.value++ else navHostController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
fun CustomProgressIndicator(modifier: Modifier, state: State, onCompleted: () -> Unit) {
    var progress = if (state == State.INITIAL) 0f else 1f

    if (state == State.ACTIVE) {
        val toggleState = remember {
            mutableStateOf(false)
        }

        LaunchedEffect(toggleState) {
            toggleState.value = true
        }

        val p: Float by animateFloatAsState(if (toggleState.value) 1f else 0f,
            animationSpec = tween(5000),
            finishedListener = { onCompleted() })
        progress = p
    }

    LinearProgressIndicator(
        progress = { progress }, modifier = modifier, color = Color.Yellow
    )

}