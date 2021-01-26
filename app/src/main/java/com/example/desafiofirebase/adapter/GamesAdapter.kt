package com.example.desafiofirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofirebase.R
import com.example.desafiofirebase.domain.Game
import com.squareup.picasso.Picasso

class GamesAdapter(val listener : OnClickGameListener) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>(){
    var listGames = arrayListOf<Game>()

    interface OnClickGameListener {
        fun onClickGame(position: Int)
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivGame: ImageView = itemView.findViewById(R.id.iv_itemGame)
        var tvGameName: TextView = itemView.findViewById(R.id.tv_gameName)
        var tvAnoGame: TextView = itemView.findViewById(R.id.tv_anoGame)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.onClickGame(position)
        }
    }

    fun addList(games : ArrayList<Game>){
        listGames = games
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = listGames[position]
        Picasso.get().load(game.img).into(holder.ivGame)
        holder.tvGameName.text = game.name
        holder.tvAnoGame.text = game.anoLancamento
    }

    override fun getItemCount(): Int  = listGames.size

}