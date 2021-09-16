package com.example.friend.view.addItem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.friend.R
import com.example.friend.databinding.FragmentAddItemBinding
import com.example.friend.model.Actions
import com.example.friend.view.MainActivity

class AddItemFragment: Fragment(), View.OnClickListener {

    private lateinit var owner: MainActivity

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private var action = Actions.ADD

    private var currentId = 0
    private var name = ""
    private var age = ""
    private var weight = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

        owner = context as MainActivity
        //заполняем аргументы для полей фрагмнта фрагмента согласно действию пользователя
        //обновляет данные
        if (arguments?.getString(ACTION) == Actions.UPDATE.name) {
            action = Actions.UPDATE
            currentId = arguments?.getInt(ID) ?: 0
            name = arguments?.getString(NAME) ?: ""
            age = arguments?.getString(AGE) ?: ""
            weight = arguments?.getString(WEIGHT) ?: ""
        } else {
            //добавляет данные
            action = Actions.ADD
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView() {
    //заполняем полученными данными поля
        if (action == Actions.UPDATE) {
            owner.title?.text = getString(R.string.update_friend)
            binding.addFriendButton.text = getString(R.string.update_friend)
            binding.nameEditText.setText(name)
            binding.ageEditText.setText(age)
            binding.weightEditText.setText(weight)
        } else {
            //изменяем только title и текст кнопки
            owner.title?.text = getString(R.string.add_friend)
            binding.addFriendButton.text = getString(R.string.add_friend)
        }

        owner.settingsIcon?.visibility = View.INVISIBLE

        binding.addFriendButton.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.add_friend_button -> {
                //checkFields() проверяем заполненность полей
                if (checkFields()) {
                    //создаем bundle с новыми даными и действием пользователя
                    val bundle = bundleOf(
                        NAME to binding.nameEditText.text.toString(),
                        AGE to binding.ageEditText.text.toString(),
                        WEIGHT to binding.weightEditText.text.toString())
                    if (action == Actions.ADD) {
                        bundle.putString(ACTION, Actions.ADD.name)
                    }
                    else {
                        bundle.putString(ACTION, Actions.UPDATE.name)
                    }
                    //используя navigation fragment прокидываем данные вj fragment_main
                    findNavController().navigate(
                        R.id.action_addItemFragment_to_mainFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun checkFields(): Boolean {
        var check = true
        if (binding.nameEditText.text.toString() == "") {
            check = false
            Toast.makeText(requireContext(), "empty name", Toast.LENGTH_SHORT).show()
        }
        if (binding.ageEditText.text.toString() == "") {
            check = false
            Toast.makeText(requireContext(), "empty age", Toast.LENGTH_SHORT).show()
        }
        if (binding.weightEditText.text.toString() == "") {
            check = false
            Toast.makeText(requireContext(), "empty weight", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val AGE = "age"
        private const val WEIGHT = "weight"
        private const val ACTION = "action"
    }

}