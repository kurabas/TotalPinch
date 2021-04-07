
import android.view.MotionEvent
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.PI

// detects the rotation, zoom and translation from motion events and calls
// onRotationZoomTranslate when motion has been detected
class RotationZoomTranslateDetector(private val mListener: OnRotationZoomTranslateListener?) {

    private var orgX1 = 0f
    private var orgY1 = 0f
    private var orgX2 = 0f
    private var orgY2 = 0f
    private var ptrID1: Int
    private var ptrID2: Int
    private var angle = 0f
    private var zoom = 1f
    private var translation = Pair(0f, 0f)

    //It detects the first 2 and second 2 fingers on the display and calls OnRotationZoom when a movement is detected
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> ptrID1 = event.getPointerId(event.actionIndex)
            MotionEvent.ACTION_POINTER_DOWN -> {
                ptrID2 = event.getPointerId(event.actionIndex)
                orgX1 = event.getX(event.findPointerIndex(ptrID1))
                orgY1 = event.getY(event.findPointerIndex(ptrID1))
                orgX2 = event.getX(event.findPointerIndex(ptrID2))
                orgY2 = event.getY(event.findPointerIndex(ptrID2))
            }
            MotionEvent.ACTION_MOVE -> if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                val curX1: Float = event.getX(event.findPointerIndex(ptrID1))
                val curY1: Float = event.getY(event.findPointerIndex(ptrID1))
                val curX2: Float = event.getX(event.findPointerIndex(ptrID2))
                val curY2: Float = event.getY(event.findPointerIndex(ptrID2))

                angle = angleBetweenLines(orgX1, orgY1, orgX2, orgY2, curX1, curY1, curX2, curY2)
                zoom = zoomBetweenLines(orgX1, orgY1, orgX2, orgY2, curX1, curY1, curX2, curY2)
                translation = translationAfterRotateAndZoom(angle, zoom, orgX2, orgY2, curX2, curY2)

                mListener?.onRotationZoomTranslate(angle, zoom, translation)
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
    // TODO: rename vars below (org/cur) LET OP: alleen 1, geen 2.
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

    // detects the first and the second length between the fingers and calculates the zoom factor
    // and divides it
    // TODO: rename vars below (org/cur)
    private fun zoomBetweenLines(
        sX: Float,
        sY: Float,
        fX: Float,
        fY: Float,
        nsX: Float,
        nsY: Float,
        nfX: Float,
        nfY: Float
    ): Float {
        //stelling van pythagoras voor de 1e en 2e lijn
        val firstLength = sqrt((fX - sX).pow(2) + (fY - sY).pow(2))
        val secondLength = sqrt((nfX - nsX).pow(2) + (nfY - nsY).pow(2))

        //zoom wordt berekend door de 2e en 1e lijn te delen
        val zoom = secondLength/firstLength
        return zoom
    }

    // calculates the angle between the original and current fingers
    // TODO: rename vars below (org/cur)
    private fun angleBetweenLines(
        sX: Float,
        sY: Float,
        fX: Float,
        fY: Float,
        nsX: Float,
        nsY: Float,
        nfX: Float,
        nfY: Float
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

    // will be called when a motion (rotation, zoom, translation) is detected
    interface OnRotationZoomTranslateListener {
        fun onRotationZoomTranslate(angle: Float, zoom: Float, translation: Pair<Float, Float>)
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
    }

    init {
        ptrID1 = INVALID_POINTER_ID
        ptrID2 = INVALID_POINTER_ID
    }
}