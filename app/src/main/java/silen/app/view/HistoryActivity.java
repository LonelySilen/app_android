package silen.app.view;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import silen.app.R;
import silen.app.adapter.ContainerAdapter;
import silen.app.db.ContainerDAO;
import silen.app.db.ContainerProvider;

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

public class HistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mLvCnts;
    private ContainerDAO cntDAO;

    private Cursor mCursor;
    private CursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mLvCnts.setAdapter(mAdapter);
        mLvCnts.setOnItemClickListener(this);
    }
    @Override
    protected void initData() {
        cntDAO = new ContainerDAO(this);
        //mCursor = cntDAO.rawQuery("select * from container where taskmasterid = ? ", new String[]{task_id});
        mAdapter = new ContainerAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,null);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    protected String setLabel() {
        return "箱号列表";
    }

    @Override
    protected int setViewLayout() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {

        mLvCnts = (ListView) findViewById(R.id.id_lv_containers);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor temp = (Cursor) mAdapter.getItem(position);
        final int cnt_id = temp.getInt(0);

        cntDAO.deleteContainers("_id=?", new String[]{cnt_id + ""});
        Snackbar.make(mLvCnts, "删除成功", Snackbar.LENGTH_SHORT).show();

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(ContainerProvider.CONTAINER_URI);
        return new CursorLoader(this, uri, null, null, new String[]{}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 将新的cursor换进来．(框架将在我们返回时关心一下旧cursor的关闭)
        mCursor = cntDAO.rawQuery("select * from container where  state='true' ", new String[]{});
        System.out.println("onLoadFinished");
        mAdapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //在最后一个Cursor准备进入上面的onLoadFinished()之前．
        // Cursor要被关闭了，我们需要确保不再使用它．
        mAdapter.swapCursor(null);
    }
}
