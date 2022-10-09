package com.example.laboratorio8

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.CharListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var charList: MutableList<Character>
    private lateinit var topAppBar: Toolbar
    private lateinit var login: LoginFragment
    private lateinit var db: Database

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login = LoginFragment()
        topAppBar = (activity as MainActivity).getToolbar()
        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)

        charList = ArrayList()

        db = Room.databaseBuilder(requireContext(), Database::class.java, "dbCharacters").build()

        setupChars()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.sync -> {
                    sync()
                    true
                }
                R.id.menu_item_a_z -> {
                    charList.sortBy { char -> char.name }
                    recyclerView.adapter!!.notifyDataSetChanged()
                    true
                }
                R.id.menu_item_z_a -> {
                    charList.sortByDescending { char -> char.name }
                    recyclerView.adapter!!.notifyDataSetChanged()
                    true
                }
                R.id.logout ->{
                    CoroutineScope(Dispatchers.IO).launch {
                        login.deleteMail(requireContext())
                        db.getChar().deleteAll()
                    }
                    requireView().findNavController().navigate(R.id.action_charactersFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CharacterAdapter(charList, this)
    }

    override fun onPlaceClicked(char: Character, position: Int) {
        requireView().findNavController().navigate(
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(char)
        )
    }

    private fun saveChars(charList: MutableList<Character>){
        charList.forEach{
            CoroutineScope(Dispatchers.IO).launch {
                db.getChar().insert(it)
            }
        }
    }

    private fun setupChars() {
        CoroutineScope(Dispatchers.IO).launch {
            val chars = db.getChar().getChars()
            charList.clear()
            if(chars.isEmpty()) getData()
            else{
                charList.addAll(chars)
                CoroutineScope(Dispatchers.Main).launch {
                    setupRecycler()
                }
            }
        }
    }

    private fun getData() {
        RetrofitInstance.api.getChars().enqueue(object : Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.isSuccessful && response.body() != null){
                    charList = response.body()!!.chars
                    saveChars(charList)
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                println("Error")
            }
        })
    }

    private fun charAPItoChar(results: MutableList<CharacterDetailsAPI>): MutableList<Character> {
        var chars = mutableListOf<Character>()
        results.forEach{
            val temp = Character(it.gender, it.id, it.image, it.name, it.origin.name,
                it.species, it.status, it.episode.size)
            chars.add(temp)
        }
        return chars
    }

    private fun sync() {
        RetrofitInstance.api.getChars().enqueue(object : Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.isSuccessful && response.body() != null){
                    CoroutineScope(Dispatchers.IO).launch { db.getChar().deleteAll() }
                    CoroutineScope(Dispatchers.Main).launch{
                        charList.clear()
                        charList = response.body()!!.chars
                        addDB(charList)
                        setupRecycler()
                    }
                }
            }
            override fun onFailure(call: Call<APIResponse>, t: Throwable) { println("Error") }
        })
    }

    private fun addDB(characterList: MutableList<Character>) {
        characterList.forEach{
            CoroutineScope(Dispatchers.IO).launch {
                db.getChar().insert(it)
            }
        }
    }

}