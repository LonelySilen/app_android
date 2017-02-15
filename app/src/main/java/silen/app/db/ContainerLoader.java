package silen.app.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.widget.CursorAdapter;

import silen.app.bean.Container;

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
 * 异步加载航班数据
 */
public class ContainerLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private CursorAdapter mAdapter;

    public ContainerLoader(Context context, CursorAdapter adapter) {
        this.mAdapter = adapter;
        this.mContext = context;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(ContainerProvider.CONTAINER_URI);
        return new CursorLoader(mContext, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
