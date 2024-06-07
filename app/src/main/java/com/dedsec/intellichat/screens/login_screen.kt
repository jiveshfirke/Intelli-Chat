package com.dedsec.intellichat.screens

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.navigation.Home
import com.dedsec.intellichat.navigation.Login
import com.dedsec.intellichat.navigation.SignUp

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    vm: viewModel
){

    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }


    fun checkForInformation(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()){
            Log.i("Sign up function", "Incomplete information")
            Toast.makeText(context, "Please fill complete information", Toast.LENGTH_LONG).show()
            return
        }
        else{
            vm.loginIn(email = email, password = password)
            navHostController.navigate(Home){
                popUpTo(0)
            }
        }
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
                value =email,
                onValueChange = { email = it},
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(text = "Enter your Email")
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
                    checkForInformation(email, password)
                }
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(10.dp))

            val SignUpText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                ){
                    append("New User? ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontSize = 12.sp
                    )
                ){
                    append("Sign Up")
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

}
