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


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

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
        val zoom = rotationZoomDetector!!.zoom
        val (tx, ty) = rotationZoomDetector!!.translation

//        Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
        Log.d("RotationGestureDetector", "Zoom: " + java.lang.Float.toString(zoom))

//        val newMatrix = mazeMatrix

        val newMatrix = Matrix()

        newMatrix.set(mazeMatrix)
        newMatrix.postRotate(angle)
        newMatrix.postScale(zoom, zoom)
        newMatrix.postTranslate(tx, ty)

        mazeImage?.imageMatrix = newMatrix


//        mazeImage!!.setImageMatrix(newMatrix)
//        postRotate(float degrees)
//        postScale (float sx,
//            float sy)
    }
}

