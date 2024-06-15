package com.dedsec.intellichat.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.ProgressionBar
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.ui.theme.loginFont

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
        Box {
            Image(
                painter = painterResource(id = R.drawable.profile_screen_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding()
            ) {
                TopSectionOfProfile(
                    navHostController = navHostController,
                    status = status,
                    vm = vm,
                    name = name,
                    number = number
                )
                Spacer(modifier = Modifier.height(30.dp))
                ProfileImage(imageUrl = imageurl, vm = vm)
                Spacer(modifier = Modifier.height(60.dp))
                UserName(name = name)
                UserContact(phonenumber = number)
            }
        }
    }

}

@Composable
fun UserContact(phonenumber: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Phone :",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            color = Color.Black,
            letterSpacing = (-1).sp
        )
        Card(
            colors = CardDefaults.cardColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent
            ),
            border = BorderStroke(2.5.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(30)
        ) {
            TextField(
                value = phonenumber.value,
                onValueChange = { phonenumber.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                )
            )
        }
    }
}

@Composable
fun UserName(name: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Name : ",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            color = Color.Black,
            letterSpacing = (-1).sp
            )
        Card(
            colors = CardDefaults.cardColors(
                contentColor = Color.Black, containerColor = Color.White
            ),
            border = BorderStroke(2.5.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(30)
        ) {
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    letterSpacing = .6.sp
                )
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
                contentColor = Color.Black, containerColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            if (imageUrl.isNullOrEmpty()) {
                Image(painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {}
                        .size(150.dp)
                        .background(Color.White)
                        .padding(bottom = 4.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {}
                        .size(150.dp)
                        .padding(bottom = 4.dp),
                    contentScale = ContentScale.Crop)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                .padding(10.dp)
                .clickable {
                    launcher.launch("image/*")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Change",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = loginFont,
                letterSpacing = (-.6).sp
            )
            Spacer(modifier = Modifier.width(5.dp))
            Image(
                imageVector = Icons.Default.Edit,
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(20.dp)
            )
        }

    }
}

@Composable
private fun TopSectionOfProfile(
    name: MutableState<String>,
    number: MutableState<String>,
    vm: viewModel,
    navHostController: NavHostController,
    status: MutableState<String>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = {
                status.value = "Cancelled"
                navHostController.popBackStack()
            },
            elevation = ButtonDefaults.buttonElevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.White,
            ),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, Color.White),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Cancel",
                fontFamily = loginFont,
                fontSize = 18.sp,
                letterSpacing = (-0.6).sp
            )
        }
        Button(
            onClick = {
                status.value = "Saved"
                vm.createOrUpdateProfile(name = name.value, phonenumber = number.value)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.White
            ),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, Color.White),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Save",
                fontFamily = loginFont,
                fontSize = 18.sp,
                letterSpacing = (-0.6).sp
            )
        }
    }
}