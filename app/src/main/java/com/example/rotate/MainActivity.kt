package com.example.rotate

import RotationZoomGestureDetector
import RotationZoomGestureDetector.OnRotationZoomGestureListener
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

class MainActivity : AppCompatActivity(), OnRotationZoomGestureListener {
    private var mRotationDetector: RotationZoomGestureDetector? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRotationDetector = RotationZoomGestureDetector(this)
//        setContentView(R.layout.activity_main)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mRotationDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun OnRotationZoom(rotationZoomDetector: RotationZoomGestureDetector?) {
        val angle = rotationZoomDetector!!.angle
        val zoom = rotationZoomDetector!!.zoom
        Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
        Log.d("RotationGestureDetector", "Zoom: " + java.lang.Float.toString(zoom))
    }
}

