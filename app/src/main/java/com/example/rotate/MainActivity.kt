package com.example.rotate

import RotationZoomGestureDetector
import RotationZoomGestureDetector.OnRotationZoomGestureListener
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scaleMatrix


//TODO: verwijder onderstaand comments (tenzij er heel goede reden is het te bewaren)
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

// TODO: na verplaatsing wordt image niet gereset, maar begint rotatiematrix wel weer op 0, reset de image OF zorg dat de rotatiematrix blijft (en voeg dan evt reset knop toe)

class MainActivity : AppCompatActivity(), OnRotationZoomGestureListener {

    private var mazeImage: ImageView? = null
    private var mazeMatrix: Matrix = Matrix()
    private var mRotationDetector: RotationZoomGestureDetector? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRotationDetector = RotationZoomGestureDetector(this)
        setContentView(R.layout.activity_main)
        mazeImage = findViewById<ImageView>(R.id.imageView)
        mazeMatrix = mazeImage!!.getImageMatrix()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mRotationDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    override fun OnRotationZoom(rotationZoomDetector: RotationZoomGestureDetector?) {
        val angle = rotationZoomDetector!!.angle
        // TODO: hieronder is de !! operator niet meer nodig zegt andriod studio... bedenk why?
        val zoom = rotationZoomDetector!!.zoom
        val (tx, ty) = rotationZoomDetector!!.translation

//        Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
        Log.d("RotationGestureDetector", "Zoom: " + java.lang.Float.toString(zoom))

// TODO: remove comment below, voeg evt comment to achter set (waarom geen assignment)

//        val newMatrix = mazeMatrix

        val newMatrix = Matrix()

        newMatrix.set(mazeMatrix)
        newMatrix.postRotate(angle)
        newMatrix.postScale(zoom, zoom)
        newMatrix.postTranslate(tx, ty)

        mazeImage?.imageMatrix = newMatrix

// TODO: remove comments below, behalve als er heel goede reden is om ze te houden.

//        mazeImage!!.setImageMatrix(newMatrix)
//        postRotate(float degrees)
//        postScale (float sx,
//            float sy)
    }
}

