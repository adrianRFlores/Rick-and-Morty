package com.example.laboratorio8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private lateinit var char: Character
    private lateinit var pfp: ImageView
    private lateinit var species: TextInputLayout
    private lateinit var status: TextInputLayout
    private lateinit var gender: TextInputLayout
    private lateinit var name: TextInputLayout
    private lateinit var origin: TextInputLayout
    private lateinit var epCount: TextInputLayout
    private lateinit var guardar: MaterialButton
    private lateinit var db: Database
    private lateinit var toolbar: MaterialToolbar
    private val arg: CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pfp = view.findViewById(R.id.pfp)
        name = view.findViewById(R.id.name)
        species = view.findViewById(R.id.species)
        status = view.findViewById(R.id.status)
        gender = view.findViewById(R.id.gender)
        origin = view.findViewById(R.id.origin)
        epCount = view.findViewById(R.id.epCount)

        toolbar = (activity as MainActivity).getToolbar()
        db = Room.databaseBuilder(requireContext(), Database::class.java, "dbChars").build()
        guardar = view.findViewById(R.id.btn_guardar)

        CoroutineScope(Dispatchers.IO).launch {
            char = db.getChar().getChar(id=arg.char.id)
            setData()
        }

        setListeners()
    }

    private fun setListeners() {
        guardar.setOnClickListener {
            updateCharacter()
        }
        toolbar.setOnMenuItemClickListener{menuItem->
            when(menuItem.itemId){
                R.id.sync->{
                    reloadCharacter()
                    true
                }
                R.id.delete->{
                    deleteCharacter()
                    true
                }
                else -> true
            }
        }
    }

    private fun updateCharacter(){
        val updatedCharacter = char.copy(
            name = name.editText!!.text.toString(),
            species = species.editText!!.text.toString(),
            status = status.editText!!.text.toString(),
            gender = gender.editText!!.text.toString(),
            origin = origin.editText!!.text.toString(),
            epCount = epCount.editText!!.text.toString().toInt())
        CoroutineScope(Dispatchers.IO).launch { db.getChar().update(updatedCharacter) }
    }

    private fun deleteCharacter(){
        CoroutineScope(Dispatchers.IO).launch {
            db.getChar().delete(char)
        }
    }

    private fun setData(){
        CoroutineScope(Dispatchers.Main).launch {
            pfp.load(char.image){
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_baseline_insert_emoticon_24)
                error(R.drawable.ic_baseline_insert_emoticon_24)
                crossfade(true)
                crossfade(300)
                memoryCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED) }
            name.editText!!.setText(char.name)
            species.editText!!.setText(char.species)
            status.editText!!.setText(char.status)
            gender.editText!!.setText(char.gender)
            origin.editText!!.setText(char.origin)
            epCount.editText!!.setText(char.epCount.toString())
        }
    }

    private fun reloadCharacter() {
        RetrofitInstance.api.getCharacter(arg.char.id).enqueue(object: Callback<CharacterDetailsAPI>{
            override fun onResponse(call: Call<CharacterDetailsAPI>, response: Response<CharacterDetailsAPI>) {
                if(response.isSuccessful && response.body() != null){
                    pfp.load(response.body()!!.image){
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_baseline_insert_emoticon_24)
                        error(R.drawable.ic_baseline_insert_emoticon_24)
                        crossfade(true)
                        crossfade(300)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        diskCachePolicy(CachePolicy.ENABLED) }
                    name.editText!!.setText(response.body()!!.name)
                    species.editText!!.setText(response.body()!!.species)
                    status.editText!!.setText(response.body()!!.status)
                    gender.editText!!.setText(response.body()!!.gender)
                    origin.editText!!.setText(response.body()!!.origin.name)
                    epCount.editText!!.setText(response.body()!!.episode.size.toString())
                    updateCharacter()
                }
            }
            override fun onFailure(call: Call<CharacterDetailsAPI>, t: Throwable) {}
        })
    }

}
