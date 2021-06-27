package com.example.flightgearcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class JoystickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var listeners=  mutableListOf<JoystickListener>() // List of listeners
    private var center: PointF = PointF() // knob center point
    private var radius = 0.0F
    // colors for draw
    private val paintOut = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#C3B4C3")
        isAntiAlias = true
    }
    private val paintIn = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#EAC9EE")
        isAntiAlias = true
    }
    private val paintRecOut = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#B4BC07")
        isAntiAlias = true
    }
    private val paintRecIn = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#09A5BC")
        isAntiAlias = true
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = 0.3f* min(width, height).toFloat()
        center = PointF(width/2.0f, height/2.0f)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background
        canvas.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paintRecOut)
        canvas.drawRect(width.toFloat() * 0.05f, height.toFloat() * 0.05f, width - width.toFloat() * 0.05f, height - height.toFloat() * 0.05f, paintRecIn)
        // Draw knob
        canvas.drawCircle(center.x, center.y, radius, paintOut)
        canvas.drawCircle(center.x, center.y, (radius * 0.8).toFloat(), paintIn)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> {
                center = PointF(width/2.0f, height/2.0f)
                onCenterChanged()
                invalidate()
            }
        }
        return true
    }

    private fun touchMove(x: Float, y: Float){
        // If touch in legal bounds
        if (x + radius < width && y + radius < height && x > radius && y > radius) {
            center.x = x
            center.y = y
            onCenterChanged()
            invalidate()
        }
    }

    fun addListener(listener: JoystickListener) {
        listeners.add(listener)
    }

    // Notify listeners when center changed
    private fun onCenterChanged() {
        val relX = (center.x - width.toFloat() / 2) / (width.toFloat() / 2 - radius)
        val relY = (height.toFloat() / 2 - center.y) / (height.toFloat() / 2 - radius)
        for (i in listeners) {
            i.centerChanged(relX, relY)
        }
    }
}

// Interface for listeners
interface JoystickListener {
    fun centerChanged(x: Float, y: Float)
}