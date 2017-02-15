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

public class GuestDAO {
    private DBHelper mHelper;
    private static final String TABLE_NAME = "guest";

    public GuestDAO(Context context) {
        mHelper = new DBHelper(context);
    }

    public int insertGuest(ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int row = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        return row;
    }

    public Cursor queryGuest(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db.query(false, TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
    }

    public int updateGuest(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public int deleteGuest(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }
}
