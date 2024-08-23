package com.unicorns.invisible.no65.view.dpad

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.util.launchCoroutineOnIO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.*


class CircleDPadMk2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val centerButtonPaint: Paint
    private val dividersPaint: Paint
    private val crossPaint: Paint

    private val pressedState = BooleanArray(NUMBER_OF_CROSS_BUTTONS)
    private val movePending = BooleanArray(NUMBER_OF_CROSS_BUTTONS)

    var listener: OnClickCircleDPadListener? = null

    private val drawableButtonResources = IntArray(NUMBER_OF_CROSS_BUTTONS)
    private val crossButtonsColors = arrayOfNulls<ColorStateList>(NUMBER_OF_CROSS_BUTTONS)
    private var drawableTintColor: ColorStateList? = null

    private var dividersWidth = 16f
    private var dividersColor = ContextCompat.getColor(context, R.color.white)

    private var centerButtonRadius = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.CircleDPadMk2) {
            dividersWidth = getDimensionPixelSize(R.styleable.CircleDPadMk2_dividersWidth, dividersWidth.toInt()).toFloat()
            dividersColor = getColor(R.styleable.CircleDPadMk2_dividersColor, dividersColor)

            drawableButtonResources[DPadButton.LEFT_BUTTON.number()] =
                getResourceId(R.styleable.CircleDPadMk2_leftButtonDrawable, R.drawable.ic_arrow_left_24)
            drawableButtonResources[DPadButton.TOP_BUTTON.number()] =
                getResourceId(R.styleable.CircleDPadMk2_topButtonDrawable, R.drawable.ic_arrow_top_24)
            drawableButtonResources[DPadButton.RIGHT_BUTTON.number()] =
                getResourceId(R.styleable.CircleDPadMk2_rightButtonDrawable, R.drawable.ic_arrow_right_24)
            drawableButtonResources[DPadButton.BOTTOM_BUTTON.number()] =
                getResourceId(R.styleable.CircleDPadMk2_bottomButtonDrawable, R.drawable.ic_arrow_bottom_24)

            crossButtonsColors[DPadButton.LEFT_BUTTON.number()] =
                getColorStateList(R.styleable.CircleDPadMk2_leftButtonColor)
                    ?: ContextCompat.getColorStateList(context, R.color.dpad_cross_button_color)
            crossButtonsColors[DPadButton.TOP_BUTTON.number()] =
                getColorStateList(R.styleable.CircleDPadMk2_topButtonColor)
                    ?: ContextCompat.getColorStateList(context, R.color.dpad_cross_button_color)
            crossButtonsColors[DPadButton.RIGHT_BUTTON.number()] =
                getColorStateList(R.styleable.CircleDPadMk2_rightButtonColor)
                    ?: ContextCompat.getColorStateList(context, R.color.dpad_cross_button_color)
            crossButtonsColors[DPadButton.BOTTOM_BUTTON.number()] =
                getColorStateList(R.styleable.CircleDPadMk2_bottomButtonColor)
                    ?: ContextCompat.getColorStateList(context, R.color.dpad_cross_button_color)

            drawableTintColor = getColorStateList(R.styleable.CircleDPadMk2_buttonDrawableTint)
                ?: ContextCompat.getColorStateList(context, R.color.dpad_tint_color)

        }
        crossPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        centerButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dividersPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dividersPaint.strokeWidth = dividersWidth
        dividersPaint.color = dividersColor
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val halfWidth = width / 2f
        val halfHeight = height / 2f
        val radius = min(halfHeight, halfWidth)
        centerButtonRadius = radius * CENTER_RADIUS_PERCENTAGE
        val background = background
        background?.draw(canvas)
        val rect = Rect()
        getDrawingRect(rect)
        repeat(NUMBER_OF_CROSS_BUTTONS) { i ->
            val startAngle = getStartAngle(i)
            drawCrossButton(
                canvas,
                RectF(rect),
                crossButtonsColors[i]!!,
                startAngle,
                pressedState[i]
            )
        }
        drawCenterButton(canvas, halfWidth, halfHeight, dividersColor)

        repeat(NUMBER_OF_CROSS_BUTTONS) { i ->
            val startAngle = getStartAngle(i)
            drawDividers(canvas, radius, startAngle, halfWidth, halfHeight)
            drawImage(canvas, i, halfWidth, halfHeight, centerButtonRadius / 2)
        }
    }

    private fun drawCrossButton(
        canvas: Canvas,
        rectF: RectF,
        color: ColorStateList,
        startAngle: Int,
        isPressed: Boolean
    ) {
        crossPaint.color = if (isPressed) color.getColorForState(
            intArrayOf(android.R.attr.state_pressed),
            color.defaultColor
        ) else color.defaultColor
        canvas.drawArc(rectF, startAngle.toFloat(), 90f, true, crossPaint)
    }

    private fun drawCenterButton(
        canvas: Canvas,
        halfWidth: Float,
        halfHeight: Float,
        color: Int
    ) {
        centerButtonPaint.style = Paint.Style.FILL
        centerButtonPaint.color = color
        canvas.drawCircle(halfWidth, halfHeight, centerButtonRadius, centerButtonPaint)
    }

    private fun drawDividers(
        canvas: Canvas,
        radius: Float,
        startAngle: Int,
        halfWidth: Float,
        halfHeight: Float
    ) {
        canvas.drawLine(
            halfWidth, halfHeight,
            radius * cos(Math.toRadians(startAngle.toDouble())).toFloat() + halfWidth,
            radius * sin(Math.toRadians(startAngle.toDouble())).toFloat() + halfHeight,
            dividersPaint
        )
    }

    private fun drawImage(
        canvas: Canvas,
        buttonId: Int,
        halfWidth: Float,
        halfHeight: Float,
        halfInnerRadius: Float
    ) {
        val button = DPadButton.getByNumber(buttonId)
        val drawableToDraw = ContextCompat.getDrawable(context, drawableButtonResources[buttonId])
        drawableToDraw?.let {
            val halfDrawableWidth = drawableToDraw.intrinsicWidth / 2
            val halfDrawableHeight = drawableToDraw.intrinsicHeight / 2
            when (button) {
                DPadButton.LEFT_BUTTON -> drawableToDraw.setBounds(
                    (halfWidth / 2 - halfInnerRadius - halfDrawableWidth).toInt(),
                    halfHeight.toInt() - halfDrawableHeight,
                    (halfWidth / 2 - halfInnerRadius + halfDrawableWidth).toInt(),
                    halfHeight.toInt() + halfDrawableHeight
                )
                DPadButton.RIGHT_BUTTON -> drawableToDraw.setBounds(
                    (halfWidth + halfInnerRadius + halfWidth / 2).toInt() - halfDrawableWidth,
                    halfHeight.toInt() - halfDrawableHeight,
                    (halfWidth + halfInnerRadius + halfWidth / 2).toInt() + halfDrawableWidth,
                    halfHeight.toInt() + halfDrawableHeight
                )
                DPadButton.TOP_BUTTON -> drawableToDraw.setBounds(
                    halfWidth.toInt() - halfDrawableWidth,
                    (halfHeight / 2 - halfInnerRadius - halfDrawableHeight).toInt(),
                    halfWidth.toInt() + halfDrawableWidth,
                    (halfHeight / 2 - halfInnerRadius + halfDrawableHeight).toInt()
                )
                DPadButton.BOTTOM_BUTTON -> drawableToDraw.setBounds(
                    halfWidth.toInt() - halfDrawableWidth,
                    (halfHeight + halfInnerRadius + halfHeight / 2).toInt() - halfDrawableHeight,
                    halfWidth.toInt() + halfDrawableWidth,
                    (halfHeight + halfInnerRadius + halfHeight / 2).toInt() + halfDrawableHeight
                )
            }

            val tintColor = drawableTintColor!!
            val colorId = if (pressedState[buttonId]) tintColor.getColorForState(
                intArrayOf(android.R.attr.state_pressed),
                tintColor.defaultColor
            ) else tintColor.defaultColor

            drawableToDraw.setTint(colorId)
            drawableToDraw.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x - width / 2f
        val y = event.y - height / 2f
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            val radius = sqrt(x.pow(2) + y.pow(2))
            if (radius < centerButtonRadius * CENTER_BUTTON_TOLERANCE) {
                setPressedStateFalse()
            } else {
                var touchAngle = Math.toDegrees(
                    atan2(y.toDouble(), x.toDouble())
                ).toFloat()
                if (touchAngle < 0) {
                    touchAngle += 360f
                }
                repeat(NUMBER_OF_CROSS_BUTTONS) { i ->
                    val startAngle = getStartAngle(i)
                    var endAngle = (startAngle + CROSS_BUTTON_ANGLE) % 360
                    if (startAngle > endAngle) {
                        if (touchAngle < startAngle && touchAngle < endAngle) {
                            touchAngle += 360f
                        }
                        endAngle += 360
                    }
                    pressedState[i] = startAngle <= touchAngle && touchAngle <= endAngle
                    if (action == MotionEvent.ACTION_DOWN && pressedState[i]) {
                        movePending[i] = true
                    }
                }
            }
        } else if (event.action == MotionEvent.ACTION_UP) {
            setPressedStateFalse()
        }
        this.invalidate()
        return true
    }

    private fun setPressedStateFalse() {
        repeat(NUMBER_OF_CROSS_BUTTONS) { i ->
            pressedState[i] = false
        }
    }

    /**
     *     270
     * 180 --- 0
     *     90
     */
    private fun getStartAngle(i: Int): Int {
        return CROSS_BUTTON_ANGLE / 2 + CROSS_BUTTON_ANGLE * i
    }

    private val job = launchCoroutineOnIO {
        while (isActive) {
            repeat(NUMBER_OF_CROSS_BUTTONS) { i ->
                if (pressedState[i] || movePending[i]) {
                    listener?.onClickButton(DPadButton.getByNumber(i))
                }
                movePending[i] = false
            }
            delay(DELAY_MILLISECONDS)
        }
    }
    fun stop() {
        job.cancel()
    }

    interface OnClickCircleDPadListener {
        fun onClickButton(button: DPadButton)
    }

    companion object {
        const val CENTER_RADIUS_PERCENTAGE = 0.33f
        const val CENTER_BUTTON_TOLERANCE = 0.5f
        const val NUMBER_OF_CROSS_BUTTONS = 4
        const val CROSS_BUTTON_ANGLE = 360 / NUMBER_OF_CROSS_BUTTONS
        const val DELAY_MILLISECONDS = 320L
    }
}