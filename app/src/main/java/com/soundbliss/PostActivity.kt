package com.soundbliss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    lateinit var description : SocialAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val close : ImageView = findViewById(R.id.close)
        val imageAdded : ImageView = findViewById(R.id.image_added)
        val textView : TextView = findViewById(R.id.post)
        description = findViewById(R.id.description)

    }
}