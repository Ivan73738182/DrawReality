package com.ivan.games.drawreality

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_draw -> true
                R.id.nav_lessons -> {
                    startActivity(Intent(this, LessonsActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Кнопка "С Камеры"
        findViewById<androidx.cardview.widget.CardView>(R.id.cameraCard).setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        // Кнопка "Шаблоны" (бывшая "Из Галереи")
        findViewById<androidx.cardview.widget.CardView>(R.id.galleryCard).setOnClickListener {
            startActivity(Intent(this, TemplatesActivity::class.java))
        }
    }
}
