package com.economist.demo.pip

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.economist.demo.R

@androidx.annotation.OptIn(UnstableApi::class)
class BaseScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)

        findViewById<Button>(R.id.startButtonId).setOnClickListener {
            val intent = Intent(this, VideoPlayerFullScreenActivity::class.java)
            startActivity(intent)
        }
    }

}



