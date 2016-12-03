package net.melove.demo.design.fragment;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import net.melove.demo.design.R;

/**
 * Created by lzan13 on 2016/11/30.
 */

public class MLSecondFragment extends MLBaseFragment {

    @BindView(R.id.text_view) TextView mTextView;

    private String mStrData = "";

    /**
     * 初始化 Fragment 界面 layout_id
     *
     * @return 返回布局 id
     */
    @Override protected int initLayoutId() {
        return R.layout.fragment_second;
    }

    /**
     * 初始化界面控件，将 Fragment 变量和 View 建立起映射关系
     */
    @Override protected void initView() {
        ButterKnife.bind(this, getView());
    }

    /**
     * 加载数据
     */
    @Override protected void initData() {
        mStrData = "Second data";
        mTextView.setText(mStrData);
    }

    private void refresh() {
    }
}
