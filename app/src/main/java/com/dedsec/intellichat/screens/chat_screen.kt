package com.dedsec.intellichat.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.ui.theme.loginFont

@Composable
fun ChatScreen(
    navHostController: NavHostController, vm: viewModel, chatId: String

) {
    val message = remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentChat = vm.chats.value.first { it.chatId == chatId }
    val chatUser =
        if (vm.userData.value?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        vm.populateMessages(chatId)
    }

    BackHandler {
        navHostController.popBackStack()
        vm.depopulateMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .safeDrawingPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(28.dp)
                    .clickable {
                        navHostController.popBackStack()
                        vm.depopulateMessages()
                    })

//            Spacer(modifier = Modifier.width(5.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    if (chatUser.imageUrl.isNullOrEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "Profile picture",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    } else {
                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = chatUser.imageUrl),
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        chatUser.name ?: "Unknown",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "MoreVert",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = MaterialTheme.colorScheme.tertiary)

        ) {
            vm.chatMessages.value.forEach { msg ->
                val alignment =
                    if (msg.senderId == vm.userData.value?.userId) Alignment.End else Alignment.Start
                val color =
                    if (msg.senderId == vm.userData.value?.userId) MaterialTheme.colorScheme.secondary else Color.White
                val textColor =
                    if(msg.senderId == vm.userData.value?.userId) Color.White else Color.Black
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp, bottomStart = 25.dp, bottomEnd = 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier.align(alignment)
                    ){
                    Text(
                        msg.message ?: "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp, bottomStart = 25.dp, bottomEnd = 0.dp))
                            .background(color)
                            .align(alignment)
                            .padding(10.dp),
                        fontSize = 18.sp,
                        color = textColor
                    )
                    }
                }

            }
        }
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.tertiary)
                .padding(10.dp)
        ) {
            val suggestionList = vm.smartSugestion.observeAsState()
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .fillMaxWidth()
            ) {
                if (suggestionList.value?.isNotEmpty() == true) {
                    suggestionList.value!!.forEach { item ->
                        Surface(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(width = 1.dp, color = Color.Gray),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    message.value = item.text
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(start = 4.dp, end = 4.dp)
                            ) {
                                Text(
                                    text = item.text,
                                    color = Color.Black,
                                    modifier = Modifier.padding(
                                        start = 10.dp,
                                        end = 8.dp, top = 8.dp, bottom = 8.dp
                                    )
                                )
                            }
                        }

                    }
                }
            }


            TextField(
                value = message.value,
                onValueChange = {
                    message.value = it
                },
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Message here",
                        color = Color.DarkGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = loginFont,
                        letterSpacing = 1.sp
                    )
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = loginFont,
                    letterSpacing = 1.sp,
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(30.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "send",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .clickable {
                                if (message.value.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "Message is empty",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    vm.sendMessage(chatId = chatId, message = message.value)
                                    message.value = ""
                                }
                            }
                    )
                }
            )
        }
    }
}