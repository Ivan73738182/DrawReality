package com.ivan.games.drawreality

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Нижнее меню
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

        // Кнопка "Из Галереи"
        findViewById<androidx.cardview.widget.CardView>(R.id.galleryCard).setOnClickListener {
            // TODO: Выбор фото из памяти телефона
        }

        // --- Настройка списков ---
        
        // "Горячий тренд" (пока пустой, или 1 картинка для вида)
        val rvTrend = findViewById<RecyclerView>(R.id.rvTrend)
        setupRecyclerView(rvTrend)
        rvTrend.adapter = TemplateAdapter(listOf(R.drawable.anime_1)) { imageResId ->
            openCameraWithImage(imageResId)
        }

        // "Аниме" (твои 8 картинок)
        val animeImages = listOf(
            R.drawable.anime_1, R.drawable.anime_2, R.drawable.anime_3,
            R.drawable.anime_4, R.drawable.anime_5, R.drawable.anime_6,
            R.drawable.anime_7, R.drawable.anime_8
        )
        val rvAnime = findViewById<RecyclerView>(R.id.rvAnime)
        setupRecyclerView(rvAnime)
        rvAnime.adapter = TemplateAdapter(animeImages) { imageResId ->
            openCameraWithImage(imageResId)
        }

        // "Милое" (пока пустой)
        val rvCute = findViewById<RecyclerView>(R.id.rvCute)
        setupRecyclerView(rvCute)
        rvCute.adapter = TemplateAdapter(listOf(R.drawable.anime_2)) { imageResId ->
            openCameraWithImage(imageResId)
        }

        // "Простые" (пока пустой)
        val rvSimple = findViewById<RecyclerView>(R.id.rvSimple)
        setupRecyclerView(rvSimple)
        rvSimple.adapter = TemplateAdapter(listOf(R.drawable.anime_3)) { imageResId ->
            openCameraWithImage(imageResId)
        }
    }

    // Функция для настройки плавности RecyclerView
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true) // Отключает пересчет размеров при прокрутке
        recyclerView.itemAnimator = null // Убирает анимации, которые могут вызывать дергание
        recyclerView.setItemViewCacheSize(20) // Кэширует карточки
    }

    private fun openCameraWithImage(imageResId: Int) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("IMAGE_ID", imageResId)
        startActivity(intent)
    }

    private class TemplateAdapter(
        private val images: List<Int>,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val img: ImageView = view.findViewById(R.id.imgTemplate)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_template_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.img.setImageResource(images[position])
            holder.itemView.setOnClickListener {
                onClick(images[position])
            }
        }

        override fun getItemCount(): Int = images.size
    }
}
