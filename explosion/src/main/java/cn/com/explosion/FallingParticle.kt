package cn.com.explosion

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

/**
 * Created by JokerWan on 2019-10-11.
 * Function: 自由落体的粒子
 */
class FallingParticle(
    override var cx: Float,
    override var cy: Float,
    override var color: Int,
    var bound: Rect? = null
) : Particle(cx, cy, color) {

    private var radius = FallingParticleFactory.PART_W_H
    private var alpha = 1.0f

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        paint.alpha = (Color.alpha(color) * alpha).toInt()// 保证透明颜色不是黑色
        canvas.drawCircle(cx, cy, radius, paint)
    }

    override fun calculate(factor: Float) {
        cx += factor * Utils.RANDOM.nextInt(bound?.width()!!) * (Utils.RANDOM.nextFloat() - 0.5f)
        cy += factor * Utils.RANDOM.nextInt(bound?.height()!! / 2)

        radius -= factor * Utils.RANDOM.nextInt(2)

        alpha = (1f - factor) * (1 + Utils.RANDOM.nextFloat())
    }
}