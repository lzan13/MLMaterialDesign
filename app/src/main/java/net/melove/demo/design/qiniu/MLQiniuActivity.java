package net.melove.demo.design.qiniu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import java.io.File;
import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;
import net.melove.demo.design.utils.MLCryptoUtil;
import net.melove.demo.design.utils.MLFileUtil;
import net.melove.demo.design.utils.MLLog;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2016/12/7.
 * 七牛云存储相关操作测试类
 */
public class MLQiniuActivity extends MLBaseActivity {

    private final String QN_TOKEN =
            "IX84TgLqFb35Sg9q3LhwV8lTfjuVhrP9J9J90BAq:AP1XPNOOy55leDH4LyvBWVOq_CE=:eyJzY29wZSI6Im1sY2hhdCIsImRlYWRsaW5lIjoxNDgxMTIxMTQ0fQ==";

    private UploadManager mUploadManager;

    @BindView(R.id.img_upload) ImageView imageView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiniu);

        mActivity = this;
        ButterKnife.bind(this);

        Configuration config =
                new Configuration.Builder().chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                        .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                        .connectTimeout(10) // 链接超时。默认10秒
                        .responseTimeout(60) // 服务器响应超时。默认60秒
                        //.recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
                        //.recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                        .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                        .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        mUploadManager = new UploadManager(config);
    }

    /**
     * 界面控件点击事件
     *
     * @param view 当前触发点击监听的控件
     */
    @OnClick({ R.id.btn_upload_image, R.id.btn_test }) void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload_image:
                startGallery();
                break;
            case R.id.btn_test:
                testPost();
                break;
        }
    }

    /**
     * 从图库获取图片
     */
    private void startGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是图库，返回选择图片的地址
        startActivityForResult(intent, 2);
    }

    /**
     * 上传文件
     */
    private void uploadImage(String imgPath) {
        final File file = new File(imgPath);
        if (!file.exists()) {
            Toast.makeText(mActivity, "图片文件不存在", Toast.LENGTH_LONG).show();
            return;
        }
        final String key = MLCryptoUtil.cryptoStr2MD5(file.getName());

        new Thread(new Runnable() {
            @Override public void run() {
                String token = MLNetworkManager.getInstance().getUploadToken(key);
                if (TextUtils.isEmpty(token)) {
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            Toast.makeText(mActivity, "token 无效", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                mUploadManager.put(file, key, token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        // 上传完成，做相应的处理
                        if (info.isOK()) {
                            MLLog.d("上传完成，key %s, info: %s, response: %s", key, info.error,
                                    response.toString());
                            final String url = "http://oht3b8c2a.bkt.clouddn.com/" + key;
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Glide.with(mActivity).load(url).crossFade().into(imageView);
                                }
                            });
                        } else {

                        }
                    }
                }, null);
            }
        }).start();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                break;
            case 2:
                if (data != null) {
                    Uri uri = data.getData();
                    // 这里解析获取选择的图片的绝对路径
                    String imgPath = MLFileUtil.getPath(this, uri);
                    uploadImage(imgPath);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void testPost() {
        MLNetworkManager.getInstance().testPost();
    }
}
