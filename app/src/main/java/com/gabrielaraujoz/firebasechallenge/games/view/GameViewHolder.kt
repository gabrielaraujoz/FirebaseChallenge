package com.gabrielaraujoz.firebasechallenge.games.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabrielaraujoz.firebasechallenge.R
import com.gabrielaraujoz.firebasechallenge.games.model.GameModel

class GameViewHolder (view: View): RecyclerView.ViewHolder(view) {

    private val name = view.findViewById<TextView>(R.id.txtCardGameName)
    private val year = view.findViewById<TextView>(R.id.txtCardGameYear)
    private val image = view.findViewById<ImageView>(R.id.imgCardGame)

    fun bind (game: GameModel) {
        name.text = game.name
        year.text = game.created_at

        Glide.with(itemView).load(game.image_URI).into(image)
    }

}
