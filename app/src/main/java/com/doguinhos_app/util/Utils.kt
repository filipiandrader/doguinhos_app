package com.doguinhos_app.util

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */

fun View.setVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun SwipeRefreshLayout.setRefresh(refresh: Boolean) {
    this.isRefreshing = refresh
}

fun capitalize(str: String): String {
    val arr = str.toLowerCase(Locale("pt", "BR")).toCharArray()
    var cap = true
    for (i in arr.indices) {
        if (cap && !Character.isWhitespace(arr[i])) {
            arr[i] = Character.toUpperCase(arr[i])
            cap = false
        } else {
            if (arr[i] == ' ') {
                cap = true
            }
        }
    }
    return String(arr)
}