package com.doguinhos_app.favorite.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doguinhos_app.R

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val doguinhosImageView: ImageView = itemView.findViewById(R.id.doguinhosImageView)
    val doguinhosNomeTextView: TextView = itemView.findViewById(R.id.doguinhosNomeTextView)
    val doguinhosSubRacaTextView: TextView = itemView.findViewById(R.id.doguinhosSubRacaTextView)
}