package net.melove.demo.design.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lzan13 on 2016/2/2.
 */
public class MLCPDBHelper extends SQLiteOpenHelper {

    /**
     * 构造方法
     *
     * @param context 当前上下文对象
     */
    public MLCPDBHelper(Context context) {
        super(context, MLCPConstants.TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MLCPConstants.TABLE_NAME + "("
                + MLCPConstants.COL_ID + " integer primary key autoincrement,"
                + MLCPConstants.COL_USERNAME + " varchar(64),"
                + MLCPConstants.COL_AGE + " integer,"
                + MLCPConstants.COL_SEX + " integer"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
