package com.dedsec.intellichat.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dedsec.intellichat.screens.ProfileScreen
import com.dedsec.intellichat.components.viewModel
import com.dedsec.intellichat.screens.ChatScreen
import com.dedsec.intellichat.screens.HomeScreen
import com.dedsec.intellichat.screens.LoginScreen
import com.dedsec.intellichat.screens.SignUpScreen
import com.dedsec.intellichat.screens.StartScreen

@Composable
fun MainNavigation() {
    val vm = hiltViewModel<viewModel>()
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Start) {
        composable(Start) {
            StartScreen(
                navHostController,
                vm
            )
        }
        composable(Home) {
            HomeScreen(
                navHostController,
                vm
            )
        }
        composable("$Chat/{chatId}") {
            val chatId = it.arguments?.getString("chatId")
            chatId.let {
                ChatScreen(
                    navHostController,
                    vm,
                    chatId
                )
            }
        }
        composable(Login) {
            LoginScreen(
                navHostController,
                vm
            )
        }
        composable(SignUp) {
            SignUpScreen(
                navHostController,
                vm
            )
        }
        composable(Profile) {
            ProfileScreen(
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
const val Profile = "profile_screen"