package com.example.rotate

import RotationZoomTranslate
import RotationZoomTranslate.OnRotationZoomGestureListener
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


//TODO: verwijder onderstaand comments (tenzij er heel goede reden is het te bewaren)
// TODO: na verplaatsing wordt image niet gereset, maar begint rotatiematrix wel weer op 0, reset de image OF zorg dat de rotatiematrix blijft (en voeg dan evt reset knop toe)

class MainActivity : AppCompatActivity(), OnRotationZoomGestureListener {

    private var mazeImage: ImageView? = null
    private var mazeMatrix: Matrix = Matrix()
    private var mRotationDetector: RotationZoomTranslate? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRotationDetector = RotationZoomTranslate(this)
        setContentView(R.layout.activity_main)
        mazeImage = findViewById<ImageView>(R.id.imageView)
        mazeMatrix = mazeImage!!.getImageMatrix()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mRotationDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    override fun OnRotationZoom(angle: Float, zoom: Float, translation: Pair<Float, Float>) {
        val (tx, ty) = translation

//        Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
        Log.d("RotationGestureDetector", "Zoom: " + java.lang.Float.toString(zoom))

        val newMatrix = Matrix()

        newMatrix.set(mazeMatrix)
        newMatrix.postRotate(angle)
        newMatrix.postScale(zoom, zoom)
        newMatrix.postTranslate(tx, ty)

        mazeImage?.imageMatrix = newMatrix

    }
}

