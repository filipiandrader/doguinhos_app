package com.doguinhos_app.main.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doguinhos_app.R

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val doguinhosNomeTextView: TextView = itemView.findViewById(R.id.doguinhosNomeTextView)
    val doguinhosSubRacaTextView: TextView = itemView.findViewById(R.id.doguinhosSubRacaTextView)
}