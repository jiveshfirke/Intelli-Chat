package com.dedsec.chatuiapp.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class viewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {

    var inProgress = mutableStateOf(false)
    var eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        Log.i("current user", "checking $currentUser")
        currentUser?.uid.let {
            inProgress.value = true
            if (it != null) {
                db.collection("User").document(it).addSnapshotListener{ snapshot, e ->
                    if (e != null) {
                        Log.i("Db collection", "Failed retrieving user data $e")
                    } else{
                        if (snapshot != null && snapshot.exists()) {
                            userData.value = snapshot.toObject(UserData::class.java)
                            inProgress.value  = false
                        }
                    }
                }
            }else{
                Log.i("Db collection", "Failed retrieving user data, uid is null")
                inProgress.value = false
            }
        }
    }

    fun signUp(name: String, phonenumber: String, email: String, password: String) {
        inProgress.value = true

        db.collection("User").whereEqualTo("number", phonenumber).get()
            .addOnSuccessListener {
                if (it.isEmpty){
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                createOrUpdateProfile(name, phonenumber)
                                signIn.value = true
                                Log.i("Hello", "Success")
                            } else {
                                // Handle unsuccessful sign-up
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i("Hello", "Failed $exception")
                            inProgress.value = false
                        }
                }else{
                    Log.i("viewModel", "number already exists")
                    inProgress.value = false
                }
            }
    }

    fun signOut(){
        signIn.value = false
        auth.signOut()
    }

    private fun createOrUpdateProfile(name: String? = null, phonenumber: String? = null, imageUrl: String? = null) {
        var uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            phonenumber = phonenumber?: userData.value?.phonenumber,
            imageUrl = imageUrl?: userData.value?.imageUrl
        )

        Log.i("RR", "Success create profile")

        uid?.let {
            inProgress.value = true
            db.collection("User").document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        //
                    }else{
                        db.collection("User").document(uid).set(userData)
                         inProgress.value = false
                    }
                }

                .addOnFailureListener{
                    Log.i("Db collection", "Failed $it")
                }
        }
    }

}