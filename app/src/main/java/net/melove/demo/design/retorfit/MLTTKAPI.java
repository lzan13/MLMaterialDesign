package net.melove.demo.design.retorfit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lzan13 on 2016/12/6.
 */

public interface MLTTKAPI {

    /**
     * 调用 retrofit post 请求实现上传图片
     *
     * @param file 将要上传的文件
     */
    @Multipart @POST("/") Call<ResponseBody> uploadImage(
            @Part("Token") RequestBody token,
            @Part("deadline") RequestBody deadline,
            @Part("aid") RequestBody aid,
            @Part("from") RequestBody from,
            @Part MultipartBody.Part file);
}
