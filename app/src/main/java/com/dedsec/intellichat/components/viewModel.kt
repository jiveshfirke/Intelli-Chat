package com.dedsec.intellichat.components

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.dedsec.intellichat.navigation.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestion
import com.google.mlkit.nl.smartreply.TextMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val inProgressChatMessages = mutableStateOf(false)
    val chatMessages = mutableStateOf<List<MessageData>>(listOf())
    var currentChatMessageListener: ListenerRegistration? = null
    val statusList = mutableStateOf<List<StatusData>>(listOf())
    val inProgressStatus = mutableStateOf(false)
    val smartSugestion = MutableLiveData<List<SmartReplySuggestion>>()

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        Log.i("viewModel", "Initializing view model")
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun populateMessages(chatId: String) {
        inProgressChatMessages.value = true
        currentChatMessageListener = db.collection("Chats").document(chatId).collection("messages")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.i("viewModel", "Failed to get messages $e")
                }
                if (snapshot != null) {
                    chatMessages.value = snapshot.documents.mapNotNull {
                        it.toObject(MessageData::class.java)
                    }.sortedBy {
                        val timestampString = it.timestamp
                        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                        val date = dateFormat.parse(timestampString)
                        date.time
                    }
                    getConv()
                    inProgressChatMessages.value = false
                }
            }
    }

    fun depopulateMessages() {
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun loginIn(email: String, password: String, navHostController: NavHostController, context: Context) {
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
                    Toast.makeText(context,"Successfully signed in", Toast.LENGTH_LONG).show()
                    navHostController.navigate(Home) {
                        popUpTo(0)
                    }
                } else {
                    Log.i("viewModel", "Login Failed")
                    inProgress.value = false
                    Toast.makeText(context,"Signed in Failed", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Log.i("viewModel", "Login Failed ${it.message}")
                inProgress.value = false
                Toast.makeText(context,"Signed in Failed", Toast.LENGTH_LONG).show()
            }
    }

    fun signUp(
        name: String,
        phonenumber: String,
        email: String,
        password: String,
        navHostController: NavHostController,
        context: Context
    ) {
        inProgress.value = true

        db.collection("User").whereEqualTo("phonenumber", phonenumber).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                createOrUpdateProfile(name, phonenumber)
                                signIn.value = true
                                navHostController.navigate(Home) {
                                    popUpTo(0)
                                }
                                Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
                                Log.i("viewModel", "Successfully created user")
                            } else {
                                // Handle unsuccessful sign-up
                                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                                inProgress.value = false
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i("viewModel", "user creation failed $exception")
                            Toast.makeText(context, "Failed to Sign up", Toast.LENGTH_LONG).show()
                            inProgress.value = false
                        }
                } else {
                    Log.i("viewModel", "Number already exists")
                    inProgress.value = false
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to Sign up ${it.message}", Toast.LENGTH_LONG).show()
                inProgress.value = false
            }
    }

    fun signOut() {
        signIn.value = false
        auth.signOut()
        userData.value = null
        depopulateMessages()
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
                populateStatus()
                Log.i("viewModel", "Successfully retrieved user data")
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {imageUrl ->
            createOrUpdateProfile(
                imageUrl = imageUrl.toString()
            )
            db.collection("Chats").where(
                Filter.or(
                    Filter.equalTo("user1.number", userData.value?.userId),
                    Filter.equalTo("user2.number", userData.value?.userId)
                )
            ).get().addOnSuccessListener {value ->
                chats.value.forEach {
                    val currentUser = if (it.user1.userId == userData.value?.userId) {
                        "user1.imageUrl"
                    } else {
                        "user2.imageUrl"
                    }
                    db.collection("Chats").document(it.chatId?: "").update(currentUser, imageUrl.toString())
                }
            }
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

    fun uploadStatusImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("status/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl?.addOnSuccessListener(onSuccess)
            inProgress.value = false
            Log.i("viewModel", "Successfully uploaded Status")
        }
            .addOnFailureListener {
                Log.i("viewModel", "Failed to upload image $it")
                inProgress.value = false
            }
    }
    fun getConv(){
        val conv: ArrayList<TextMessage> = ArrayList()
        chatMessages.value.forEach {
            conv.add(TextMessage.createForRemoteUser(it.message?: "", System.currentTimeMillis(), it.senderId?: ""))
        }

        val smartReply = SmartReply.getClient()
        if (conv.isNotEmpty()) {
            smartReply.suggestReplies(conv).addOnSuccessListener {
                if (it.suggestions.isNotEmpty()) {
                    smartSugestion.value = it.suggestions
                }
            }
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
                            Log.i(
                                "viewModel",
                                "Failed to get user with this number:$addChatNumber $it"
                            )
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
        ).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.i("viewModel", "Failed to get chats ${e.message}")
                inProgressChat.value = false
            }
            if (snapshot != null) {
                chats.value = snapshot.documents.mapNotNull {
                    it.toObject(ChatData::class.java)
                }
                inProgressChat.value = false
            }
        }
    }

    fun sendMessage(chatId: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val messageData = MessageData(
            senderId = userData.value?.userId,
            message = message,
            timestamp = time
        )
        db.collection("Chats").document(chatId).collection("messages").add(messageData)

    }

    fun uploadStatus(it: Uri) {
        uploadStatusImage(it) {
            createStatus(it.toString())
        }
    }

    fun createStatus(imageUrl: String) {
        val newStatus = StatusData(
            user = ChatUser(
                userId = userData.value?.userId,
                name = userData.value?.name,
                imageUrl = userData.value?.imageUrl,
                number = userData.value?.phonenumber
            ),
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis()
        )

        db.collection("Status").document().set(newStatus)
    }

    fun populateStatus() {
        val timeDelta = 24L * 60 * 60 * 1000
        val cutOff = System.currentTimeMillis() - timeDelta
        inProgressStatus.value = true
        db.collection("Chats").where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.i("viewModel", "Failed to get chats $e")
            }
            if (snapshot != null) {
                val currentConnections = arrayListOf(userData.value?.userId)
                val chats = snapshot.toObjects(ChatData::class.java)
                chats.forEach { chat ->
                    if (chat.user1.userId == userData.value?.userId) {
                        currentConnections.add(chat.user2.userId)
                    } else {
                        currentConnections.add(chat.user1.userId)
                    }
                }

                db.collection("Status").whereGreaterThan("timestamp", cutOff).whereIn("user.userId", currentConnections)
                    .addSnapshotListener { snapshotstatus, error ->
                        if (error != null) {
                            Log.i("viewModel", "Failed to get chats $error")
                        }
                        if (snapshotstatus != null) {
                            statusList.value = snapshotstatus.toObjects(StatusData::class.java)
                            statusList.value = statusList.value.filter { it.timestamp!! >= cutOff }
                            inProgressStatus.value = false
                        }
                    }
            }
        }
    }
}