package com.example.laboratorio8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private lateinit var pfp: ImageView
    private lateinit var species: TextView
    private lateinit var status: TextView
    private lateinit var gender: TextView
    private val arg: CharacterDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pfp = view.findViewById(R.id.pfp)

        pfp.load(arg.char.image){
            placeholder(R.drawable.ic_baseline_insert_emoticon_24)
            transformations(CircleCropTransformation())
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
        }

        species = view.findViewById(R.id.species)
        status = view.findViewById(R.id.status)
        gender = view.findViewById(R.id.gender)

        species.text = arg.char.species
        status.text = arg.char.status
        gender.text = arg.char.gender

    }

}