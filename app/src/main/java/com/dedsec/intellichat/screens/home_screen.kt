package com.dedsec.intellichat.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.ChatUser
import com.dedsec.intellichat.components.ProgressionBar
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.data.Person
import com.dedsec.intellichat.data.personList
import com.dedsec.intellichat.navigation.Chat
import com.dedsec.intellichat.navigation.Profile
import com.dedsec.intellichat.navigation.Start

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
            .background(Color.Black)
            .safeDrawingPadding()
            .padding(top = 15.dp)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
        ){
            Header()

            LazyRow {
                item {
                    AddStoryLayout()
                    Spacer(modifier = Modifier.width(10.dp))
                }
                items(personList, key = {it.id}){
                    UserStoryLayout(person = it)
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp
                        )
                    )
                    .background(Color.White),
                contentAlignment = Alignment.TopCenter
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheet()
                    val showDialog = remember {
                        mutableStateOf(false)
                    }

                    AddChatRow(
                        showDialog = showDialog,
                        vm = vm
                    )

                    if (vm.inProgressChat.value){
                        ProgressionBar()
                    }else {
                        val chats = vm.chats.value

                        if (chats.isEmpty()){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = "No chats available")
                            }
                        }else{
                            LazyColumn {
                                items(chats){chat->
                                    val chatuser = if (chat.user1.userId == vm.userData.value?.userId){
                                        chat.user2
                                    }else{
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
                tint = Color.White,
                modifier = Modifier
                    .clickable {
                        isVisible.value = !isVisible.value
                    }
            )
            DropdownMenu(
                expanded = isVisible.value,
                onDismissRequest = { isVisible.value = !isVisible.value},
                modifier = Modifier,

            ){
                DropdownMenuItem(
                    text = { Text(text = "Profile")},
                    onClick = {
                        navHostController.navigate(Profile)
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Logout")},
                    onClick = {
                        vm.signOut()
                        navHostController.navigate(Start){
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
    addChat: (String)-> Unit,
    onDismiss: ()-> Unit,
){
    val addChatNumber = remember {
        mutableStateOf("")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                addChatNumber.value = ""
            },
            confirmButton = {
                Button(
                    onClick = {
                        addChat(addChatNumber.value)
                    }
                ) {
                    Text(
                        "Add"
                    )
                }
            },
            title = {
                Text(text = "Add Chat")
            },
            text = {
                TextField(
                    value = addChatNumber.value,
                    onValueChange = {addChatNumber.value = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                showDialog.value = true
            }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Add Chat",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth(0.9f))
    }
    if (showDialog.value){
        addChatFunction(
            showDialog = showDialog.value,
            addChat = {
                if (it.length < 10){
                    Toast.makeText(context, "Please add a valid number", Toast.LENGTH_SHORT).show()
                }else {
                    vm.addChat(addChatNumber = it)
                    showDialog.value = false
                }
            },
            onDismiss = {
                showDialog.value = false
            }
        )
    }
}

@Composable
fun UserRow(chatuser: ChatUser, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ){
        Row(
            Modifier
                .height(80.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chatuser.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Person Profile",
                    modifier = Modifier
                        .size(50.dp)
                )
            }else{
                Image(
                    painter = rememberAsyncImagePainter(model = chatuser.imageUrl),
                    contentDescription = "Person Profile",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Text(
                chatuser.name?: "",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth(0.9f))
    }
}

@Composable
fun BottomSheet(){
    Box(
        modifier = Modifier
            .padding(top = 15.dp)
            .clip(RoundedCornerShape(90.dp))
            .background(Color.Gray)
            .width(90.dp)
            .height(5.dp)
    )
}

@Composable
fun Header(){
    val text = buildAnnotatedString {
        withStyle(
            style =  SpanStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.W300
            )
        ){
            append("Welcome back, ")
        }
        withStyle(
            style = SpanStyle(
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        ){
            append("Jivesh")
        }
    }
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 20.dp)
    )
}

@Composable
fun AddStoryLayout(){
    Column (
        modifier = Modifier
            .padding(start = 20.dp)
    ){
        Box(
            modifier = Modifier
                .border(2.dp, Color.Yellow, CircleShape)
                .background(Color.Yellow, CircleShape)
                .size(70.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(12.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Add Story",
            style = TextStyle(
                color = Color.White,
                fontSize = 13.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun UserStoryLayout(person: Person){
    Column (
        modifier = Modifier
            .padding(horizontal = 10.dp)
    ){
        Box(
            modifier = Modifier
                .border(2.dp, Color.Yellow, CircleShape)
                .background(Color.Yellow, CircleShape)
                .size(70.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = person.icon),
                contentDescription = "Person DP",
                modifier = Modifier
                    .size(65.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            person.name,
            style = TextStyle(
                color = Color.White,
                fontSize = 13.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}
