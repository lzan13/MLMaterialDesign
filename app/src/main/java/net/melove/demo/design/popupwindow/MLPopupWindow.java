package net.melove.demo.design.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import net.melove.demo.design.R;

/**
 * Created by lzan13 on 2016/12/1.
 */

public class MLPopupWindow extends PopupWindow {

    protected Context mContext;

    public MLPopupWindow(Context context, int layoutId) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutId, null);

        this.setContentView(view);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimPopupWindow);
    }

    /**
     * 显示 PopupWindow
     */
    public void show(View view, int gravity, int x, int y) {
        if (!this.isShowing()) {
            this.showAtLocation(view, gravity, x, y);
        } else {
            this.dismiss();
        }
    }
}
