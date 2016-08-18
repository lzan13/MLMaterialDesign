package net.melove.demo.design.file;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import net.melove.demo.design.R;
import net.melove.demo.design.utils.MLFileUtil;

import java.io.File;

/**
 * 测试文件相关功能
 */
public class MLFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        findViewById(R.id.ml_btn_create_dir_1).setOnClickListener(viewListener);
        findViewById(R.id.ml_btn_create_dir_2).setOnClickListener(viewListener);
        findViewById(R.id.ml_btn_create_file).setOnClickListener(viewListener);

    }


    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dir = "";
            boolean b = false;
            switch (v.getId()) {
            case R.id.ml_btn_create_dir_1:
                dir = MLFileUtil.getSDCard() + "MLUtils";
                File dir1 = new File(dir);
                if (!dir1.exists()) {
                    b = dir1.mkdirs();
                }
                break;
            case R.id.ml_btn_create_dir_2:
                dir = MLFileUtil.getSDCard() + "mlutils";
                File dir2 = new File(dir);
                if (!dir2.exists()) {
                    b = dir2.mkdir();
                }
                break;
            case R.id.ml_btn_create_file:
                //                String testPath = MLFileUtil.getFilesFromSDCard() + "test1.json";
                //                b = MLFileUtil.createFile(testPath);
                String filepath = MLFileUtil.getSDCard() + "MLUtils/test.json";
                //                                String filepath = MLFileUtil.getSDCard() + "mlutils/test.json";
                b = MLFileUtil.createFile(filepath);
                break;
            }

            if (b) {
                Toast.makeText(MLFileActivity.this, "操作成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MLFileActivity.this, "操作失败", Toast.LENGTH_LONG).show();
            }
        }
    };
}
