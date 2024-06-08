package com.dedsec.intellichat.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.data.Person

@Composable
fun ChatScreen(
    navHostController: NavHostController,
    vm: viewModel,
    chatId: String

) {
    val message = remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentChat = vm.chats.value.first { it.chatId == chatId }
    val chatUser =
        if (vm.userData.value?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

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
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navHostController.popBackStack()
                        vm.depopulateMessages()
                    }
            )

            Spacer(modifier = Modifier.width(5.dp))

            if (chatUser.imageUrl.isNullOrEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Profile picture",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = chatUser.imageUrl),
                    contentDescription = "Profile picture",
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    chatUser.name ?: "Unknown",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "MoreVert",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                )
        ) {
            LazyColumn {
                items(vm.chatMessages.value){msg->
                    val alignment = if (msg.senderId == vm.userData.value?.userId) Alignment.End else Alignment.Start
                    val color = if (msg.senderId == vm.userData.value?.userId) Color(0xFFE0E0E0) else Color(0xFF7B90FF)
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ){
                        Text(
                            msg.message ?: "",
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(color)
                                .align(alignment)
                                .padding(8.dp)
                        )
                    }

                }
            }
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "send",
                        modifier = Modifier
                            .clickable {
                                if (message.value.isEmpty()) {
                                    Toast.makeText(context, "Message is empty", Toast.LENGTH_SHORT)
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