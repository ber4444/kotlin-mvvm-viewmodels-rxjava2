package com.test

import androidx.lifecycle.ViewModel
import com.lib.Conversations
import com.lib.model.Conversation
import io.reactivex.Single

class MainViewModel : ViewModel() {

    fun getConversations(offset: Long): Single<List<Conversation>> {
        return Conversations(offset)
    }
}