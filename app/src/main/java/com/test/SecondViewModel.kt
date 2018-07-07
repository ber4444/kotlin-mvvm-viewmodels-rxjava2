package com.test

import androidx.lifecycle.ViewModel
import com.lib.Events
import com.lib.model.Event
import io.reactivex.Observable

class SecondViewModel : ViewModel() {
    fun getEvents(id: Long): Observable<Event> {
        return Events(id)//.slowDown() // optionally slow down
    }
}