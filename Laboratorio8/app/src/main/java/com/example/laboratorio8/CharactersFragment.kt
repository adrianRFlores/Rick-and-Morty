package com.example.laboratorio8

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.CharListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var charList: MutableList<Character>
    private lateinit var topAppBar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAppBar = (activity as MainActivity).getToolbar()
        recyclerView = view.findViewById(R.id.recycler_recyclerActivity)

        charList = ArrayList()
        RetrofitInstance.api.getChars().enqueue(object: Callback<APIResponse> {
            override fun onResponse(
                call: Call<APIResponse>,
                response: Response<APIResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    charList = response.body()!!.chars as MutableList<Character>
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {

            }

        })

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

}