package silen.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import silen.app.db.DBHelper;

/*
 *
 *
 * 版 权 :@Copyright *****有限公司版权所有
 *
 * 作 者 :lonelysilen
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/11/10  10:21
 *
 * 描 述 :*****处理类
 *
 * 修订日期 :
 */

/**
 * Created by admin on 2016/10/24.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();
        initDB();
    }


    /**
     * 初始化数据库,从Assets资源中获取
     */
    private void initDB() {
        DBHelper dbHelper = new DBHelper(context);
        // 第一次调用该方法会调用数据库
        dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    /**
     * 初始化数据库,从Assets资源中获取
     */
    private void createDB() {
        String DB_DIR_PATH = "/data/data/" + this.getPackageName() + "/databases";
        // 初始化数据库
        if (!new File(DB_DIR_PATH + "/data.db3").exists()) {
            try {
                new File(DB_DIR_PATH).mkdir();
                FileOutputStream fos = new FileOutputStream(DB_DIR_PATH + "/data.db3");
                InputStream is = getAssets().open("data.db3");
                byte[] buf = new byte[1024];
                int len;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                Log.v("LOG", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
