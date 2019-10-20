package cn.com.explosion

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

/**
 * Created by JokerWan on 2019-10-20.
 * Function:
 */
class ExplosionAnimator(private val container: View, bitmap: Bitmap, bound: Rect, particleFactory: ParticleFactory) :
    ValueAnimator() {

    companion object {
        const val DEFAULT_DURATION = 1500
    }
    private val mParticles: Array<Array<Particle?>> = particleFactory.generateParticles(bitmap, bound)
    private val mPaint: Paint = Paint()

    init {
        setFloatValues(0.0f, 1.0f)
        duration = DEFAULT_DURATION.toLong()
    }

    fun draw(canvas: Canvas) {
        if (!isStarted) { //动画结束时停止
            return
        }
        for (particle in mParticles) {
            for (p in particle) {
                p?.advance(canvas, mPaint, animatedValue as Float)
            }
        }
        container.invalidate()
    }

    override fun start() {
        super.start()
        container.invalidate()
    }
}