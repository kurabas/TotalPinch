package com.example.rotate

import RotationGestureDetector
import RotationGestureDetector.OnRotationGestureListener
import android.app.Activity
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

class MainActivity : AppCompatActivity(), OnRotationGestureListener {
    private var mRotationDetector: RotationGestureDetector? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRotationDetector = RotationGestureDetector(this)
//        setContentView(R.layout.activity_main)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mRotationDetector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun OnRotation(rotationDetector: RotationGestureDetector?) {
        val angle = rotationDetector!!.angle
        Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
    }
}