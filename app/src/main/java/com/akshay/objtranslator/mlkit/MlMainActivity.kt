package com.akshay.objtranslator.mlkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.akshay.objtranslator.BuildConfig
import com.akshay.objtranslator.R
import com.akshay.objtranslator.mlkit.ar.BoundingBoxArOverlay
import com.akshay.objtranslator.mlkit.ar.PathInterpolator
import com.akshay.objtranslator.mlkit.ar.PositionTranslator
import timber.log.Timber

class MlMainActivity : AppCompatActivity() {

    private lateinit var imageAnalyzer: ClassifyObjectImageAnalyzer

    private val camera
        get() = supportFragmentManager.findFragmentById(R.id.cameraFragment) as CameraFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup logging
        if (Timber.treeCount() == 0) {
            Timber.plant(Timber.DebugTree())
        }

        val boundingBoxArOverlay = BoundingBoxArOverlay(this,
            BuildConfig.DEBUG
        )
        imageAnalyzer = ViewModelProviders.of(this).get(ClassifyObjectImageAnalyzer::class.java)

        camera.imageAnalyzer = imageAnalyzer
        camera.arOverlayView.observe(camera, Observer {
            it.doOnLayout { view ->
                imageAnalyzer.arObjectTracker
                    .pipe(PositionTranslator(view.width, view.height))
                    .pipe(PathInterpolator())
                    .addTrackingListener(boundingBoxArOverlay)
            }

            it.add(boundingBoxArOverlay)
        })
    }
}
