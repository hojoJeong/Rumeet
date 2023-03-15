package com.d204.rumeet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.d204.rumeet.R
import com.d204.rumeet.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fcv_main, HomeFragment()).commit()
    }
}