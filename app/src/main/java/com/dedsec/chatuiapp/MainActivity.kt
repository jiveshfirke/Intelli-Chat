package com.dedsec.chatuiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import com.dedsec.chatuiapp.components.viewModel
import com.dedsec.chatuiapp.navigation.MainNavigation
import com.dedsec.chatuiapp.screens.Login_Screen
import com.dedsec.chatuiapp.screens.ProfileScreen
import com.dedsec.chatuiapp.ui.theme.ChatUIAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatUIAppTheme {
//                MainNavigation()
//                Login_Screen()
                ProfileScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatUIAppTheme {
        MainNavigation()
    }
}


// jivesh

