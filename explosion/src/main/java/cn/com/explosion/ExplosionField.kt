package cn.com.explosion

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import cn.com.explosion.Utils.Companion.RANDOM

/**
 * Created by JokerWan on 2019-10-20.
 * Function:
 */
class ExplosionField(context: Context?, private var particleFactory: ParticleFactory?) :
    View(context) {

    private val explosionAnimators = ArrayList<ExplosionAnimator>()
    private val explosionAnimatorsMap = HashMap<View, ExplosionAnimator>()
    private var clickCallback: ClickCallback? = null
    private val clickListener by lazy {
        OnClickListener {
            this@ExplosionField.explode(it)
            clickCallback?.onClick(it)
        }
    }


    init {
        attach2Activity(context as? Activity)
    }

    private fun attach2Activity(activity: Activity?) {
        activity?.run {
            val rootView = this.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            rootView.addView(this@ExplosionField, layoutParams)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (explosionAnimator in explosionAnimators) {
            canvas?.let { explosionAnimator.draw(it) }
        }
    }

    private fun explode(view: View) {
        // 防止重复点击
        val explosionAnimator = explosionAnimatorsMap[view]
        if (explosionAnimator != null && explosionAnimator.isStarted) {
            return
        }
        // 可见时才执行动画
        if (view.visibility != VISIBLE || view.alpha == 0f) {
            return
        }

        val rect = Rect()
        //得到view相对于整个屏幕的坐标
        view.getGlobalVisibleRect(rect)
        val contentTop = (parent as ViewGroup).top
        val frame = Rect()
        (context as Activity).window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        //去掉状态栏高度和标题栏高度
        rect.offset(0, -contentTop - statusBarHeight)
        if (rect.width() == 0 || rect.height() == 0) {
            return
        }

        //震动动画
        val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(150)
        animator.addUpdateListener {
            view.translationX = (RANDOM.nextFloat() - 0.5f) * view.width * 0.05f
            view.translationY = (RANDOM.nextFloat() - 0.5f) * view.height * 0.05f
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                explode(view, rect)
            }
        })
        animator.start()
    }

    private fun explode(view: View, rect: Rect) {
        val animator = particleFactory?.let {
            ExplosionAnimator(this, Utils.createBitmapFromView(view)!!, rect, it)
        }
        animator?.run {
            explosionAnimators.add(this)
            explosionAnimatorsMap[view] = this
        }
        animator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.isClickable = false
                //缩小,透明动画
                view.animate().setDuration(150).scaleX(0f).scaleY(0f).alpha(0f).start()
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(150).start()
                view.isClickable = true
                //动画结束时从动画集中移除
                explosionAnimators.remove(animation)
                explosionAnimatorsMap.remove(view)
            }
        })
        animator?.start()

    }

    /**
     * 希望谁有破碎效果，就给谁加Listener
     *
     * @param view 可以是ViewGroup
     */
    fun addListener(view: View) {
        if (view is ViewGroup) {
            val count = view.childCount
            for (i in 0 until count) {
                addListener(view.getChildAt(i))
            }
        } else {
            view.isClickable = true
            view.setOnClickListener(getOnClickListener())
        }
    }

    private fun getOnClickListener(): OnClickListener {
        return clickListener
    }

    fun setClickCallback(clickCallback: ClickCallback?) {
        this.clickCallback = clickCallback
    }
}