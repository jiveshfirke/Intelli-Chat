package com.dedsec.chatuiapp.components

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class viewModel @Inject constructor(
    val auth: FirebaseAuth
): ViewModel() {

    init {

    }

    var inProgress = mutableStateOf(false)
    var eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)

    fun signUp(name: String,phonenumer:String, email: String, password: String){
        inProgress.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signIn.value = true
                    createOrUpdateProfile()
                    Log.i("Hello", "Success")
                } else {
                    // Handle unsuccessful sign-up
                }
            }
            .addOnFailureListener { exception ->
                Log.i("Hello", "Failed $email h")
            }
    }

    private fun createOrUpdateProfile() {

    }

}