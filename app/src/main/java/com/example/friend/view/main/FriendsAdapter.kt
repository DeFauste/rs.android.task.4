package com.example.friend.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.friend.databinding.ItemFriendBinding
import com.example.friend.model.Friend
import com.example.friend.view.ActionsListener

class FriendsAdapter(private val listener: ActionsListener) :
    ListAdapter<Friend, FriendViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendBinding.inflate(layoutInflater, parent, false)
        return FriendViewHolder(binding, listener,)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Friend>() {

            override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.age == newItem.age &&
                        oldItem.weight == oldItem.weight
            }

            override fun getChangePayload(oldItem: Friend, newItem: Friend) = Any()
        }
    }
}
