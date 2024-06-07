package com.dedsec.chatuiapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

val welcomeText = buildAnnotatedString {
    withStyle(
        style = SpanStyle(
            fontFamily = FontFamily.Cursive,
            fontSize = 52.sp,
            fontWeight = FontWeight.W900,
            color = Color.White,
        )
    ) {
        append(
            "Welcome To"
        )
    }
}
val appName = buildAnnotatedString {
    withStyle(
        style = SpanStyle(
            fontFamily = appNameFont,
            fontSize = 45.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp,
            color = Color.White
        )
    ) {
        append("IntelliChats")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Login_Screen() {

    Surface{
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {

            BackgroundImage()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopSection()
                Spacer(modifier = Modifier.height(100.dp))
                BottomSection()
            }
        }
    }
}

@Composable
fun BottomSection() {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(text = "Enter your email")
            },
            shape = RoundedCornerShape(70),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
            )
        )
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.login_screen_bg),
        contentDescription = "",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
            .rotate(180f)
    )
}

@Composable
private fun TopSection() {
    Text(
        text = welcomeText,
        modifier = Modifier
            .padding(top = 100.dp)
    )
    Spacer(modifier = Modifier.padding(5.dp))
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.iconimg),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .height(60.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = appName,
            modifier = Modifier
        )
    }
    Spacer(modifier = Modifier.padding(top = 120.dp))
    Text(
        text = "Login",
        fontSize = 40.sp,
        fontWeight = FontWeight.W900,
        color = Color.White,
        fontFamily = welcomeFont
    )
}
