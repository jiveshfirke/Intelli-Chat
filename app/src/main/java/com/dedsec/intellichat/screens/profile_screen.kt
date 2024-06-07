package com.dedsec.intellichat.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.ProgressionBar
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.ui.theme.appNameFont
import com.dedsec.intellichat.ui.theme.welcomeFont

@Composable
fun ProfileScreen(navHostController: NavHostController, vm: viewModel) {
    val status = rememberSaveable {
        mutableStateOf("")
    }
    if (status.value.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current, status.value, Toast.LENGTH_SHORT
        ).show()
        status.value = ""
    }
    val imageurl = vm.userData.value?.imageUrl
    val userData = vm.userData.value
    val name = rememberSaveable {
        mutableStateOf(userData?.name ?: "")
    }
    val number = rememberSaveable {
        mutableStateOf(userData?.phonenumber ?: "")
    }
    if (vm.inProgress.value) {
        ProgressionBar()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF092635))
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
        ) {
            TopSectionOfProfile(navHostController = navHostController, status = status, vm = vm, name = name, number = number)
            Spacer(modifier = Modifier.height(60.dp))
            ProfileImage(imageUrl = imageurl, vm = vm)
            Spacer(modifier = Modifier.height(60.dp))
            UserName(name = name)
            Spacer(modifier = Modifier.height(30.dp))
            UserContact(phonenumber = number)

        }
    }

}

@Composable
fun UserContact(phonenumber: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
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
            elevation = CardDefaults.cardElevation(10.dp), colors = CardDefaults.cardColors(
                contentColor = Color.Black, containerColor = Color.White
            )
        ) {
            TextField(
                value = phonenumber.value,
                onValueChange = { phonenumber.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
fun UserName(name: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
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
            elevation = CardDefaults.cardElevation(10.dp), colors = CardDefaults.cardColors(
                contentColor = Color.Black, containerColor = Color.White
            )
        ) {
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: viewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }
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
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "",
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {}
                    .size(150.dp)
                    .padding(bottom = 4.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.clickable {
            launcher.launch("image/*")
        }) {
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
private fun TopSectionOfProfile(name: MutableState<String>, number: MutableState<String>, vm: viewModel, navHostController: NavHostController, status: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                status.value = "Cancelled"
                navHostController.popBackStack()
                      },
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
            onClick = {
                status.value = "Saved"
                vm.createOrUpdateProfile(name = name.value, phonenumber = number.value)
                      },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Save", fontFamily = appNameFont, letterSpacing = 1.sp, fontSize = 30.sp
            )
        }
    }
}