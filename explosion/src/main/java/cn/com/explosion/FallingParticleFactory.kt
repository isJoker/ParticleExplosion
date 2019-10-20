package cn.com.explosion

import android.graphics.Bitmap
import android.graphics.Rect

/**
 * Created by JokerWan on 2019-10-11.
 * Function:
 */
class FallingParticleFactory : ParticleFactory() {

    companion object {
        // 默认粒子宽高
        const val PART_W_H = 8f
    }


    override fun generateParticles(bitmap: Bitmap, bound: Rect): Array<Array<Particle?>> {
        val w = bound.width()
        val h = bound.height()

        var partWCount = (w / PART_W_H).toInt()//横向个数
        var partHCount = (h / PART_W_H).toInt() //竖向个数

        // 如果图片本身不够一个粒子，算一个
        if (partWCount == 0) {
            partWCount = 1
        }
        if (partHCount == 0) {
            partHCount = 1
        }
        val bitmapPartW = bitmap.width / partWCount
        val bitmapPartH = bitmap.height / partHCount

        val particles = Array<Array<Particle?>>(partHCount) { arrayOfNulls(partWCount) }
        for (row in 0 until partHCount) { // 行数
            for (column in 0 until partWCount) {// 列数
                // 取得当前粒子所在位置的颜色
                val color = bitmap.getPixel(column * bitmapPartW, row * bitmapPartH)

                val x = bound.left + PART_W_H * column
                val y = bound.top + PART_W_H * row
                particles[row][column] = FallingParticle(x, y, color, bound)
            }
        }
        return particles
    }
}