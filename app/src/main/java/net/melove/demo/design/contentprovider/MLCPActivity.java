package net.melove.demo.design.contentprovider;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;

/**
 * Created by lzan13 on 2016/2/2.
 */
public class MLCPActivity extends MLBaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Button mAddBtn;
    private ListView mListView;

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentprovider);

        mActivity = this;

        initView();
        initLoader();

    }

    private void initView() {
        findViewById(R.id.ml_btn_add).setOnClickListener(viewListener);
        findViewById(R.id.ml_btn_delete).setOnClickListener(viewListener);
        findViewById(R.id.ml_btn_update).setOnClickListener(viewListener);

        mListView = (ListView) findViewById(R.id.ml_listview);

        String[] from = {MLCPConstants.COL_USERNAME, MLCPConstants.COL_AGE, MLCPConstants.COL_SEX};
        int[] to = {R.id.ml_text_username, R.id.ml_text_age, R.id.ml_text_sex};
        Cursor cursor = getContentResolver().query(MLCPConstants.URI_BASE, null, null, null, null);
        mAdapter = new SimpleCursorAdapter(this, R.layout.item_contentprovider,
                cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(mAdapter);
//        mListView.setEmptyView();
    }


    private void initLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ml_btn_add:
                    addData();
                    break;
                case R.id.ml_btn_delete:
                    String selection = MLCPConstants.COL_USERNAME + "=?";
                    getContentResolver().delete(MLCPConstants.URI_BASE, selection, new String[]{"lz3"});
                    break;
                case R.id.ml_btn_update:

                    break;
            }
        }
    };

    private void addData() {
        ContentValues values = new ContentValues();
        values.put(MLCPConstants.COL_USERNAME, "lz3");
        values.put(MLCPConstants.COL_AGE, 24);
        values.put(MLCPConstants.COL_SEX, 1);
        getContentResolver().insert(MLCPConstants.URI_BASE, values);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mActivity, MLCPConstants.URI_BASE, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
