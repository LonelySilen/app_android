package silen.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
 * 通过继承SqliteOpenHelper来创建一个数据库
 * 提供数据库连接
 */

public class DBHelper extends SQLiteOpenHelper {

    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态
    private static String DATABASENAME = "data.db3";
    private static int DATABASEVERSION = 1;
    private static final String DB_PATH = "/data/data/" +
            "silen.app/databases/data.db3";

    /**
     * (Context context, String name, CursorFactory factory,int version)
     * @param context 上下文对象
     */
    public DBHelper(Context context)
    {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    /**数据库第一次被使用时创建数据库
     * @param db 操作数据库的
     */
    public void onCreate(SQLiteDatabase db) {

        //1.创建数据库的语句
        //构造建表语句

        String taskTable = "CREATE TABLE IF NOT EXISTS task ("+
                "_id INTEGER    PRIMARY KEY AUTOINCREMENT,"+
                "state  Boolean default 'false'   ,"+
                "mendiaohao     TEXT        ,"+
                "mendiaomingcheng   TEXT    ,"+
                "fabushijian    DATETIME    ,"+
                "renwunum       TEXT        ,"+
                "faburenid      TEXT        ,"+
                "faburenxingming    TEXT    ,"+
                "fabuflag   TEXT            ,"+
                "jieshourenid,     TEXT     ,"+
                "jieshoushijian DATETIME    ,"+
                "dangqianzhuangtai  TEXT    "+
                ")";
        db.execSQL(taskTable);

        String containerTable = "CREATE TABLE IF NOT EXISTS container ("+
                "_id INTEGER    PRIMARY KEY  AUTOINCREMENT,"+
                "state  Boolean default 'false'   ,"+
                "taskmasterid   TEXT        ,"+
                "yundanid       TEXT        ,"+
                "yundanhao      TEXT        ,"+
                "fazhan     TEXT        ,"+
                "daozhan    TEXT        ,"+
                "zhuanyongxianmingcheng TEXT    ,"+
                "tuoyuanren TEXT        ,"+
                "shouhuoren TEXT        ,"+
                "xiangshu   TEXT        ,"+
                "pinming    TEXT        ,"+
                "xiangxing  TEXT        ,"+
                "xianghao   TEXT        NOT NULL,"+
                "zhongliang TEXT        NOT NULL,"+
                "riqi       DATETIME    ,"+
                "chezhong   TEXT        ,"+
                "chexing    TEXT        "+
                ")";
        db.execSQL(containerTable);

        String guestTable = "CREATE TABLE IF NOT EXISTS guest ("+
                "guest_name TEXT PRIMARY KEY UNIQUE NOT NULL,"+
                "guest_pwd  TEXT NOT NULL,"+
                "guest_state  INTEGER default 0,"+
                "guest_no  TEXT default 0"+
                ")";
        db.execSQL(guestTable);

        //2.初始化参数 ContentValues
        ContentValues cv = new ContentValues();

        cv.put("guest_name","root");
        cv.put("guest_pwd","123");
        cv.put("guest_state", "0");
        //返回id long型  如果不成功返回-1
        //1-表名
        //2-空列的默认值
        //3-字段和值的key/value集合
        Long l = db.insert("guest", null, cv);
        System.out.println("id="+l);

        //2.初始化数据

    }

    /**数据库版本发生改变时才会被调用,数据库在升级时才会被调用;
     * @param db 操作数据库
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists person");
        onCreate(db);
    }

//    /**
//     * 获取可读写的数据库
//     */
//    public SQLiteDatabase getWritableDatabase() {
//        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
//    }
//
//
//    /**
//     * 获取仅可读的数据库
//     */
//    public SQLiteDatabase getReadableDatabase() {
//        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
//    }
}

