package com.dedsec.intellichat.components

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.dedsec.intellichat.navigation.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class viewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProgress = mutableStateOf(false)
    var inProgressChat = mutableStateOf(false)
    var eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        Log.i("viewModel", "Initializing view model")
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun loginIn(email: String, password: String) {
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Log.i("viewModel", "Successful login")
                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    Log.i("viewModel", "Login Failed")
                }
            }
    }

    fun signUp(name: String, phonenumber: String, email: String, password: String, navHostController: NavHostController) {
        inProgress.value = true

        db.collection("User").whereEqualTo("phonenumber", phonenumber).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                createOrUpdateProfile(name, phonenumber)
                                signIn.value = true
                                navHostController.navigate(Home){
                                    popUpTo(0)
                                }
                                Log.i("viewModel", "Successfully created user")

                            } else {
                                // Handle unsuccessful sign-up
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i("viewModel", "user creation failed $exception")
                            inProgress.value = false
                        }
                } else {
                    Log.i("viewModel", "Number already exists")
                    inProgress.value = false
                }
            }
    }

    fun signOut() {
        signIn.value = false
        auth.signOut()
        userData.value = null
        Log.i("viewModel", "Successfully signed out")
    }

    fun createOrUpdateProfile(
        name: String? = null,
        phonenumber: String? = null,
        imageUrl: String? = null
    ) {
        var uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            phonenumber = phonenumber ?: userData.value?.phonenumber,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )
        uid?.let {
            inProgress.value = true
            db.collection("User").document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        db.collection("User").document(uid).set(userData)
                        inProgress.value = false
                        Log.i("viewModel", "User already exists")
                        getUserData(uid)
                    } else {
                        db.collection("User").document(uid).set(userData)
                        inProgress.value = false
                        getUserData(uid)
                    }
                }

                .addOnFailureListener {
                    Log.i("viewModel", "Couldn't get the user data $it")
                }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection("User").document(uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.i("viewModel", "Cannot retrieve user data $e")
            }

            if (snapshot != null) {
                val user = snapshot.toObject(UserData::class.java)
                userData.value = user
                inProgress.value = false
                populateChats()
                Log.i("viewModel", "Successfully retrieved user data")
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(
                imageUrl = it.toString()
            )
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl?.addOnSuccessListener(onSuccess)
            inProgress.value = false
            Log.i("viewModel", "Successfully uploaded image")
        }
            .addOnFailureListener {
                Log.i("viewModel", "Failed to upload image $it")
                inProgress.value = false
            }
    }

    fun addChat(addChatNumber: String) {
        db.collection("Chats").where(
            Filter.or(
                Filter.and(
                    Filter.equalTo("user1.number", addChatNumber),
                    Filter.equalTo("user2.number", userData.value?.phonenumber)
                ),
                Filter.and(
                    Filter.equalTo("user1.number", userData.value?.phonenumber),
                    Filter.equalTo("user2.number", addChatNumber)
                )
            )
        ).get()
            .addOnSuccessListener { userchat ->
                if (userchat.isEmpty) {
                    Log.i("viewModel", "Chat does not exist")
                    db.collection("User").whereEqualTo("phonenumber", addChatNumber).get()
                        .addOnSuccessListener { user ->
                            if (user.isEmpty) {
                                Log.i("viewModel", "")
                            } else {
                                val chatPartner = user.toObjects(UserData::class.java)[0]
                                val id = db.collection("Chats").document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        userId = userData.value?.userId,
                                        name = userData.value?.name,
                                        imageUrl = userData.value?.imageUrl,
                                        number = userData.value?.phonenumber
                                    ),
                                    ChatUser(
                                        userId = chatPartner.userId,
                                        name = chatPartner.name,
                                        imageUrl = chatPartner.imageUrl,
                                        number = chatPartner.phonenumber
                                    )
                                )

                                db.collection("Chats").document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {
                            Log.i("viewModel", "Failed to get user with this number:$addChatNumber $it")
                        }
                } else {
                    Log.i("viewModel", "Chat already exists")
                }
            }
            .addOnFailureListener {
                Log.i("viewModel", "Failed to add chat $it")
            }
    }

    fun populateChats() {
        inProgressChat.value = true
        db.collection("Chats").where(
            Filter.or(
                Filter.equalTo("user1.number", userData.value?.phonenumber),
                Filter.equalTo("user2.number", userData.value?.phonenumber)
            )
        ).addSnapshotListener{snapshot, e ->
            if (e != null) {
                Log.i("viewModel", "Failed to get chats $e")
            }
            if (snapshot != null) {
                chats.value = snapshot.documents.mapNotNull {
                    it.toObject(ChatData::class.java)
                }
                inProgressChat.value = false
            }
        }
    }
}