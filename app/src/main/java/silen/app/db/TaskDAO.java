package silen.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

public class TaskDAO {
    private DBHelper mHelper;
    private static final String TABLE_NAME = "task";
    private SQLiteDatabase db;

    public TaskDAO(Context context) {
        mHelper = new DBHelper(context);
        db = mHelper.getReadableDatabase();
    }

    //析构函数
    protected void finalize()
    {
        db.close();
    }
    public int insertTask(ContentValues values) {
        int result = -1;
        //SQLiteDatabase db = mHelper.getWritableDatabase();
        //开启事务
        db.beginTransaction();
        try
        {
            result = (int) db.insert(TABLE_NAME, null, values);
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        }
        finally
        {
            //结束事务
            db.endTransaction();
        }
        return result;
    }


    public Cursor queryTask(String selection, String[] selectionArgs) {
        //SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor result = db.query(false, TABLE_NAME, null, selection, selectionArgs,
                null, null, null, null);
        return result;
    }

    public int updateTask(ContentValues values, String whereClause, String[] whereArgs) {
        int result = -1;
        //SQLiteDatabase db = mHelper.getWritableDatabase();
        //开启事务
        db.beginTransaction();
        try
        {
            result = db.update(TABLE_NAME, values, whereClause, whereArgs);
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        }
        finally
        {
            //结束事务
            db.endTransaction();
        }
        return result;
    }

    public int deleteTask(String whereClause, String[] whereArgs) {
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
