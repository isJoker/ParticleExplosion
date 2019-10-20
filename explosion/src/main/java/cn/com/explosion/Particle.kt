package cn.com.explosion

import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by JokerWan on 2019-10-11.
 * Function: 爆破粒子（坐标、颜色）
 */
abstract class Particle(
    open val cx: Float,
    open val cy: Float,
    open val color: Int
) {
    protected abstract fun draw(canvas: Canvas, paint: Paint)
    protected abstract fun calculate(factor: Float)

    /**
     * 向前运动
     */
    fun advance(canvas: Canvas, paint: Paint, factor: Float) {
        draw(canvas, paint)
        calculate(factor)
    }
}