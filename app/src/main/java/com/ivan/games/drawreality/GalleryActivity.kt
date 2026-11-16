package com.ivan.games.drawreality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryActivity : AppCompatActivity() {

    // Список наших картинок для раздела "Аниме"
    private val animeImages = listOf(
        R.drawable.anime_1, R.drawable.anime_2, R.drawable.anime_3,
        R.drawable.anime_4, R.drawable.anime_5, R.drawable.anime_6,
        R.drawable.anime_7, R.drawable.anime_8
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 колонки
        recyclerView.adapter = GalleryAdapter(animeImages)
    }

    // Внутренний класс адаптера
    private class GalleryAdapter(private val images: List<Int>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.findViewById(R.id.imageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageView.setImageResource(images[position])
        }

        override fun getItemCount(): Int = images.size
    }
}
