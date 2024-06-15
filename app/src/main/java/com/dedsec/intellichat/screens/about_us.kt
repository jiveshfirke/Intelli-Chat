package com.dedsec.intellichat.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.UiContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dedsec.intellichat.R
import com.dedsec.intellichat.components.viewModel

@Composable
fun About_Us(navHostController: NavHostController, vm: viewModel) {
    val context = LocalContext.current
    Scaffold (
        topBar = {
            Row (
                modifier = Modifier
                    .safeDrawingPadding()
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .clickable {
                            navHostController.popBackStack()
                        }
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "About Us",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        content = { padding->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SingleTeammate(image = R.drawable.jivesh, name = "Jivesh Firke", lnkdn = "http://www.linkedin.com/in/jiveshfirke", context = context)
                Spacer(modifier = Modifier.height(50.dp))
                SingleTeammate(image = R.drawable.brijeshwar, name = "Brijeshwar Gupta", lnkdn = "https://www.linkedin.com/in/brijeshwar-gupta-39441324b?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app", context = context)
            }
        }
    )


}

@Composable
fun SingleTeammate(@DrawableRes image: Int, name: String, lnkdn: String, context: Context) {
    Column(
        modifier = Modifier
            .heightIn(max = 300.dp)
            .widthIn(max = 200.dp)
            .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .weight(0.75f)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        )
        Text(
            name,
            color = MaterialTheme.colorScheme.secondary,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            ),
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        )

        Text(
            "LinkedIn",
            color = MaterialTheme.colorScheme.secondary,
            style = TextStyle(
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(lnkdn))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
        )

    }
}