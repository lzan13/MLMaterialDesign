package net.melove.demo.design.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by lzan13 on 2016/2/2.
 */
public class MLContentProvider extends ContentProvider {

    // 继承自 SqliteOpenHelper 的数据库帮助类
    private MLCPDBHelper mCPDBHelper;
    // 数据库对象，
    private SQLiteDatabase mSqliteDatabase;

    /**
     * 主要用来初始化使用到的工具，在ContentProvider对象创建之后就会被调用，
     * 比如继承自 SQLiteOpenHelper 类{@link MLCPDBHelper}的对象的初始化，
     * SQLiteDatabase中获得可读可写权限对象的初始化等
     *
     * @return 返回初始化的结果
     */
    @Override
    public boolean onCreate() {
        // 初始化数据库帮助类
        mCPDBHelper = new MLCPDBHelper(getContext());

        /**
         * 获取 SqliteDatabase 可读写权限的对象，此方法会先调用 getWritableDatabase
         *
         * getWritableDatabase取得的实例不是仅仅具有写的功能，而是同时具有读和写的功能，
         * 同样的，getReadableDatabase取得的实例也是具对数据库进行读和写的功能，
         * 两者的区别在于
         * getWritableDatabase 取得的实例是以读写的方式打开数据库，如果打开的数据库磁盘满了，此时只能读不能写，
         * 此时去调用 getWritableDatabase 那么将会发生错误（异常）
         * getReadableDatabase 取得的实例是先调用getWritableDatabase以读写的方式打开数据库，
         * 如果数据库的磁盘满了，此时返回打开失败，继而用 getReadableDatabase 的实例以只读的方式去打开数据库
         */
        mSqliteDatabase = mCPDBHelper.getReadableDatabase();
        // 操作完成 return true
        return true;
    }

    /**
     * 返回类型为Cursor，通过对数据库具有可读权限对象的query方法，完成外部应用数据库关于表的查询操作，
     * 可以让外部应用的从 ContentProvider 中获取数据
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return 返回查询结果的指针
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        return mSqliteDatabase.query(MLCPConstants.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    /**
     * 用来标识ContentProvider中数据的类型
     *
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * 返回类型为Uri，通过对数据库具有可写权限对象的 insert(插入) 方法，完成数据库关于表的插入操作，
     * 可以让外部应用向ContentProvider中插入数据
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mSqliteDatabase.insert(MLCPConstants.TABLE_NAME, null, values);
        return uri;
    }

    /**
     * 返回类型为int，通过对数据库具有可写权限对象的 delete 方法，完成数据库中关于表的删除操作，
     * 可以让外部应用从 ContentProvider 中删除数据
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mSqliteDatabase.delete(MLCPConstants.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * 返回类型为int，通过对数据库具有可写权限对象的update方法，完成数据库中关于表的更新操作，
     * 可以让外部应用更新 ContentProvider 中的数据，
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mSqliteDatabase.update(MLCPConstants.TABLE_NAME, values, selection, selectionArgs);
    }
}
