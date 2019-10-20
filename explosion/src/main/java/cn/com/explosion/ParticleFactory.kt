package cn.com.explosion

import android.graphics.Bitmap
import android.graphics.Rect

/**
 * Created by JokerWan on 2019-10-11.
 * Function: 生成粒子抽象工厂类
 */
abstract class ParticleFactory {
    abstract fun generateParticles(bitmap: Bitmap, bound: Rect): Array<Array<Particle?>>
}