package silen.app.view;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import silen.app.R;
import silen.app.adapter.TaskAdapter;
import silen.app.bean.Task;
import silen.app.db.ContainerDAO;
import silen.app.db.TaskDAO;
import silen.app.db.TaskProvider;
import silen.app.utils.FormatArry;
import silen.app.utils.HttpUtils;

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

public class TaskActivity extends BaseActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mLvTasks;
    private CursorAdapter mAdapter;
    private Cursor mCursor;
    private TaskDAO mTaskDAO;
    private ContainerDAO cntDao;

    private int task_id;
    private String mid;
    private View itemView;

    private Button btnClr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refresh");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        btnClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDb(v);
            }

        });
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar_base);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearDb(v);
//            }
//        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    protected void initEvent() {
        mLvTasks.setAdapter(mAdapter);
        mLvTasks.setOnItemClickListener(this);
    }

    /**
     * 切换到指定的Activity
     */
    private void switchToActivity(String id) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra("task_id", id);
        String userName = getIntent().getStringExtra("userName");//SignupActivity.SEND_USER_NAME;
        intent.putExtra("userName", userName);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    @Override
    protected String setLabel() {
        return "任务信息："+ getIntent().getStringExtra("mname"); //"一号门吊";
    }

    @Override
    protected int setViewLayout() {
        return R.layout.activity_task;
    }

    @Override
    protected void initData() {

        mTaskDAO = new TaskDAO(this);
        cntDao = new ContainerDAO(this);
        //mCursor = mTaskDAO.rawQuery("select * from task where mendiaohao= ?",new String[]{No});
        mAdapter = new TaskAdapter(this, null
                , CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void initView() {
        mLvTasks = (ListView) findViewById(R.id.id_lv_tasks_info);
        btnClr= (Button) findViewById(R.id.btnClr);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 更新信息
        Cursor temp = (Cursor) mAdapter.getItem(position);
        task_id = temp.getInt(0);
        mid = temp.getString(2);
        boolean isPaying = Boolean.parseBoolean(temp.getString(temp.getColumnIndex("state")));
        // 需正确获取每个Item
        itemView = mLvTasks.getChildAt((position - mLvTasks.getFirstVisiblePosition()));

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
        builder.setTitle("确认接受任务");
        builder.setMessage("点击确定即可接受任务");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(runnable).start();

            }
        });

        builder.setNegativeButton("取消", null);
        final AlertDialog dialog = builder.create();
        if (!isPaying) {

            dialog.show();
        }else{
            switchToActivity(temp.getString(0));
        }

    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if(val.equals("false")) {

                Toast.makeText(TaskActivity.this, "联网失败！", Toast.LENGTH_SHORT).show();
                Toast.makeText(TaskActivity.this, data.getString("info"), Toast.LENGTH_SHORT).show();
            }else{
                final TaskAdapter.ViewHolder holder = (TaskAdapter.ViewHolder) itemView.getTag();
                ContentValues values = new ContentValues();
                values.put("state", "true");
                if (mTaskDAO.updateTask(values, "_id=?", new String[]{task_id + ""}) > 0) {
                    // 付款成功后添加图标
                    holder.mIvPaying.setImageResource(R.drawable.item_is_paying);

                    Snackbar.make(mLvTasks, "接受任务成功", Snackbar.LENGTH_SHORT).show();
                }

                getLoaderManager().restartLoader(0, null, TaskActivity.this);
                switchToActivity(Integer.toString(task_id));
            }
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            // Store values at the time of the login attempt.

            ConnectivityManager con=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();
            boolean internet = false;
            if (mNetworkInfo != null) {
                internet = mNetworkInfo.isAvailable();
            }

            if(internet){
                //执行相关操作
                //
                // TODO: http request.
                //
                boolean is_crt = false;
                ArrayList<String> cmd = new ArrayList();
                cmd.add(""+task_id);
                cmd.add(getIntent().getStringExtra("uid"));
                String url = FormatArry.formatArryToUrl("1003",cmd);
                System.out.println("TaskAct:"+url);
                String result = HttpUtils.doGet(url);

                try {
                    if(result!=null) {
                        System.out.println(result);
                        JSONObject jsonObject = new JSONObject(result);
                        is_crt = (boolean) jsonObject.get("IsSuccess");
                        if(is_crt) {

                        }else{
                            System.out.println("失败！");
                        }
                    }else{
                        System.out.println("失败！");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", String.valueOf(is_crt));
                data.putString("info", result);
                msg.setData(data);
                handler.sendMessage(msg);

            }else{

                Snackbar.make(mLvTasks, "网络连接异常", Snackbar.LENGTH_SHORT).show();
            }


        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(TaskProvider.TASK_URI);
        String No = getIntent().getStringExtra("no");
        return new CursorLoader(this, uri, null, "mendiaohao= ?",new String[]{No}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 将新的cursor换进来．(框架将在我们返回时关心一下旧cursor的关闭)
        //mCursor = mTaskDAO.rawQuery("select * from task", null);
        System.out.println("onLoadFinished!");
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //在最后一个Cursor准备进入上面的onLoadFinished()之前．
        // Cursor要被关闭了，我们需要确保不再使用它．
        mAdapter.swapCursor(null);
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refresh"))
            {
                //System.out.println("BroadcastReceiver !!!!!!!!!!!!!!! Refresh");
                //refresh();
                mAdapter.notifyDataSetChanged();
                getLoaderManager().restartLoader(0, null, TaskActivity.this);
            }else if(action.equals("action.restart")){
                refresh();
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    private int back_time = 0;
    private static final int INTERVAL = 500;   //响应间隔时间
    private long lastReturnTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            long curTime = System.currentTimeMillis();
            back_time++;
            if(back_time<2) {
                lastReturnTime = curTime;
                Snackbar.make(mLvTasks, "连续点击两次返回退出程序！", Snackbar.LENGTH_SHORT).show();
            }else if(back_time>1 && curTime - lastReturnTime < INTERVAL){

                return super.onKeyDown(keyCode, event);
            }else if(back_time>1 && curTime - lastReturnTime >= INTERVAL){
                back_time = 0;
                Snackbar.make(mLvTasks, "连续点击两次返回退出程序！", Snackbar.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    /**
     * 刷新
     */
    private void refresh() {
//        mAdapter.notifyDataSetChanged();
//        getLoaderManager().restartLoader(0, null, TaskActivity.this);
        System.out.println("Refresh");
        Intent intent = new Intent(TaskActivity.this, TaskActivity.class);
        String userName = getIntent().getStringExtra("userName");//SignupActivity.SEND_USER_NAME;
        intent.putExtra("userName", userName);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));

        intent.putExtra("no", getIntent().getStringExtra("no"));
        intent.putExtra("realname", getIntent().getStringExtra("realname"));
        intent.putExtra("mname", getIntent().getStringExtra("mname"));
        startActivity(intent);
        this.finish();
    }

    /**
     * 清空数据
     */
    private void clearDb(View v){


        mTaskDAO.deleteAll();
        cntDao.deleteAll();
        Toast.makeText(TaskActivity.this, "已清空数据！", Toast.LENGTH_SHORT).show();
        getLoaderManager().restartLoader(0, null, TaskActivity.this);
    }
}
