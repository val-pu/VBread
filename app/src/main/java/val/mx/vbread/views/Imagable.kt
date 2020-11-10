package `val`.mx.vbread.views

import `val`.mx.vbread.Complex
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.ColorSpace
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

class Imagable(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private val paint = Paint()
    private val paintBlue = Paint()
    private val paintGray = Paint()
    private lateinit var canvas: Canvas;


    var START = -2.0
    var END = 2.0
    var VERTICAL_START = -2.0
    var VERTICAL_END = 2.0

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)



        if (canvas != null) {
            this.canvas = canvas
            paint()

        }

    }


    fun paint() {

        val width: Int = Resources.getSystem().getDisplayMetrics().heightPixels

        paint.color = BLACK
        paintGray.color = Color.rgb(25, 7, 26)
        paintBlue.color = Color.rgb(9, 1, 47)


        var res1: Complex?
        var res: Complex


        for (i in 0..width) {
            val imag = getPoints(VERTICAL_START, VERTICAL_END, width / 2, i)

            for (j in 0..width) {

//                    canvas.drawPoint(i asj as Float, paint)


//                System.out.println("x=" + getPoints(START,END,40,j));
                res1 = getPoints(START, END, width / 2, j)?.let {
                    imag?.let { it1 ->
                        Complex(
                            it,
                            it1
                        )
                    }
                }
                res = Complex(0.0, 0.0)

                var itera: Int = 0
                for (k in 0..16) {


                    res = res.multiply(res).add(res1)
                    if (res.toDouble() > 2) {


                        val paintM: Paint = Paint()


                        if(itera != 0)
                        paintM.color = Color.rgb(10*itera,255/19*itera + 20,255/itera)

                        canvas.drawPoint(j.toFloat(), i.toFloat(), paintM)

//                        when (itera) {
//                            1 -> {
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintGray)
//                            }
//                            2-> {
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintBlue)
//
//                            }
//                            3-> {
//
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(9, 1, 47)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            4-> {
//
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(4, 4, 73)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            5-> {
//
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(0, 7, 100)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//
//                            6 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(12, 44, 138)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//
//                            7 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(24, 82, 177)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            8 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(57, 125, 209)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            9 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(134, 181, 229)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            10 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(211, 236, 248)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            11 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(241, 233, 191)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//                            12 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(248, 201, 95)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }13 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(255, 170, 0)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }14 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(204, 128, 0)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }15 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(153, 87, 0)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }16 -> {
//                                val paintM : Paint = Paint()
//
//                                paintM.color = Color.rgb(106, 52, 3)
//
//                                canvas.drawPoint(j.toFloat(),i.toFloat(),paintM)
//
//                            }
//
//
//                            //SOURCE https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
//
//                        }

                        break
                    }
                    itera++;
                }
                if (res.toDouble() < 2) {
                    canvas.drawPoint(j.toFloat(), i.toFloat(), paint)
                }
            }
        }

    }

    fun getPoints(low: Double, high: Double, count: Int, index: Int): Double? {
        val combinedRange = Math.abs(high - low)
        val step = combinedRange / count
        return step * index + low
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }


}