package com.example.friend.view.main

import androidx.lifecycle.*
import com.example.friend.data.FriendRepository
import com.example.friend.model.Friend
import kotlinx.coroutines.launch

class FriendsViewModel(private val repository: FriendRepository): ViewModel() {
//используя LiveData обновляем пользовательский интерфейс только тогда когда изменятся данные
    val allFriends: LiveData<List<Friend>> = repository.allFriends.asLiveData()
    //создание coroutine для изменения данных без остановки потока
    fun insert(friend: Friend) = viewModelScope.launch {
        repository.insert(friend)
    }

    fun update(friend: Friend) = viewModelScope.launch {
        repository.update(friend)
    }

    fun delete(friend: Friend) = viewModelScope.launch {
        repository.delete(friend)
    }
}

class FriendViewModelFactory(private val repository: FriendRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}