package com.soundapp.mobile.filmica

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

class DetailActivity : AppCompatActivity() {

    // The way of swift :)
    val addButton : Button by lazy {
        addButton.setOnClickListener {
            Toast.makeText(this@DetailActivity, "Button tapped", Toast.LENGTH_SHORT).show()
        }
        addButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Associate te ID of the file to the view
        val addButton : Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            Toast.makeText(this@DetailActivity, "Button tapped", Toast.LENGTH_SHORT).show()
        }
    }

}
