package net.melove.demo.design.retorfit;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lzan13 on 2016/12/6.
 */

public class MLTTKManager {

    /**
     * 贴图库参数：
     * 相册 ID：1264034，对应相册 avatar
     * Token：e7e2a7477acd2c588622106fd81ad730e4658405:HJrxxHj1c5k3EYBcHY4Dhx-SyQ4=:eyJkZWFkbGluZSI6MTQ4MTAzNTQ4OCwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTgwNzA3IiwiYWlkIjoiMTI2NDAzNCIsImZyb20iOiJ3ZWIifQ==
     */
    public final int ALBUM_ID = 1264034;
    private final String BASE_URL = "http://up.imgapi.com/";
    public final String TOKEN =
            "e7e2a7477acd2c588622106fd81ad730e4658405:HJrxxHj1c5k3EYBcHY4Dhx-SyQ4=:eyJkZWFkbGluZSI6MTQ4MTAzNTQ4OCwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNTgwNzA3IiwiYWlkIjoiMTI2NDAzNCIsImZyb20iOiJ3ZWIifQ==";

    // 当前单例类的实例
    private static MLTTKManager instanc;
    private Retrofit mRetrofit;
    private MLTTKAPI mTTKAPI;

    private MLTTKManager() {
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTTKAPI = mRetrofit.create(MLTTKAPI.class);
    }

    /**
     * 获取当前类实例
     */
    public static MLTTKManager getInstance() {
        if (instanc == null) {
            instanc = new MLTTKManager();
        }
        return instanc;
    }

    public void uploadImage(String filePath) {
        File file = new File(filePath);

        RequestBody Token = RequestBody.create(MediaType.parse("multipart/form-data"), TOKEN);
        RequestBody deadline = RequestBody.create(MediaType.parse("multipart/form-data"),
                "" + (int) (System.currentTimeMillis() / 1000));
        RequestBody aid = RequestBody.create(MediaType.parse("multipart/form-data"), "" + ALBUM_ID);
        RequestBody from = RequestBody.create(MediaType.parse("multipart/form-data"), "file");

        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // 执行请求
        Call<ResponseBody> call = mTTKAPI.uploadImage(Token, deadline, aid, from, filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.v("Upload", "success"+response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
