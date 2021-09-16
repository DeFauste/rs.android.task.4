package com.example.friend.view.main

import androidx.recyclerview.widget.RecyclerView
import com.example.friend.databinding.ItemFriendBinding
import com.example.friend.model.Friend
import com.example.friend.view.ActionsListener

class FriendViewHolder (
    private val binding: ItemFriendBinding,
    private val listener: ActionsListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(friend: Friend) {
        binding.name.text = friend.name
        binding.age.text = friend.age
        binding.weight.text = friend.weight

        initButtonListeners(friend)
    }

    private fun initButtonListeners(friend: Friend) {
        binding.editButton.setOnClickListener {
            listener.update(friend)
            listener.delete(friend)
        }
        binding.deleteButton.setOnClickListener {
            listener.delete(friend)
        }
    }
}