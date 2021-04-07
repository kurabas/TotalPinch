package com.example.rotate

import RotationZoomTranslateDetector
import RotationZoomTranslateDetector.OnRotationZoomTranslateListener
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


// TODO: na verplaatsing wordt image niet gereset, maar begint rotatiematrix wel weer op 0, reset de image OF zorg dat de rotatiematrix blijft (en voeg dan evt reset knop toe)

class MainActivity : AppCompatActivity(), OnRotationZoomTranslateListener {

    private var mazeImage: ImageView? = null
    private var mazeMatrix: Matrix = Matrix()
    private var rztDetector: RotationZoomTranslateDetector? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rztDetector = RotationZoomTranslateDetector(this)
        setContentView(R.layout.activity_main)
        mazeImage = findViewById<ImageView>(R.id.imageView)
        mazeMatrix = mazeImage!!.getImageMatrix()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        rztDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    override fun onRotationZoomTranslate(angle: Float, zoom: Float, translation: Pair<Float, Float>) {
        val (tx, ty) = translation

        Log.d("RotationGestureDetector", "Rotation: " +
                java.lang.Float.toString(angle) +
                " Zoom: " +
                java.lang.Float.toString(zoom) +
                " Translation: (" +
                java.lang.Float.toString(tx) +
                ", " +
                java.lang.Float.toString(ty) +
                ")"
        )

        val newMatrix = Matrix()

        newMatrix.set(mazeMatrix)
        newMatrix.postRotate(angle)
        newMatrix.postScale(zoom, zoom)
        newMatrix.postTranslate(tx, ty)

        mazeImage?.imageMatrix = newMatrix

    }
}

