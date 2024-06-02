package com.dedsec.chatuiapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.chatuiapp.R
import com.dedsec.chatuiapp.components.viewModel
import com.dedsec.chatuiapp.data.Person

@Composable
fun ChatScreen(
    navHostController: NavHostController,
    vm: viewModel
) {
    var message = remember { mutableStateOf("") }
    val person = navHostController.previousBackStackEntry!!.savedStateHandle.get<Person>("data") ?: Person()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        Row (
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navHostController.popBackStack()
                    }
            )

            Spacer(modifier = Modifier.width(5.dp))

            Image(painter = painterResource(id = person!!.icon), contentDescription = "Person picture", modifier = Modifier.size(42.dp))

            Spacer(modifier = Modifier.width(10.dp))

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        person.name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        "Online",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    )
                }

                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "MoreVert", tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }

        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                )
        ){
            TextField(
                value = message.value,
                onValueChange = {
                                message.value = it
                },
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Message here")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                },
                trailingIcon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_mic_24), contentDescription = "mic")
                }
            )
        }
    }
}