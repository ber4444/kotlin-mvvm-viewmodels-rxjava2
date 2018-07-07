package com.lib

import com.lib.model.Comment
import com.lib.model.Message
import com.lib.model.Teammate
import com.lib.model.Event
import com.thedeanda.lorem.LoremIpsum
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Emits Events. An Event is a message or comment in a conversation.
 */
class Events(conversationId: Long) : Observable<Event>() {

    companion object {
        private val avatars = listOf(
                "https://randomuser.me/api/portraits/lego/0.jpg",
                "https://randomuser.me/api/portraits/lego/1.jpg",
                "https://randomuser.me/api/portraits/lego/2.jpg",
                "https://randomuser.me/api/portraits/lego/3.jpg",
                "https://randomuser.me/api/portraits/lego/4.jpg",
                "https://randomuser.me/api/portraits/lego/5.jpg",
                "https://randomuser.me/api/portraits/lego/6.jpg",
                "https://randomuser.me/api/portraits/lego/7.jpg",
                "https://randomuser.me/api/portraits/lego/8.jpg"
        )
    }

    private val random = Random(conversationId)
    private val lorem = LoremIpsum(conversationId)
    private val teammates: List<Teammate> = (1..50).map { Teammate(lorem.name, avatars.random()) }

    override fun subscribeActual(observer: Observer<in Event>?) {
        if (observer == null) return

        val intervals = (1..10L).map {
            Observable.interval(it * 100, TimeUnit.MILLISECONDS)
        }

        Observable.merge(intervals)
                .observeOn(Schedulers.computation())
                .map { generateEvent() }
                .subscribe(observer)
    }

    /**
     * For debug use - this will slow down the event rate to 1 per second
     */
    fun slowDown(): Observable<Event> = this.compose(slowDownTransformer)

    private val slowDownTransformer = ObservableTransformer<Event, Event> { upstream ->
        zip(
                upstream,
                interval(1, TimeUnit.SECONDS),
                BiFunction<Event, Long, Event> { obs, _ -> obs }
        )
    }

    private fun generateEvent() = when(random.nextInt(3)) {
        0 -> Event(
                id = random.nextInt(100).toLong(),
                date = System.currentTimeMillis(),
                from = teammates.random(),
                message = generateMessage(),
                comment = null,
                type = null
        )
        1 -> Event(
                id = random.nextInt(100).toLong() + 100,
                date = System.currentTimeMillis(),
                from = teammates.random(),
                message = null,
                comment = generateComment(),
                type = null
        )
        else -> Event(
                id = random.nextInt(100).toLong() + 200,
                date = System.currentTimeMillis(),
                from = teammates.random(),
                message = null,
                comment = null,
                type = "archived" // The conversation has been archived
        )
    }.takeUnless { random.nextInt(501) == 500 } ?: throw Exception("HAHA!")

    private fun generateMessage() = Message(
            text = generateParagraphs(max = 50)
    )

    private fun generateComment() = Comment(
            text = generateParagraphs(max = 10)
    )

    private fun generateParagraphs(max: Int) = lorem.getParagraphs(1, max)

    private fun <T : Any> List<T>.random(): T = random.nextInt(size).let (this::get)
}
