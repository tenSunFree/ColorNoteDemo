package com.home.colornotedemo.main.view.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

open class MainAddNoteTableView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint: Paint = initializeBasicSetting(canvas)
        drawTable(canvas, paint)
    }

    private fun initializeBasicSetting(canvas: Canvas): Paint {
        val paint = Paint()
        canvas.drawColor(0xFFFFF8BB.toInt()) // 畫布背景設置
        paint.color = 0xFFF1DC31.toInt() // 設置畫筆顏色
        paint.style = Paint.Style.FILL_AND_STROKE // 設置填充樣式
        paint.strokeWidth = 1F // 設置畫筆寬度
        return paint
    }

    private fun drawTable(canvas: Canvas, paint: Paint) {
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height
        var height = 0F
        val startX = 0F
        val itemHeightDp = 23.5F
        while (height <= canvasHeight) {
            height += convertDpToPixel(itemHeightDp)
            canvas.drawLine(startX, height, canvasWidth.toFloat(), height, paint)
        }
    }

    /**
     * Covert dp to px
     */
    private fun convertDpToPixel(dp: Float): Float {
        val displayMetrics = context.resources.displayMetrics
        return dp * displayMetrics.density
    }
}