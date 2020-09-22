package com.example.whatclone.listener

interface ChatClickListener {
    fun onChatClicked(
        chatId: String?,
        otherUser: String?,
        chatImageUrl: String?,
        chatName: String?)
}