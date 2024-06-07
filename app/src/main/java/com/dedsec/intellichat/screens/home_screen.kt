package com.dedsec.intellichat.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
                    LazyColumn {
                        items(personList, key = {it.id }){
                            UserRow(person = it){
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("data", it)
                                navHostController.navigate(Chat)
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
fun UserRow(person: Person, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ){
        Row(
            Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = person.icon),
                contentDescription = "Person Profile",
                modifier = Modifier
                    .size(65.dp)
            )
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Text(
                        person.name,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "Okay",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                        )
                    )

                }
                Text(text = "12.23pm")

            }
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
