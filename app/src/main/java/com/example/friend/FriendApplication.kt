package com.example.friend

import android.app.Application
import com.example.friend.data.FriendRepository
import com.example.friend.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FriendApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FriendRepository(database.friendDao()) }

}