package com.google.imtoken.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom

class HideByScrollBehavior(
    context: Context,
    attr: AttributeSet?
) : CoordinatorLayout.Behavior<View>(context, attr) {

    private var hide = false        // 底部导航栏是否隐藏

    constructor(context: Context) : this(context, null)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        val animator = child.animate()
        val h = child.measuredHeight + child.marginBottom   //计算出需要移动的高度
        animator.duration = 500
        if (hide) {
            // 往下滑
            if (dyConsumed < 0) {
                animator.translationY(0f).start()
                hide = false
            }
        } else {
            // 往上滑
            if (dyConsumed > 0) {
                animator.translationY(h.toFloat()).start()
                hide = true
            }
        }
    }
}