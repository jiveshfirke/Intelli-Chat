package com.dedsec.intellichat.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.ChatUser
import com.dedsec.intellichat.components.ProgressionBar
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.navigation.Chat
import com.dedsec.intellichat.navigation.Profile
import com.dedsec.intellichat.navigation.Single_Status
import com.dedsec.intellichat.navigation.Start
import com.dedsec.intellichat.navigation.About_Us
import com.dedsec.intellichat.ui.theme.RedDark
import com.dedsec.intellichat.ui.theme.RedLight
import com.dedsec.intellichat.ui.theme.RedNormal
import com.dedsec.intellichat.ui.theme.RedNormal2

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    vm: viewModel
) {
    val isVisible = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .safeDrawingPadding()
            .padding(top = 15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Header(vm.userData.value?.name)

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .background(RedNormal)
                        .fillMaxWidth()
                ) {
                    item {
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri ->
                            uri?.let {
                                vm.uploadStatus(it)
                            }
                        }
                        AddStoryLayout(onClick = { launcher.launch("image/*") })
                    }
                    val statusList = vm.statusList.value
                    val userData = vm.userData.value
                    val myStatus = statusList.filter {
                        it.user.userId == userData?.userId
                    }

                    val otherStatus = statusList.filter {
                        it.user.userId != userData?.userId
                    }
                    if (vm.inProgressStatus.value) {
                        item {
                            ProgressionBar()
                        }
                    } else {
                        if (myStatus.isNotEmpty()) {
                            item {
                                UserStoryLayout(
                                    name = myStatus[0].user.name,
                                    imageUrl = myStatus[0].user.imageUrl,
                                    onClick = { navHostController.navigate("$Single_Status/${userData?.userId}") }
                                )
                            }
                        }
                        val uniqueUsers = otherStatus.map { it.user }.toSet().toList()
                        items(uniqueUsers) { user ->
                            UserStoryLayout(
                                name = user.name,
                                imageUrl = user.imageUrl,
                                onClick = { navHostController.navigate("$Single_Status/${user.userId}") }
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 0.dp)
                    .background(RedNormal)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 60.dp,
                            topEnd = 60.dp
                        )
                    )
                    .background(RedLight),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
//                    BottomSheet()
                    val showDialog = remember {
                        mutableStateOf(false)
                    }

                    AddChatRow(
                        showDialog = showDialog,
                        vm = vm
                    )

                    if (vm.inProgressChat.value) {
                        ProgressionBar()
                    } else {
                        val chats = vm.chats.value

                        if (chats.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No chats available")
                            }
                        } else {
                            LazyColumn {
                                items(chats) { chat ->
                                    val chatuser =
                                        if (chat.user1.userId == vm.userData.value?.userId) {
                                            chat.user2
                                        } else {
                                            chat.user1
                                        }
                                    UserRow(chatuser = chatuser) {
                                        chat.chatId?.let {
                                            navHostController.navigate("$Chat/$it")
                                        }
                                    }
                                }
//                        items(personList, key = {it.id }){
//                            UserRow(person = it){
//                                navHostController.currentBackStackEntry?.savedStateHandle?.set("data", it)
//                                navHostController.navigate(Chat)
//                            }
//                        }
                            }
                        }

                    }

                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .clickable {
                        isVisible.value = !isVisible.value
                    }
                    .padding(end = 10.dp)
            )
            DropdownMenu(
                expanded = isVisible.value,
                onDismissRequest = { isVisible.value = !isVisible.value },
                modifier = Modifier
                    .background(Color.White),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                            letterSpacing = 1.sp
                        )
                    },
                    onClick = {
                        navHostController.navigate(Profile)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "About us",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                            letterSpacing = 1.sp
                        )
                    },
                    onClick = {
                        navHostController.navigate(About_Us)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Logout",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                            letterSpacing = 1.sp
                        )
                    },
                    onClick = {
                        vm.signOut()
                        navHostController.navigate(Start) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun addChatFunction(
    showDialog: Boolean,
    addChat: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                addChatNumber.value = ""
            },
            containerColor = Color.White,
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        addChat(addChatNumber.value)
                    },
                    modifier = Modifier,
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFFF6EAF)),
                ) {
                    Text(
                        text = "Add",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_regular))
                    )
                }
            },
            title = {
                Text(
                    text = "Enter Phone Number",
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    letterSpacing = (-1).sp
                )
            },
            text = {
                TextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                    )
                )
            }
        )
    }
}

@Composable
fun AddChatRow(
    showDialog: MutableState<Boolean>,
    vm: viewModel
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(70.dp)
            .padding(top = 12.dp)
    ) {

        OutlinedButton(
            onClick = { showDialog.value = true },
            modifier = Modifier,
            border = BorderStroke(width = 2.dp, color = RedDark),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Add Chat",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                letterSpacing = 0.1.sp,
            )
        }
    }
    if (showDialog.value) {
        addChatFunction(
            showDialog = showDialog.value,
            addChat = {
                if (it.length < 10) {
                    Toast.makeText(context, "Please add a valid number", Toast.LENGTH_SHORT).show()
                } else {
                    vm.addChat(addChatNumber = it)
                    showDialog.value = false
                }
            },
            onDismiss = {
                showDialog.value = false
            }
        )
    }
    HorizontalDivider(
        thickness = 0.8.dp,
        color = Color.LightGray,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 30.dp, end = 30.dp, bottom = 10.dp, top = 10.dp)
    )
}

@Composable
fun UserRow(chatuser: ChatUser, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .height(70.dp)
                .padding(start = 24.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chatuser.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Person Profile",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = chatuser.imageUrl),
                    contentDescription = "Person Profile",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Text(
                chatuser.name ?: "",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W800,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    letterSpacing = 0.6.sp
                )
            )
        }
        HorizontalDivider(
            thickness = 0.8.dp,
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 30.dp, end = 30.dp, top = 10.dp)
        )

}

@Composable
fun Header(name: String?) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.W300
            )
        ) {
            append("Welcome back, ")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(name ?: "")
        }
    }
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 20.dp)
    )
}

@Composable
fun AddStoryLayout(onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 16.dp, top = 30.dp, bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
//                .border(width = 3.dp, color = Color.Black, shape = CircleShape)
                .background(Color.White, CircleShape)
                .size(70.dp)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.Black,
                modifier = Modifier
                    .size(45.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Add Status",
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp
            ),
            modifier = Modifier
        )
    }
}

@Composable
fun UserStoryLayout(name: String?, imageUrl: String?, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(start = 16.dp, top = 30.dp, bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, CircleShape)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Person DP",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Person DP",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            name ?: "",
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}
