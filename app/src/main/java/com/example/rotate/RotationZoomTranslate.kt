
import android.view.MotionEvent
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

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
    //It detects the first 2 and second 2 fingers on the display and it the detects the movement after
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
                // TODO: android studio heeft golfjes  met tip bij onderstaande, dat moet gefixed
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

    // TODO: voeg commentaar toe, beschrijf wat deze functie berekend (wellicht eerst andere twee)
    //It calculates the points after rotate and zoom
    private fun translationAfterRotateAndZoom(
            angle: Float,
            zoom: Float,
            fX: Float,
            fY: Float,
            nfX: Float,
            nfY: Float): Pair<Float, Float> {

        // TODO: met import kunnen onderstaande regels korter, zonder de kotlin math
        val s = kotlin.math.sin(angle / 180f * kotlin.math.PI)
        val c = kotlin.math.cos(angle / 180f * kotlin.math.PI)
        // TODO: fix onderstaande varname naar camelCase
        val xNew = (fX * c - fY * s) * zoom
        val yNew = (fX * s + fY * c) * zoom

        // calculate translation needed to get to new point
        val tx = nfX - xNew
        val ty = nfY - yNew

        return Pair(tx.toFloat(), ty.toFloat())
    }

    // TODO: voeg commentaar toe, beschrijf wat deze functie berekend
    //It detects the first and the second length between the fingers and calculates by deviding it
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

    // TODO: voeg commentaar toe, beschrijf wat deze functie berekend
    //It calculates the angle between the first and second finger
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
        // TODO: android studio heeft wat suggesties over de atan2 functie hieronder... volg op.
        val angle1 =
            atan2((fY - sY).toDouble(), (fX - sX).toDouble()).toFloat()
        val angle2 =
            atan2((nfY - nsY).toDouble(), (nfX - nsX).toDouble()).toFloat()
        var angle = Math.toDegrees((angle2 - angle1).toDouble()).toFloat() % 360
        // TODO: voeg commentaar toe, beschrijf wat onderstaande regels doen.
        // If the angle is below -180 it will add up by 360
        // If the angle is above 180 it will minus by 360
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