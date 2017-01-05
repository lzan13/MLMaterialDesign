package net.melove.demo.design.qiniu;

import android.text.TextUtils;
import java.io.IOException;
import net.melove.demo.design.utils.MLLog;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lzan13 on 2016/12/9.
 * 自定义实现项目网络请求管理器
 */
public class MLNetworkManager {

    // 请求服务器地址
    private final String ML_API_URL = "http://42.96.192.98:5121/api/v1/";

    // 当前单例类的实例
    private static MLNetworkManager instanc;
    private MLNetworkAPI mNetworkAPI;

    private Retrofit mRetrofit;

    /**
     * 私有构造方法，在里边做一些全局的对象初始化操作
     */
    private MLNetworkManager() {
        // 获取 Retrofit 对象
        mRetrofit = new Retrofit.Builder().baseUrl(ML_API_URL).build();
        // 使用 Retrofit 创建自定义网络请求接口的实例
        mNetworkAPI = mRetrofit.create(MLNetworkAPI.class);
    }

    /**
     * 获取当前类实例
     */
    public static MLNetworkManager getInstance() {
        if (instanc == null) {
            instanc = new MLNetworkManager();
        }
        return instanc;
    }

    /**
     * 获取向七牛上传图片 token
     *
     * @param key 上传图片时的 key，根据上传图片文件名称 md5 获取
     */
    public String getUploadToken(String key) {
        // 请求服务器获取
        Call<ResponseBody> call = mNetworkAPI.getUploadToken(key);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                JSONObject object = new JSONObject(response.body().string());
                String token = object.optJSONObject("data").optString("token");
                int deadline = object.optJSONObject("data").optInt("deadline");
                return token;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void testPost() {
        try {
            JSONObject object = new JSONObject();
            object.put("key1", "this test post param 1");
            object.put("key2", "param 2");
            String[] usernames = { "lz0", "lz1", "lz2" };
            Call<ResponseBody> call = mNetworkAPI.testPost("value1", "value2", usernames);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            MLLog.d("请求结果：%s", response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
                    MLLog.d("请求结果：%s", t.getMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
