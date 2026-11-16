package com.ivan.games.drawreality

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настраиваем нижнее меню
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_draw -> {
                    // Уже на экране рисования (главный экран) — ничего не делаем
                    true
                }
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

        // Кнопка "С Камеры" (переход на камеру)
        // В разметке мы дали ID карточке cameraCard
        val cameraCard = findViewById<androidx.cardview.widget.CardView>(R.id.cameraCard)
        cameraCard.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}
