package com.example.friend.data

import androidx.annotation.WorkerThread
import com.example.friend.model.Friend
import kotlinx.coroutines.flow.Flow
//создаем репозиторий для разделения кода и получения доступа к данными
class FriendRepository (private val friendDao: FriendDao) {

    val allFriends: Flow<List<Friend>> = friendDao.loadAllFriends()

    @WorkerThread
    suspend fun insert(friend: Friend) {
        friendDao.insert(friend)
    }

    @WorkerThread
    suspend fun update(friend: Friend) {
        friendDao.insert(friend)
    }

    @WorkerThread
    suspend fun delete(friend: Friend) {
        friendDao.delete(friend)
    }
}
