package net.melove.demo.design.popupwindow;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;

/**
 * Created by lzan13 on 2016/12/1.
 */

public class MLPopupWindowActivity extends MLBaseActivity {

    private Button btn;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        btn = (Button) findViewById(R.id.btn_show_popup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                MLPopupWindow popupWindow =
                        new MLPopupWindow(MLPopupWindowActivity.this, R.layout.dialog_popup);
                popupWindow.show(btn, Gravity.CENTER, 0, 0);
            }
        });
    }
}
