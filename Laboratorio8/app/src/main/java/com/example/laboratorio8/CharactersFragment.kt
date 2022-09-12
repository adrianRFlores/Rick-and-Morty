package com.example.laboratorio8

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.CharListener {

    private lateinit var recyclerView: RecyclerView
    private val charList: MutableList<Character> = RickAndMortyDB.getCharacters()
    private lateinit var topAppBar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAppBar = (activity as MainActivity).getToolbar()
        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)
        setupRecycler()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
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

    fun updateRecycler(chars: MutableList<Character>){
        recyclerView.adapter = CharacterAdapter(chars, this)
    }
}