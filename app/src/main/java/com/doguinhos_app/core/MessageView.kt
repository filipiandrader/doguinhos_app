package com.doguinhos_app.core

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
interface MessageView {

    val mContext: Context

    fun showMessage(@StringRes resId: Int) {
        Toast.makeText(mContext, mContext.getText(resId), Toast.LENGTH_SHORT).show()
    }

    fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showNoInternetMessage() {
        Toast.makeText(mContext, "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
    }

    fun showGenericErrorMessage() {
        Toast.makeText(mContext, "Houve um erro ao realizar ação", Toast.LENGTH_SHORT).show()
    }
}