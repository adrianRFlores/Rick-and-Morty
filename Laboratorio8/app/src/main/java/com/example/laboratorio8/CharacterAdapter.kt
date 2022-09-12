package com.example.laboratorio8

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import java.util.*

class CharacterAdapter(private val dataSet: MutableList<Character>, private val listener: CharListener) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    interface CharListener {
        fun onPlaceClicked(char: Character, position: Int)
    }

    class ViewHolder(private val view: View, private val listener: CharListener) : RecyclerView.ViewHolder(view){
        private val imageType: ImageView = view.findViewById(R.id.image_itemChar_pfp)
        private val charName: TextView = view.findViewById(R.id.text_itemChar_name)
        private val charStatus: TextView = view.findViewById(R.id.text_itemChar_status)
        private val layout: ConstraintLayout = view.findViewById(R.id.layout_itemChar)
        private lateinit var char: Character

        @SuppressLint("SetTextI18n")
        fun setData(character: Character){
            this.char = character
            imageType.load(char.image){
                placeholder(R.drawable.ic_baseline_insert_emoticon_24)
                transformations(CircleCropTransformation())
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.DISABLED)
            }
            charName.text = char.name
            charStatus.text = "${char.species} - ${char.status}"
            setListeners()
        }

        private fun setListeners() {
            layout.setOnClickListener {
                listener.onPlaceClicked(char, this.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_character, parent, false)

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}