package com.ivan.games.drawreality

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Пока делаем заглушки: 10 пустых карточек
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 колонки
        
        // Временный адаптер-заглушка
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = android.widget.TextView(parent.context).apply {
                    text = "Шаблон"
                    gravity = android.view.Gravity.CENTER
                    setBackgroundColor(android.graphics.Color.LTGRAY)
                    layoutParams = android.view.ViewGroup.MarginLayoutParams(250, 250).apply { setMargins(8, 8, 8, 8) }
                }
                return object : RecyclerView.ViewHolder(view) {}
            }
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
            override fun getItemCount(): Int = 10
        }
    }
}
