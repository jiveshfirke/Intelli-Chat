package com.dedsec.intellichat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.CheckSignedIn
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.navigation.Login

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "STAY CONNECTED\nWITH YOUR FRIENDS\nAND FAMILY",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            )

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
                        .background(Color(0XFF00FFA3))
                        .size(24.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        Modifier.size(20.dp),
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    "Secure, private messaging",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

            }
        }

        Button(
            onClick = { navHostController.navigate(Login) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp, 15.dp)
        ) {
            Text(
                text = "Get Started",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}