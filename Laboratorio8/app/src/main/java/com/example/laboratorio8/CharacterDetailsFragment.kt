package com.example.laboratorio8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private lateinit var char: Character
    private lateinit var pfp: ImageView
    private lateinit var species: TextView
    private lateinit var status: TextView
    private lateinit var gender: TextView
    private lateinit var name: TextView
    private lateinit var origin: TextView
    private lateinit var epCount: TextView
    private val arg: CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pfp = view.findViewById(R.id.pfp)

        name = view.findViewById(R.id.charName)
        species = view.findViewById(R.id.species)
        status = view.findViewById(R.id.status)
        gender = view.findViewById(R.id.gender)
        origin = view.findViewById(R.id.origin)
        epCount = view.findViewById(R.id.count)

        RetrofitInstance.api.getCharacter(arg.char.id).enqueue(object : Callback<Character> {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                char = response.body()!!
                pfp.load(arg.char.image){
                    placeholder(R.drawable.ic_baseline_insert_emoticon_24)
                    transformations(CircleCropTransformation())
                    memoryCachePolicy(CachePolicy.ENABLED)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
                name.text = char.name
                species.text = char.species
                status.text = char.status
                gender.text = char.gender
                origin.text = char.origin.name
                epCount.text = char.episode.size.toString()

            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
            }

        })
    }
}
