package com.doguinhos_app.main.details.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doguinhos_app.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val doguinhosImageView: CircleImageView = itemView.findViewById(R.id.doguinhosImageView)
}