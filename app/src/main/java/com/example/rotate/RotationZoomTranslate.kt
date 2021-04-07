
import android.view.MotionEvent
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.PI

// TODO: voeg commentaar toe,beschrijf wat de class doet
// TODO: evt rename class, hij detecteert nu ook translations (maar RotationZoomTranslationGestureDetector is wat lang en ook niet heel helder...)
//RotationZoomTranslate, it detects the rotation, zoom and translation for an image
class RotationZoomTranslate(private val mListener: OnRotationZoomGestureListener?) {


    private var fX = 0f
    private var fY = 0f
    private var sX = 0f
    private var sY = 0f
    private var ptrID1: Int
    private var ptrID2: Int
    var angle = 0f
    var zoom = 1f
    var translation = Pair(0f, 0f)

    // TODO: voeg commentaar toe, beschrijf wat deze functie doet
    //It detects the first 2 and second 2 fingers on the display and calls OnRotationZoom when a movement is detected
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> ptrID1 = event.getPointerId(event.actionIndex)
            MotionEvent.ACTION_POINTER_DOWN -> {
                ptrID2 = event.getPointerId(event.actionIndex)
                sX = event.getX(event.findPointerIndex(ptrID1))
                sY = event.getY(event.findPointerIndex(ptrID1))
                fX = event.getX(event.findPointerIndex(ptrID2))
                fY = event.getY(event.findPointerIndex(ptrID2))
            }
            MotionEvent.ACTION_MOVE -> if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                val nsX: Float = event.getX(event.findPointerIndex(ptrID1))
                val nsY: Float = event.getY(event.findPointerIndex(ptrID1))
                val nfX: Float = event.getX(event.findPointerIndex(ptrID2))
                val nfY: Float = event.getY(event.findPointerIndex(ptrID2))

                angle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY)
                zoom = zoomBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY)
                translation = translationAfterRotateAndZoom(angle, zoom, fX, fY, nfX, nfY)

                mListener?.OnRotationZoom(this)
            }
            MotionEvent.ACTION_UP -> ptrID1 = INVALID_POINTER_ID
            MotionEvent.ACTION_POINTER_UP -> ptrID2 = INVALID_POINTER_ID
            MotionEvent.ACTION_CANCEL -> {
                ptrID1 = INVALID_POINTER_ID
                ptrID2 = INVALID_POINTER_ID
            }
        }
        return true
    }

    // Calculates the translation after rotate and zoom
    private fun translationAfterRotateAndZoom(
            angle: Float,
            zoom: Float,
            fX: Float,
            fY: Float,
            nfX: Float,
            nfY: Float): Pair<Float, Float> {

        //Calculate the rotation
        val s = sin(angle / 180f * PI)
        val c = cos(angle / 180f * PI)

        //Calculate the zoom
        val xNew = (fX * c - fY * s) * zoom
        val yNew = (fX * s + fY * c) * zoom

        // calculate translation needed to get to new point
        val tx = nfX - xNew
        val ty = nfY - yNew

        return Pair(tx.toFloat(), ty.toFloat())
    }

    // TODO: voeg commentaar toe, beschrijf wat deze functie berekend
    // detects the first and the second length between the fingers and calculates the zoom factor
    // and divides it
    private fun zoomBetweenLines(
        fX: Float,
        fY: Float,
        sX: Float,
        sY: Float,
        nfX: Float,
        nfY: Float,
        nsX: Float,
        nsY: Float
    ): Float {
        //stelling van pythagoras voor de 1e en 2e lijn
        val firstLength = sqrt((fX - sX).pow(2) + (fY - sY).pow(2))
        val secondLength = sqrt((nfX - nsX).pow(2) + (nfY - nsY).pow(2))

        //zoom wordt berekend door de 2e en 1e lijn te delen
        val zoom = secondLength/firstLength
        return zoom

    }

    // calculates the angle between the original and current fingers
    private fun angleBetweenLines(
        fX: Float,
        fY: Float,
        sX: Float,
        sY: Float,
        nfX: Float,
        nfY: Float,
        nsX: Float,
        nsY: Float
    ): Float {
        val angle1 =
            atan2((fY - sY).toDouble(), (fX - sX).toDouble()).toFloat()
        val angle2 =
            atan2((nfY - nsY).toDouble(), (nfX - nsX).toDouble()).toFloat()
        var angle = Math.toDegrees((angle2 - angle1).toDouble()).toFloat() % 360

        // Make the calculated angle between -180 and 180 degrees
        if (angle < -180f) angle += 360.0f
        if (angle > 180f) angle -= 360.0f
        return angle
    }

    // TODO: voeg commentaar toe, beschrijf wat deze interface doet
    interface OnRotationZoomGestureListener {
        fun OnRotationZoom(rotationZoomDetector: RotationZoomTranslate?)
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
    }

    init {
        ptrID1 = INVALID_POINTER_ID
        ptrID2 = INVALID_POINTER_ID
    }
}