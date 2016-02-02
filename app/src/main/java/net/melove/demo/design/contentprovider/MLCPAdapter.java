package net.melove.demo.design.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.melove.demo.design.R;
import net.melove.demo.design.util.MLLog;

/**
 * Created by lzan13 on 2016/2/2.
 */
public class MLCPAdapter extends CursorAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayout;

    public MLCPAdapter(Context context, Cursor c, int layout) {
        super(context, c, true);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mLayout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        MLLog.i("newView");
        View view = mInflater.inflate(mLayout, null);
        ViewHolder holder = new ViewHolder();
        holder.usernameView = (TextView) view.findViewById(R.id.ml_text_username);
        holder.ageView = (TextView) view.findViewById(R.id.ml_text_age);
        holder.sexView = (TextView) view.findViewById(R.id.ml_text_sex);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MLLog.i("newView");

    }

    static class ViewHolder {
        public TextView usernameView;
        public TextView ageView;
        public TextView sexView;

    }
}
