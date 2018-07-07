package com.lib

import com.lib.model.Conversation
import com.thedeanda.lorem.LoremIpsum
import io.reactivex.Single
import io.reactivex.SingleObserver

/**
 * Emits a list. The offset is the number of items already displayed.
 */
class Conversations(private val offset: Long) : Single<List<Conversation>>() {

    private val lorem = LoremIpsum(offset)

    public override fun subscribeActual(observer: SingleObserver<in List<Conversation>>) {
        if (offset > 150) {
            emptyList()
        } else {
            (0 until 50).map (this::generateConversation)
        }.let {
            Single.just(it)
        }.subscribe(observer)
    }

    private fun generateConversation(id: Int) = Conversation(
            id = offset + id,
            from = lorem.name,
            blurb = lorem.getWords(50),
            subject = lorem.getWords(3)
    )
}