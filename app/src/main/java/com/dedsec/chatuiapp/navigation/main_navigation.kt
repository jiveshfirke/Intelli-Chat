package com.dedsec.chatuiapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dedsec.chatuiapp.components.viewModel
import com.dedsec.chatuiapp.screens.ChatScreen
import com.dedsec.chatuiapp.screens.HomeScreen
import com.dedsec.chatuiapp.screens.LoginScreen
import com.dedsec.chatuiapp.screens.SignUpScreen
import com.dedsec.chatuiapp.screens.StartScreen

@Composable
fun MainNavigation() {
    val vm = hiltViewModel<viewModel>()
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Start) {
        composable(Start){
            StartScreen(
                navHostController,
                vm
            )
        }
        composable(Home){
            HomeScreen(
                navHostController,
                vm
            )
        }
        composable(Chat){
            ChatScreen(
                navHostController,
                vm
            )
        }
        composable(Login){
            LoginScreen(
                navHostController,
                vm
            )
        }
        composable(SignUp){
            SignUpScreen(
                navHostController,
                vm
            )
        }
    }
}

const val Start = "start_screen"
const val Home = "home_screen"
const val Chat = "chat_screen"
const val Login = "login_screen"
const val SignUp = "signup_screen"