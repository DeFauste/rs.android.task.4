package com.example.friend.view

import com.example.friend.model.Friend

interface ActionsListener {

    fun add()

    fun update(friend: Friend)

    fun delete(friend: Friend)
}