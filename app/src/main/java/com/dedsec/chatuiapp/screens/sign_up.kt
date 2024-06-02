package com.dedsec.chatuiapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dedsec.chatuiapp.R
import com.dedsec.chatuiapp.components.ProgressionBar
import com.dedsec.chatuiapp.components.viewModel
import com.dedsec.chatuiapp.navigation.Login
import com.dedsec.chatuiapp.navigation.SignUp

@Composable
fun SignUpScreen(
    navHostController: NavHostController,
    vm: viewModel
){
    var name by remember {
        mutableStateOf("")
    }

    var phonenumber by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.iconimg),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(42.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = name,
                onValueChange = { name = it},
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Enter your name")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = phonenumber,
                onValueChange = { phonenumber = it},
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Enter your phone number")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value =email,
                onValueChange = { email = it},
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Enter your email")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = password,
                onValueChange = { password = it},
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Enter your password")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    vm.signUp(
                        name,
                        phonenumber,
                        email,
                        password
                    )
                }
            ) {
                Text(text = "Sign up")
            }

            Spacer(modifier = Modifier.height(10.dp))

            val SignUpText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                ){
                    append("Already an user? ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontSize = 12.sp
                    )
                ){
                    append("Login here")
                }
            }

            Text(
                SignUpText,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate(SignUp){
                            popUpTo(Login){
                                inclusive = true
                            }
                        }
                    }
            )
        }
    }

    if (vm.inProgress.value){
        ProgressionBar()
    }
}