package com.example.friend.data

import androidx.room.*
import com.example.friend.model.Friend
import kotlinx.coroutines.flow.Flow

//Создаем Dao. Указываем запросы SQL и связываем их с вызовами методов
@Dao
interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friend: Friend)

    @Update
    suspend fun update(vararg friend: Friend)

    @Delete
    suspend fun delete(friend: Friend)

    @Query("SELECT * FROM friendsTable ")
    fun loadAllFriends(): Flow<List<Friend>>

}