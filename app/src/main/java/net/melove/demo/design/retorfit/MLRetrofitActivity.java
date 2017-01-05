package net.melove.demo.design.retorfit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;
import net.melove.demo.design.utils.MLFileUtil;
import net.melove.demo.design.utils.MLLog;
import net.melove.demo.design.widget.MLViewGroup;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2016/12/3.
 * 使用 Retrofit 网络请求框架测试 MobAPI 接口
 */
public class MLRetrofitActivity extends MLBaseActivity {

    @BindView(R.id.custom_view_group) MLViewGroup viewGroup;
    @BindView(R.id.text_view) TextView textView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        String[] btns = { "Async Add", "Async Get", "Sync Get", "Upload Image" };
        for (int i = 0; i < btns.length; i++) {
            Button btn = new Button(mActivity);
            btn.setText(btns[i]);
            btn.setId(100 + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            switch (view.getId()) {
                case 100:
                    asyncPutData();
                    break;
                case 101:
                    asyncGetData();
                    break;
                case 102:
                    syncGetData();
                    break;
                case 103:
                    uploadImage();
                    break;
            }
        }
    };

    /**
     * 异步请求保存数据
     */
    private void asyncPutData() {
        String key = "lz0";
        String value = "{\n"
                + "    \"username\": \"lz0\",\n"
                + "    \"email\": \"lzan13@easemob.com\",\n"
                + "    \"nickname\": \"穿裤衩闯天下\",\n"
                + "    \"age\": 26,\n"
                + "    \"gender\": 1,\n"
                + "    \"location\": \"北京市海淀区中关村南大街2号院数码大厦A座\",\n"
                + "    \"signature\": \"慢慢来，一步一个脚印！\",\n"
                + "    \"create_at\": 1480747109,\n"
                + "    \"update_at\": 1480747109\n"
                + "}";
        MLMobManager.getInstance().asyncPutData(key, value, new MLNetCallback() {
            @Override public void onSuccess(JSONObject object) {
                String retCode = object.optString("retCode");
                String msg = object.optString("msg");
                if (msg.equals("success")) {
                    MLLog.d("保存数据成功：%s", object);
                } else {
                    MLLog.e("保存数据失败：%d, %s", retCode, msg);
                }
            }

            @Override public void onFailed(int code, String error) {
                MLLog.d("保存数据失败：%s", error);
            }
        });
    }

    /**
     * 异步请求获取数据
     */
    private void asyncGetData() {
        MLMobManager.getInstance().asyncGetData("lz0", new MLNetCallback() {
            @Override public void onSuccess(JSONObject object) {
                String retCode = object.optString("retCode");
                String msg = object.optString("msg");
                if (msg.equals("success")) {
                    MLLog.d("保存数据成功：%s", object);
                } else {
                    MLLog.e("保存数据失败：%d, %s", retCode, msg);
                }
            }

            @Override public void onFailed(int code, String error) {

            }
        });
    }

    /**
     * 同步请求获取数据
     */
    private void syncGetData() {
        new Thread(new Runnable() {
            @Override public void run() {
                String str = MLMobManager.getInstance().syncGetData("lz0");
                try {
                    JSONObject object = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        String filepath = "/storage/emulated/0/Pictures/Messenger/messengercode.jpg";
        MLTTKManager.getInstance().uploadImage(filepath);
    }

}
