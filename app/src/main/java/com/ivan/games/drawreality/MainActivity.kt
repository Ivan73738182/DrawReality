package com.ivan.games.drawreality

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим кнопку "С Камеры" по ID и вешаем клик
        // Примечание: в разметке карточка не имеет ID, поэтому вешаем клик на неё по индексу
        val cameraButton = findViewById<androidx.cardview.widget.CardView>(R.id.cameraCard)
        cameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}
