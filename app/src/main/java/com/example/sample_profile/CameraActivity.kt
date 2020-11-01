package com.example.sample_profile

import android.app.Activity
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CameraActivity : AppCompatActivity(), CameraView {
    private val cameraProcessor: CameraProcessor by lazy {
        CameraProcessor(this)
    }

    override val textureView: TextureView  by lazy {
        this.findViewById(R.id.textureView) as TextureView
    }

    override val activity: Activity by lazy {
        this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)

        setup()
    }

    fun setup() {
        val button = this.findViewById(R.id.captureButton) as Button
        button.setOnTouchListener { view, motionEvent ->
            cameraProcessor.take()
            true
        }

        if (textureView.isAvailable) {
            cameraProcessor.setup()
        } else {
            textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                    cameraProcessor.setup()
                }

                override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                    return true
                }

                override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

                }

                override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

                }
            }
        }
    }
}