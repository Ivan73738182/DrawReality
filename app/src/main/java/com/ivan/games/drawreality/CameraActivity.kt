package com.ivan.games.drawreality

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private lateinit var arImageView: ImageView
    private lateinit var uiContainer: ConstraintLayout
    private lateinit var cameraExecutor: ExecutorService
    
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private var isLocked = false
    private var dX = 0f
    private var dY = 0f
    private var scaleFactor = 1f
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        viewFinder = findViewById(R.id.previewView)
        arImageView = findViewById(R.id.arImageView)
        uiContainer = findViewById(R.id.uiContainer)
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Получаем переданную картинку
        val imageResId = intent.getIntExtra("IMAGE_ID", 0)
        if (imageResId != 0) {
            arImageView.setImageResource(imageResId)
        }

        // Кнопка Назад
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        // Ползунок прозрачности
        val alphaSeekBar = findViewById<SeekBar>(R.id.alphaSeekBar)
        alphaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                arImageView.alpha = progress / 100f
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Кнопка Замок
        val btnLock = findViewById<ImageButton>(R.id.btnLock)
        btnLock.setOnClickListener {
            isLocked = !isLocked
            if (isLocked) {
                btnLock.setImageResource(R.drawable.ic_lock)
                Toast.makeText(this, "Рисунок заблокирован", Toast.LENGTH_SHORT).show()
            } else {
                btnLock.setImageResource(R.drawable.ic_unlock)
                Toast.makeText(this, "Рисунок разблокирован", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка Глаз (Скрыть/Показать интерфейс)
        findViewById<ImageButton>(R.id.btnEye).setOnClickListener {
            if (uiContainer.visibility == View.VISIBLE) {
                uiContainer.visibility = View.INVISIBLE
                arImageView.visibility = View.INVISIBLE
            } else {
                uiContainer.visibility = View.VISIBLE
                arImageView.visibility = View.VISIBLE
            }
        }

        // Настройка зума двумя пальцами
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)
                arImageView.scaleX = scaleFactor
                arImageView.scaleY = scaleFactor
                return true
            }
        })

        // Движение картинки (если не заблокирована)
        arImageView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            
            if (!isLocked && event.pointerCount == 1) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = arImageView.x - event.rawX
                        dY = arImageView.y - event.rawY
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        arImageView.animate()
                            .x(event.rawX + dX)
                            .y(event.rawY + dY)
                            .setDuration(0)
                            .start()
                        true
                    }
                    else -> false
                }
            } else {
                true
            }
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                Toast.makeText(this, "Ошибка камеры: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Разрешение на камеру не выдано", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
