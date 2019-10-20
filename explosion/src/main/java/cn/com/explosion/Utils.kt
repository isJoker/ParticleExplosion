package cn.com.explosion

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import java.util.*

/**
 * Created by JokerWan on 2019-10-11.
 * Function:
 */
class Utils {

    companion object {
        val RANDOM = Random(System.currentTimeMillis())
        private val CANVAS = Canvas()

        fun createBitmapFromView(view: View): Bitmap? {
            view.clearFocus()//使View失去焦点恢复原本样式
            val bitmap = createBitmapSafely(view.width, view.height, Bitmap.Config.ARGB_8888, 1)
            if (bitmap != null) {
                synchronized(CANVAS) {
                    CANVAS.setBitmap(bitmap)
                    view.draw(CANVAS)
                    CANVAS.setBitmap(null)
                }
            }
            return bitmap
        }

        /**
         * @param width
         * @param height
         * @param config
         * @param retryCount 重试次数，发生内存溢出时回收重试
         * @return
         */
        private fun createBitmapSafely(
            width: Int,
            height: Int,
            config: Bitmap.Config,
            retryCount: Int
        ): Bitmap? {
            try {
                return Bitmap.createBitmap(width, height, config)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                if (retryCount > 0) {
                    System.gc()
                    return createBitmapSafely(width, height, config, retryCount - 1)
                }
                return null
            }

        }
    }
}