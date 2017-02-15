package silen.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

public class ContainerDAO {
    private DBHelper mHelper;
    private static final String TABLE_NAME = "container";
    private SQLiteDatabase db;

    public ContainerDAO(Context context) {
        mHelper = new DBHelper(context);
        db = mHelper.getReadableDatabase();
    }

    //析构函数
    protected void finalize()
    {
        db.close();
        db=null;
    }
    public int insertContainers(ContentValues values) {
        int result = -1;
        //SQLiteDatabase db = mHelper.getWritableDatabase();
        //
        db.beginTransaction();
        try
        {
            result = (int) db.insert(TABLE_NAME, null, values);
            //
            db.setTransactionSuccessful();
        }
        finally
        {
            //
            db.endTransaction();
        }
        return result;
    }


    public Cursor queryContainers(String selection, String[] selectionArgs) {
        //SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor result = db.query(false, TABLE_NAME, null, selection, selectionArgs,
                null, null, null, null);
        return result;
    }

    public int updateContainers(ContentValues values, String whereClause, String[] whereArgs) {
        int result = -1;
        //SQLiteDatabase db = mHelper.getWritableDatabase();
        //
        db.beginTransaction();
        try
        {
            result = db.update(TABLE_NAME, values, whereClause, whereArgs);
            //
            db.setTransactionSuccessful();
        }
        finally
        {
            //
            db.endTransaction();
        }
        return result;
    }

    public int deleteContainers(String whereClause, String[] whereArgs) {
        //SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = db.delete(TABLE_NAME, whereClause, whereArgs);
        return result;
    }

    public void deleteAll() {
        //SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "delete from "+TABLE_NAME+" where 1=1";
        db.execSQL(sql);

    }

    public Cursor rawQuery(String sql, String[] args) {

        Cursor result = db.rawQuery(sql, args);
        return result;
    }

}
