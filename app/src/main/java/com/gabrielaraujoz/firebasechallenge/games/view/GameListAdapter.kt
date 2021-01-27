package com.gabrielaraujoz.firebasechallenge.games.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabrielaraujoz.firebasechallenge.R
import com.gabrielaraujoz.firebasechallenge.games.model.GameModel

class GameListAdapter(
    private var games: MutableList<GameModel>,
    private val listener: (GameModel) -> Unit
) : RecyclerView.Adapter<GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = games[position]

        Glide.with(holder.itemView).load(item.image_URI).into(holder.image)

        holder.bind(item)

        holder.itemView.setOnClickListener { listener(item) }

    }

    override fun getItemCount() = games.size

}