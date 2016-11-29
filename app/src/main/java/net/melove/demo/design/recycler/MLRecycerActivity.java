package net.melove.demo.design.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;

/**
 * Created by lzan13 on 2016/11/24.
 */

public class MLRecycerActivity extends MLBaseActivity {

    private MLLinearLayoutManager mLayoutManager;
    private MLRecyclerAdapter mAdapter;
    private List<String> mList;

    @BindView(R.id.ml_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.ml_edit_position) EditText mEditText;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        ButterKnife.bind(this);

        initRecyclerView();
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {

        mLayoutManager = new MLLinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mList.add("item " + i);
        }

        mAdapter = new MLRecyclerAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({
            R.id.ml_btn_jump, R.id.ml_btn_up, R.id.ml_btn_down, R.id.ml_btn_add_up,
            R.id.ml_btn_add_bottom
    }) void onClick(View view) {
        switch (view.getId()) {
            case R.id.ml_btn_jump:
                mRecyclerView.smoothScrollToPosition(
                        Integer.valueOf(mEditText.getText().toString()));
                break;
            case R.id.ml_btn_up:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            case R.id.ml_btn_down:
                mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                //mRecyclerView.scrollToPosition(mList.size() - 1);
                break;
            case R.id.ml_btn_add_up:
                mList.add(0, "add up");
                mList.add(0, "add up");
                mList.add(0, "add up");
                mList.add(0, "add up");
                mList.add(0, "add up");
                mList.add(0, "add up");
                mAdapter.notifyItemRangeInserted(0, 6);
                mRecyclerView.smoothScrollToPosition(5);
                break;
            case R.id.ml_btn_add_bottom:
                mList.add(mList.size(), "add up");
                mAdapter.notifyItemInserted(mList.size());
                mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                break;
        }
    }
}
