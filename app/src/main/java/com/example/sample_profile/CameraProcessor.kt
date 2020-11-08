package com.example.sample_profile

import android.Manifest
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*


interface CameraView {
    val textureView: TextureView
    val activity: Activity
}

class CameraProcessor {
    private val REQUEST_CAMERA_PERMISSION = 1
    
    private var cameraDevice: CameraDevice? = null
    private val cameraManager: CameraManager by lazy {
        this.activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private var cameraCaptureSession: CameraCaptureSession? = null
    private var activity: Activity
    private val textureView: TextureView

    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null

    var bitmap: Bitmap? = null


    constructor(cameraView: CameraView) {
        this.textureView = cameraView.textureView
        this.activity = cameraView.activity
    }

    private fun startBackgroundThread() {
        val thread = HandlerThread("CameraBackground")
        thread.start()

        mBackgroundHandler = Handler(thread.looper)
        mBackgroundThread = thread

        // thread.start() // ここだと thread.looper == null になる
    }
    private fun stopBackgroundThread() {
        mBackgroundThread?.quitSafely()

        mBackgroundThread?.join()
        mBackgroundThread = null
        mBackgroundHandler = null
    }

    private fun showDialogCameraPermission() {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
            return
        }

        AlertDialog.Builder(activity)
            .setMessage("R string request permission")
            .setPositiveButton(R.string.ok) { dialog, which ->
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
            .setNegativeButton(R.string.cancel) { dialog, which ->  }
            .create()
    }

    fun setup() {
        // カメラ搭載の有無をチェック
        if (activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) === false) return

        // カメラのアクセス権限を確認して、なければ許可を求める
        val currentPermssion = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        if (currentPermssion != PackageManager.PERMISSION_GRANTED) {
            showDialogCameraPermission()
            return
        }

        // すでにカメラが起動していたら何もしない
        if (cameraDevice !== null) {
            showToast("Camera started already.")
        }

//        startBackgroundThread()

        // 背面カメラの取得
        val cameraId: String = cameraManager.cameraIdList[0]
        cameraManager.openCamera(cameraId, object: CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                // カメラが取得できたらプレビューを生成する
                this@CameraProcessor.cameraDevice = cameraDevice
                createCameraPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                // カメラの取得に失敗したら終了する
                cameraDevice.close()
                this@CameraProcessor.cameraDevice = null
                stopBackgroundThread()
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                // カメラの取得に失敗したら終了する
                cameraDevice.close()
                this@CameraProcessor.cameraDevice = null
                stopBackgroundThread()
            }
        }, mBackgroundHandler)
    }

    private fun createPreviewRequest(cameraDevice: CameraDevice): CaptureRequest {

        val surface = Surface(this.textureView.surfaceTexture)
        val imageReader = ImageReader.newInstance(
            this.textureView.width,
            this.textureView.height,
            ImageFormat.JPEG,
            2
        )

        // プレビューテクスチャの設定
        val previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder.addTarget(surface)
        previewRequestBuilder.set(
            CaptureRequest.CONTROL_AF_MODE,
            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
        )

        return previewRequestBuilder.build()
    }

    private fun createCameraPreviewSession() {
        if (this.cameraDevice === null) return

        val cameraDevice = this.cameraDevice!!

        // プレビュー用のテクスチャ取得
        val texture = this.textureView.surfaceTexture

        val surface = Surface(texture)
        val imageReader = ImageReader.newInstance(
            this.textureView.width,
            this.textureView.height,
            ImageFormat.JPEG,
            2
        )

        val surfaces: List<Surface> = Arrays.asList(surface, imageReader.surface)

        val type = SessionConfiguration.SESSION_REGULAR
        val configurations: List<OutputConfiguration> = surfaces.map { OutputConfiguration(it) }
        val executor = this.activity.mainExecutor

        val previewRequest = this.createPreviewRequest(cameraDevice)
        val callback = object: CameraCaptureSession.StateCallback() {
            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                // 起動成功時に呼ばれる
                this@CameraProcessor.cameraCaptureSession = cameraCaptureSession
                cameraCaptureSession.setRepeatingRequest(previewRequest, null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                // 起動失敗時に呼ばれる
            }
        }

        // 起動
        val configuration = SessionConfiguration(type, configurations, executor, callback)
        cameraDevice.createCaptureSession(configuration)
    }

    fun take() {
        this.cameraCaptureSession?.stopRepeating()
        this.bitmap = this.textureView.bitmap

//        val request = this.createPreviewRequest(this.cameraDevice!!)
//        this.cameraCaptureSession?.setRepeatingRequest(request, null, null)
//        this.createCameraPreviewSession()
//        this.activity.finish()
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        toast.show()
    }
}