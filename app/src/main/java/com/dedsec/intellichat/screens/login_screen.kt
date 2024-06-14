package com.dedsec.intellichat.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.navigation.Home
import com.dedsec.intellichat.navigation.Login
import com.dedsec.intellichat.navigation.SignUp
import com.dedsec.intellichat.ui.theme.loginFont
import com.dedsec.intellichat.ui.theme.singUpTextFont

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    vm: viewModel
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val imeins= WindowInsets.ime
    val imeHeight = with(LocalDensity.current) { imeins.getBottom(this).toDp() }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun checkForInformation(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Log.i("Login in function", "Incomplete information")
            Toast.makeText(context, "Please fill complete information", Toast.LENGTH_LONG).show()
            return
        } else {
            vm.loginIn(email = email, password = password, navHostController = navHostController, context = context)
        }
    }

    Box{
        BackgroundImageLogin()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = imeHeight)
                .verticalScroll(scrollState),
        ) {

            Spacer(Modifier.height(10.dp))
            LoginText()

            Column (
                modifier = Modifier
                    .padding(43.dp)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    shape = RoundedCornerShape(100),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Enter your Email",
                            color = Color(0xFFB4B4B3),
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                    },
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email_foreground),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.W400,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(100),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Enter your Password",
                            color = Color(0xFFB4B4B3),
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock_password_foreground),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.W400,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            keyboardController?.hide()
                        }
                    ),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (isPasswordVisible) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { isPasswordVisible = false }
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_visibility_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { isPasswordVisible = true }
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))
                Spacer(modifier = Modifier.height(220.dp))

                ElevatedButton(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    onClick = {
                        checkForInformation(email, password)
                    }
                ) {
                    Text(
                        text = "SIGN IN",
                        fontSize = 30.sp,
                        color = Color.Black,
                        fontFamily = loginFont
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                val signUpText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = singUpTextFont,
                            letterSpacing = 0.8.sp
                        )
                    ) {
                        append("New User? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 22.sp,
                            fontFamily = singUpTextFont,
                            textDecoration = TextDecoration.Underline,
                            letterSpacing = 0.8.sp
                        )
                    ) {
                        append("Sign Up")
                    }
                }

                Text(
                    text = signUpText,
                    modifier = Modifier
                        .clickable {
                            navHostController.navigate(SignUp) {
                                popUpTo(Login) {
                                    inclusive = true
                                }
                            }
                        },
                )
            }
        }
    }

}

@Composable
private fun LoginText() {
    Column {
        Text(
            text = "Welcome",
            fontSize = 55.sp,
            color = Color(0xFF171624),
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding(top = 100.dp, start = 15.dp)
        )
        Text(
            text = "Back",
            fontSize = 50.sp,
            color = Color(0xFF171624),
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding(top = 0.dp, start = 15.dp)
        )
        Text(
            text = "Hey! Good to see you again",
            fontSize = 20.sp,
            color = Color(0xFF171624),
            fontWeight = FontWeight.Normal,
            fontFamily = loginFont,
            modifier = Modifier
                .padding(top = 0.dp, start = 15.dp)
        )
    }
}

@Composable
fun BackgroundImageLogin() {
    Image(
        painter = painterResource(id = R.drawable.login_screen_background),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
