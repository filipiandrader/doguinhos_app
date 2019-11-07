package com.doguinhos_app.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doguinhos_app.R
import com.doguinhos_app.main.principal.ui.MainActivity
import org.jetbrains.anko.startActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startActivity<MainActivity>()
    }
}
