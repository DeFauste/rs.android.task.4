package com.example.friend.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friend.FriendApplication
import com.example.friend.R
import com.example.friend.data.SQLHelper
import com.example.friend.databinding.FragmentMainBinding
import com.example.friend.model.Actions
import com.example.friend.model.Friend
import com.example.friend.model.SortMode
import com.example.friend.view.ActionsListener
import com.example.friend.view.MainActivity

class MainFragment : Fragment(), View.OnClickListener, ActionsListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var owner: MainActivity
    private lateinit var sqlDatabase: SQLHelper

    private var isRoomDatabase = true
    private var sortMode = SortMode.NAME.name

    private var name = ""
    private var age = ""
    private var weight = ""
    private var action = Actions.NOTHING
    private var currentId = 0

    private val friendsViewModel: FriendsViewModel by viewModels {
        FriendViewModelFactory((requireActivity().application as FriendApplication).repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = context as MainActivity

        sqlDatabase = SQLHelper(requireContext())

        name = arguments?.getString(NAME) ?: ""
        age = arguments?.getString(AGE) ?: ""
        weight = arguments?.getString(WEIGHT) ?: ""
        currentId = arguments?.getInt(ID) ?: 0

        action = when (arguments?.getString(ACTION)) {
            Actions.ADD.name -> Actions.ADD
            Actions.UPDATE.name -> Actions.UPDATE
            else -> Actions.NOTHING
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initView() {
        owner.title?.text = resources.getString(R.string.friends)
        owner.settingsIcon?.visibility = View.VISIBLE

        owner.settingsIcon?.setOnClickListener(this)
        when (action) {
            Actions.ADD -> add()
            //согласно тому, что выбрано в качестве базы данных в
            // настройках RoomDatabase/Cursor
            // вызывваем соотвествующий им метод
            Actions.UPDATE -> {
                if (isRoomDatabase) {
                    friendsViewModel.update(Friend(currentId, name, age, weight))
                } else {
                    sqlDatabase.update(Friend(currentId, name, age, weight))
                }
            }
            else -> {
                name = ""
                age = ""
                weight = ""
            }
        }
    //заменям RecyclerAdapter нашим FriendsAdapter
        val friendAdapter = FriendsAdapter(this)
        binding.friendRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = friendAdapter
        }

        if (isRoomDatabase) {
            //назначаем слушателя
            friendsViewModel.allFriends.observe(requireActivity()) { friends ->
                friends.let { friendAdapter.submitList(sortList(friends)) }
            }
        } else {
            var list = sqlDatabase.getListOfFriends()
            list = sortList(list)
            friendAdapter.submitList(list)
        }
        //изпользуя FragmentNavigation открываем AddItemFragment
        findNavController()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
        }
    }

    private fun sortList(list: List<Friend>): List<Friend> {
        when (sortMode) {
            SortMode.NAME.name -> return list.sortedBy { it.name }
            SortMode.AGE.name -> return list.sortedBy { it.age }
            SortMode.WEIGHT.name -> return list.sortedBy { it.weight }
        }
        return list.sortedBy { it.name }
    }

    override fun onResume() {
        super.onResume()

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        isRoomDatabase = prefs.getBoolean("database_mode", true)
        sortMode = prefs.getString("sort", SortMode.NAME.name) ?: SortMode.NAME.name

        initView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.settings_icon -> {
                owner.title?.text = resources.getString(R.string.settings)
                owner.settingsIcon?.visibility = View.GONE
                action = Actions.NOTHING
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            }
        }
    }

    override fun add() {
        if (action == Actions.ADD && isRoomDatabase) {
            friendsViewModel.insert(Friend(0,name, age, weight))
        } else {
            sqlDatabase.insert(Friend(0,name, age, weight))
        }
    }

    override fun update(friend: Friend) {
        currentId = friend.id
        val bundle = bundleOf(
            ACTION to Actions.UPDATE.name,
            ID to currentId,
            NAME to friend.name,
            AGE to friend.age,
            WEIGHT to friend.weight
        )
        findNavController().navigate(R.id.action_mainFragment_to_addItemFragment, bundle)
    }

    override fun delete(friend: Friend) {
        if (isRoomDatabase) {
            Log.e("DELETE",friend.name)
            friendsViewModel.delete(friend)
        } else {
            sqlDatabase.delete(friend)
            action = Actions.NOTHING
            onResume()
        }
    }

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val AGE = "age"
        private const val WEIGHT = "weight"
        private const val ACTION = "action"
    }
}
