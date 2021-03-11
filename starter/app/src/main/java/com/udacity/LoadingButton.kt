package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()
    private var arcRect = RectF()
    private var currentSweepAngle = 0
    private var currentRectangleWidth = 0f

    private var buttonText = resources.getString(R.string.button_completed)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
            }
            ButtonState.Loading -> {
                isEnabled = false
                startAnimation()
            }
            ButtonState.Completed -> {
                isEnabled = true
            }
        }
    }

    init {
        isClickable = true
        contentDescription = resources.getString(R.string.button_completed)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val circleRadius = 120f
        val margin = (h.toFloat() - circleRadius) / 2
        Timber.e("top ${h.toFloat()}")
        arcRect.set(w.toFloat() - circleRadius - margin, h.toFloat() - circleRadius - margin, w.toFloat() - margin, h.toFloat() - margin)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = ContextCompat.getColor(context, R.color.button_complete)
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = ContextCompat.getColor(context, R.color.button_loading)
        canvas.drawRect(0f, 0f, currentRectangleWidth, heightSize.toFloat(), paint)
        paint.color = ContextCompat.getColor(context, R.color.button_circle)
        canvas.drawArc(arcRect, 0f, currentSweepAngle.toFloat(), true, paint)
        paint.color = ContextCompat.getColor(context, R.color.white)
        canvas.drawText(buttonText, widthSize.toFloat() / 2, heightSize.toFloat() / 2 + 20f, paint)
    }

    private fun startAnimation() {
        valueAnimator.cancel()
        valueAnimator = ValueAnimator.ofInt(0, 360).apply {
            duration = 3000
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentSweepAngle = valueAnimator.animatedValue as Int
                currentRectangleWidth = (valueAnimator.animatedValue as Int * width / 360f)
                invalidate()
            }

            doOnStart {
                buttonText = resources.getString(R.string.button_loading)
            }

            doOnEnd {
                buttonText = resources.getString(R.string.button_completed)
                currentSweepAngle = 0
                currentRectangleWidth = 0f
                invalidate()
                buttonState = ButtonState.Completed
            }
        }
        valueAnimator.start()
    }
}