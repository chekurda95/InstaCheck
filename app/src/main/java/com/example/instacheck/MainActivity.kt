package com.example.instacheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) placeMainFragment()
    }

    private fun placeMainFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, NewsFragment())
            .commit()
    }
}