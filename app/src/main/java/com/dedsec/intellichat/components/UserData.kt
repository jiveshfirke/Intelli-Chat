package com.dedsec.intellichat.components

data class UserData(
    val userId: String? = null,
    val name: String? = null,
    val phonenumber: String? = null,
    val imageUrl: String? = null
)

data class ChatData(
    val chatId: String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser(),
)

data class ChatUser(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val imageUrl: String? = ""

)

data class MessageData(
    val senderId: String? = "",
    val message: String? = "",
    val timestamp: String? = ""
)