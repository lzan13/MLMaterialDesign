package net.melove.demo.design.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import net.melove.demo.design.R;
import net.melove.demo.design.activity.MLBaseActivity;

/**
 * Created by lzan13 on 2016/2/2.
 */
public class MLCPActivity extends MLBaseActivity {

    private Button mAddBtn;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentprovider);

        initView();

    }

    private void initView() {
        mAddBtn = (Button) findViewById(R.id.ml_btn_add);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        mListView = (ListView) findViewById(R.id.ml_listview);

        String[] from = {MLCPConstants.COL_USERNAME, MLCPConstants.COL_AGE, MLCPConstants.COL_SEX};
        int[] to = {R.id.ml_text_username, R.id.ml_text_age, R.id.ml_text_sex};
        Cursor cursor = getContentResolver().query(MLCPConstants.URI, null, null, null, null);
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.item_contentprovider,
                cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(mAdapter);
    }

    private void addData() {
        ContentValues values = new ContentValues();
        values.put(MLCPConstants.COL_USERNAME, "lz2");
        values.put(MLCPConstants.COL_AGE, 23);
        values.put(MLCPConstants.COL_SEX, 1);
        getContentResolver().insert(MLCPConstants.URI, values);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
