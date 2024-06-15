package com.dedsec.intellichat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.CheckSignedIn
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.navigation.Login
import com.dedsec.intellichat.ui.theme.appNameFont
import com.dedsec.intellichat.ui.theme.loginFont

@Composable
fun StartScreen(
    navHostController: NavHostController,
    vm: viewModel
) {
    CheckSignedIn(vm = vm, navHostController = navHostController)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Image(
            painter = painterResource(id = R.drawable.welcome_page_two),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                text = "IntelliChat",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 50.sp,
                    fontFamily = appNameFont
                ),
                modifier = Modifier
                    .padding(top = 200.dp)
            )

            Spacer(modifier = Modifier.padding(top = 270.dp))
            
            Text(
                text = "It's easy to chat intelligently",
                fontSize = 30.sp,
                fontWeight = FontWeight.W800,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily(Font(R.font.dmserif_text_regular)),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.paddingFromBaseline(20.dp))

            Row (
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ){
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 80.dp,
                                topEnd = 0.dp,
                                bottomEnd = 80.dp
                            )
                        )
                        .background(Color.Green)
                        .size(24.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        Modifier.size(20.dp),
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    "Secure, private messaging",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        fontFamily = appNameFont
                    )
                )

            }
        }

        Button(
            onClick = { navHostController.navigate(Login) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(60.dp)
                .size(70.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp)
        ) {
            Text(
                text = "Get Started",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = loginFont,
                    letterSpacing = 0.8.sp
                )
            )
        }
    }
}