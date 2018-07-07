package com.lib.model

data class Event(
        val id: Long,
        val date: Long,
        val from: Teammate,
        val message: Message?,
        val comment: Comment?,
        val type: String?
)
