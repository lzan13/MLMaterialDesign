package net.melove.demo.design.test;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.melove.demo.design.R;
import net.melove.demo.design.contentprovider.MLCPActivity;
import net.melove.demo.design.util.MLLog;
import net.melove.demo.design.widget.MLViewGroup;

/**
 * 测试Fragment，
 * 继承自自定义的MLBaseFramgnet类，为了减少代码量，在MLBaseFrament类中定义接口回调
 * 包含此Fragment的活动窗口必须实现{@link MLBaseFragment.OnMLFragmentListener}接口,
 * 定义创建实例的工厂方法 {@link MLTestFragment#newInstance}，可使用此方法创建实例
 */
public class MLTestFragment extends MLBaseFragment {


    private OnMLFragmentListener mListener;
    private MLViewGroup viewGroup;

    /**
     * 使用这个工厂方法创建一个新的实例
     *
     * @return 一个新的Fragment MLTestFragment.
     */
    public static MLTestFragment newInstance() {
        MLTestFragment fragment = new MLTestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MLTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        init();
    }

    private void init() {
        viewGroup = (MLViewGroup) getView().findViewById(R.id.ml_view_custom_viewgroup);

        getView().findViewById(R.id.ml_btn_test_jump).setOnClickListener(viewListener);
        getView().findViewById(R.id.ml_btn_test_toast).setOnClickListener(viewListener);
        getView().findViewById(R.id.ml_btn_test_record).setOnClickListener(viewListener);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMLFragmentListener) context;
        } catch (ClassCastException e) {
            MLLog.e("必须实现Fragment的回调接口！");
            e.printStackTrace();
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ml_btn_test_jump:
                    mActivity.startActivity(new Intent(mActivity, MLCPActivity.class));
                    break;
                case R.id.ml_btn_test_toast:
//                    MLToast.makeToast(1, "Test Toast").show();
                    break;
                case R.id.ml_btn_test_record:

                    break;
            }
        }
    };
}
