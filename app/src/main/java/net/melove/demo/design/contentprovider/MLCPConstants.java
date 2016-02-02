package net.melove.demo.design.contentprovider;

import android.net.Uri;

/**
 * Created by lzan13 on 2016/2/2.
 */
public class MLCPConstants {

    public static final String TABLE_NAME = "user";
    public static final String COL_ID = "_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_AGE = "age";
    public static final String COL_SEX = "sex";


    // Uri 格式 content://packagename.classname（也就是注册中android:authorities的属性值）
    public static final Uri URI = Uri.parse("content://net.melove.demo.design.contentprovider");

}
