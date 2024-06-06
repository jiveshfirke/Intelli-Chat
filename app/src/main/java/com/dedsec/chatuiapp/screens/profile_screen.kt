package com.dedsec.chatuiapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.chatuiapp.R
import com.dedsec.chatuiapp.components.viewModel
import com.dedsec.chatuiapp.navigation.Login
import com.dedsec.chatuiapp.navigation.SignUp
import com.dedsec.chatuiapp.ui.theme.appNameFont
import com.dedsec.chatuiapp.ui.theme.welcomeFont

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreen() {
    val status = rememberSaveable {
        mutableStateOf("")
    }
    if (status.value.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current, status.value, Toast.LENGTH_SHORT
        ).show()
        status.value = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF092635))
            .verticalScroll(rememberScrollState())
    ) {
        TopSectionOfProfile(status)
        Spacer(modifier = Modifier.height(60.dp))
        ProfileImage()
        Spacer(modifier = Modifier.height(60.dp))
        UserName()
        Spacer(modifier = Modifier.height(30.dp))
        UserContact()

    }

}

@Composable
fun UserContact() {
    var phone by rememberSaveable {
        mutableStateOf("")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(15.dp).fillMaxWidth()
    ) {
        Text(
            text = "Phone",
            fontSize = 30.sp,
            fontWeight = FontWeight.W800,
            fontFamily = welcomeFont,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.Black,
                containerColor = Color.White
            )
        ) {
            TextField(
                value = phone,
                onValueChange = { phone = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
fun UserName() {
    var name by rememberSaveable {
        mutableStateOf("")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(15.dp).fillMaxWidth()
    ) {
        Text(
            text = "Name",
            fontSize = 30.sp,
            fontWeight = FontWeight.W800,
            fontFamily = welcomeFont,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.Black,
                containerColor = Color.White
            )
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
fun ProfileImage() {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = painterResource(id = R.drawable.ic_user)
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(0.dp)
                .size(150.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.White, containerColor = Color.White
            )
        ) {
            Image(painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { }
                    .size(150.dp)
                    .padding(bottom = 4.dp),
                contentScale = ContentScale.FillBounds)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.clickable { /* new profile to be changed */ }
        ) {
            Text(
                text = "Change",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(22.dp)
            )
        }

    }
}

@Composable
private fun TopSectionOfProfile(status: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { status.value = "Cancelled" },
            elevation = ButtonDefaults.buttonElevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.White
            ),
        ) {
            Text(
                text = "Cancel", fontFamily = appNameFont, letterSpacing = 1.sp, fontSize = 30.sp
            )
        }
        Button(
            onClick = { status.value = "Saved" }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.White
            )
        ) {
            Text(
                text = "Save", fontFamily = appNameFont, letterSpacing = 1.sp, fontSize = 30.sp
            )
        }
    }
}