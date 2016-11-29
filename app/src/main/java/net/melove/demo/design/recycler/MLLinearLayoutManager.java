package net.melove.demo.design.recycler;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by lzan13 on 2016/11/24.
 * RecyclerView 布局管理类
 */

public class MLLinearLayoutManager extends LinearLayoutManager {

    // 上下文对象
    private Context mContext;

    // 定义速度，px/ms
    private static final float MILLISECOND_PER_PX = 0.1f;

    public MLLinearLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
            final int position) {
        //super.smoothScrollToPosition(recyclerView, state, position);

        LinearSmoothScroller scroller = new LinearSmoothScroller(mContext) {
            /**
             * 计算滚动速度
             *
             * @param displayMetrics DisplayMetrics用于实际维度计算
             * @return 每个像素应该占用的时间（以毫秒为单位）。 例如，如果返回值为2 ms，则意味着使用LinearInterpolation滚动1000像素需要2秒。
             */
            @Override protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECOND_PER_PX / displayMetrics.density;
            }

            /**
             * 计算指定位置滚动的向量
             *
             * 如果布局管理器无法计算给定位置的滚动（例如，它没有当前滚动位置），则此方法可以返回null。
             *
             * @param targetPosition 指定滚动到的位置
             * @return 返回指定位置需要滚动的向量
             */
            @Nullable @Override public PointF computeScrollVectorForPosition(int targetPosition) {
                return MLLinearLayoutManager.this.computeScrollVectorForPosition(position);
            }
        };
        // 设置滚动到的位置
        scroller.setTargetPosition(position);
        // 开始进行平滑滚动
        startSmoothScroll(scroller);
    }
}
