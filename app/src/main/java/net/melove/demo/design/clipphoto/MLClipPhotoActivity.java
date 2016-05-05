package net.melove.demo.design.clipphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.melove.demo.design.R;
import net.melove.demo.design.utils.MLFile;
import net.melove.demo.design.utils.MLMedia;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 剪切图片，从图库选择或者相机拍摄图片后修剪图片并展示
 */
public class MLClipPhotoActivity extends AppCompatActivity {
    private ImageView mImageViewClip;
    private ImageView mImageViewSrc;
    private File mTempFile;
    private Uri mTempUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_photo);

        initView();
    }

    private void initView() {
        findViewById(R.id.ml_btn_camera_clip_photo).setOnClickListener(viewListener);
        findViewById(R.id.ml_btn_gallery_clip_photo).setOnClickListener(viewListener);
        mImageViewClip = (ImageView) findViewById(R.id.ml_img_clip_photo);
        mImageViewSrc = (ImageView) findViewById(R.id.ml_img_src_photo);
    }

    /**
     * 控件点击监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.ml_btn_camera_clip_photo:
                startCamera();
                break;
            case R.id.ml_btn_gallery_clip_photo:
                startGallery();
                break;
            }
        }
    };

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
     * 从相机获取图片
     */
    private void startCamera() {
        String tempPath = Environment.getExternalStorageDirectory().getPath() + "/";
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (MLFile.hasSdcard()) {
            // 这里先设置好等下拍照保存的文件路径
            mTempFile = new File(tempPath, "CameraTemp.jpg");
            // 根据文件路径解析成Uri
            mTempUri = Uri.fromFile(mTempFile);
            // 将Uri设置为媒体输出的目标，目的就是为了等下拍照保存在自己设定的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
        }
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是相机，返回选择图片的地址
        startActivityForResult(intent, 1);
    }

    /**
     * 剪切图片，根据传入的宽高决定图片大小，使用Uri 不返回数据
     *
     * @param uri
     * @param width
     * @param height
     */
    private void clipImage(Uri uri, int width, int height) {
        String tempPath = Environment.getExternalStorageDirectory().getPath() + "/temp_clip.jpg";
        mTempUri = Uri.fromFile(new File(tempPath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 设置剪切框大小为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置输出图片大小
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);

        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 3);
    }

    /**
     * 通过Uri获取Bitmap
     *
     * @param uri
     * @return
     */
    private Bitmap decodeUriToBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            // 根据uri获取Bitmap，因为此Uri包含的是图片资源的二进制流数据
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case 1:
            if (MLFile.hasSdcard()) {
                Uri uri = Uri.fromFile(mTempFile);
                clipImage(uri, 512, 512);

                // 获取原图路径，并获取Bitmap 设置给ImageView
                String imgPath = uri.getPath();
                Bitmap cameraBitmap = BitmapFactory.decodeFile(imgPath);
                mImageViewSrc.setImageBitmap(cameraBitmap);

            }
            break;
        case 2:
            if (data != null) {
                Uri uri = data.getData();
                // 这里解析获取选择的图片的绝对路径
                String imgPath = MLMedia.getPath(this, uri);
                Uri uriAbsolute = Uri.fromFile(new File(imgPath));
                clipImage(uriAbsolute, 512, 512);

                // 根据原图路径获取Bitmap，并设置给ImageView
                Bitmap srcBitmap = BitmapFactory.decodeFile(imgPath);
                mImageViewSrc.setImageBitmap(srcBitmap);
            }
            break;
        case 3:
            try {
                // 从临时保存的Uri中获取Bitmap，这个值是在剪切文件，以及照相时获取到的
                Bitmap clipBitmap = decodeUriToBitmap(mTempUri);
                mImageViewClip.setImageBitmap(clipBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
