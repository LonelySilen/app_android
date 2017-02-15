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
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import silen.app.R;
import silen.app.adapter.ContainerAdapter;
import silen.app.adapter.TaskAdapter;
import silen.app.bean.Container;
import silen.app.bean.Task;
import silen.app.db.ContainerDAO;
import silen.app.db.ContainerProvider;
import silen.app.db.TaskDAO;
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

public class ContainerActivity extends BaseActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mLvCnts;
    private ContainerDAO cntDAO;
    private TaskDAO mTaskDAO;
    private String task_id;


    // 对话框UI
    private View mDialogview;
    private EditText cnsClass;
    private EditText cnsType;
    private Button mBtnAddSeat;


    private Button btnFinish;
    private Cursor mCursor;
    private CursorAdapter mAdapter;

    private int _id;
    private android.app.AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("adapter.refresh");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    @Override
    protected void initEvent() {
        mLvCnts.setAdapter(mAdapter);
        mLvCnts.setOnItemClickListener(this);
    }
    @Override
    protected void initData() {
        cntDAO = new ContainerDAO(this);
        mTaskDAO = new TaskDAO(this);
        task_id = getIntent().getStringExtra("task_id");
        //mCursor = cntDAO.rawQuery("select * from container where taskmasterid = ? ", new String[]{task_id});// and state='false'
        //System.out.println("###################mCursor count:"+mCursor.getCount());
        String argvs[] = {
            getIntent().getStringExtra("uid"),
            getIntent().getStringExtra("mid")
        };
        mAdapter = new ContainerAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, argvs);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    protected String setLabel() {
        return "箱号列表";
    }

    @Override
    protected int setViewLayout() {
        return R.layout.activity_container;
    }

    @Override
    protected void initView() {

        mLvCnts = (ListView) findViewById(R.id.id_lv_containers);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 对订单进行付款
        // 根据位置返回特定的Cursor对象
        Cursor cursor = (Cursor) mAdapter.getItem(position);
        _id = cursor.getInt(0);
        // 弹出对话框，添加机票
        // 创建一个对话框

        LayoutInflater inflater = LayoutInflater.from(ContainerActivity.this);
        mDialogview = inflater.inflate(R.layout.dialog_add_container, null, false);
        mBtnAddSeat = (Button) mDialogview.findViewById(R.id.id_btn_add_ticket);

        cnsClass = (EditText) mDialogview.findViewById(R.id.id_tv_cnts_class);
        cnsType = (EditText) mDialogview.findViewById(R.id.id_tv_cnts_type);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ContainerActivity.this);
        builder.setView(mDialogview);
        alertDialog = builder.create();
        alertDialog.show();
        mBtnAddSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("begin dialog");
                new Thread(runnable).start();
            }
        });

//        btnFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
//                String mendiaohao = getIntent().getStringExtra("mid");
//                Date currentTime = new Date();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//                String dateString = formatter.format(currentTime);
//
//                ArrayList<String> command = new ArrayList();
//                command.add(""+_id);
//                command.add(mendiaohao);
//                command.add(getIntent().getStringExtra("uid"));
//                command.add(dateString);
//                String cur_url = FormatArry.formatArryToUrl("1006",command);
//                System.out.println(cur_url);
//                String cur_result = HttpUtils.doGet(cur_url);
//
//                if(cur_result!=null) {
//                    System.out.println("返回结果："+cur_result);
//                }
//                getLoaderManager().restartLoader(0, null, ContainerActivity.this);
//
//            }
//        });

    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if(val.equals("false")) {

                Toast.makeText(ContainerActivity.this, "联网失败！", Toast.LENGTH_SHORT).show();
            }else{
                ContentValues values = new ContentValues();
                values.put("chezhong", cnsClass.getText().toString());
                values.put("chexing", cnsType.getText().toString());
                //values.put("state", "true");
                cntDAO.updateContainers(values, "_id=?", new String[]{_id + ""});
                Snackbar.make(mLvCnts, "处理成功", Snackbar.LENGTH_SHORT).show();

                //SystemClock.sleep(1500);
                //refresh();
                getLoaderManager().restartLoader(0, null, ContainerActivity.this);
                alertDialog.dismiss();
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
                cmd.add(""+_id);
                cmd.add(getIntent().getStringExtra("uid"));
                cmd.add(cnsClass.getText().toString());
                cmd.add(cnsType.getText().toString());
                String url = FormatArry.formatArryToUrl("1005",cmd);
                System.out.println(url);
                String result = HttpUtils.doGet(url);

                try {
                    if(result!=null) {
                        System.out.println(result);
                        JSONObject jsonObject = new JSONObject(result);
                        is_crt = (boolean) jsonObject.get("IsSuccess");
                        if(is_crt) {

//                            String mendiaohao = getIntent().getStringExtra("mid");
//                            Date currentTime = new Date();
//                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//                            String dateString = formatter.format(currentTime);
//
//                            ArrayList<String> command = new ArrayList();
//                            command.add(""+_id);
//                            command.add(mendiaohao);
//                            command.add(getIntent().getStringExtra("uid"));
//                            command.add(dateString);
//                            String cur_url = FormatArry.formatArryToUrl("1006",command);
//                            System.out.println(cur_url);
//                            String cur_result = HttpUtils.doGet(cur_url);
//                            Snackbar.make(mLvCnts, cur_result, Snackbar.LENGTH_SHORT).show();

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
                msg.setData(data);
                handler.sendMessage(msg);

            }else{

            }


        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(ContainerProvider.CONTAINER_URI);
        System.out.println("onCreateLoader");
        return new CursorLoader(this, uri, null, "taskmasterid = ? ", new String[]{task_id}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 将新的cursor换进来．(框架将在我们返回时关心一下旧cursor的关闭)
        //mCursor = cntDAO.rawQuery("select * from container where taskmasterid = ? ", new String[]{task_id});// and state='false'
        System.out.println("onLoadFinished");
        //mAdapter.notifyDataSetChanged();
        mAdapter.swapCursor(data);
//        if(mCursor.getCount()<1){
//            new Thread(new Runnable() {
//                public void run() {
//                    boolean is_crt = false;
//                    ArrayList<String> cmd = new ArrayList();
//                    cmd.add(task_id);
//                    cmd.add(getIntent().getStringExtra("uid"));
//                    String url = FormatArry.formatArryToUrl("1004",cmd);
//                    System.out.println(url);
//                    //SystemClock.sleep(1000);
//                    String result = HttpUtils.doGet(url);
//
//                    try {
//                        if(result!=null) {
//                            System.out.println(result);
//                            JSONObject jsonObject = new JSONObject(result);
//                            is_crt = (boolean) jsonObject.get("IsSuccess");
//                            if(is_crt) {
//                                if (mTaskDAO.deleteTask( "_id=?", new String[]{task_id + ""}) > 0) {
//
//                                    Snackbar.make(mLvCnts, "任务已经完成", Snackbar.LENGTH_SHORT).show();
//                                    SystemClock.sleep(3000);
//
//                                    // 广播通知
//                                    Intent intent = new Intent();
//                                    intent.setAction("action.refresh");
//                                    sendBroadcast(intent);
//
//                                    ContainerActivity.this.finish();
//
//                                }
//                            }else{
//                                System.out.println("结束任务失败！");
//                            }
//                        }else{
//                            System.out.println("结束任务失败！");
//                        }
//                    }catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //在最后一个Cursor准备进入上面的onLoadFinished()之前．
        // Cursor要被关闭了，我们需要确保不再使用它．
        System.out.println("onLoaderReset");
        mAdapter.swapCursor(null);
    }

    public void stopTask(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认结束任务");
        builder.setMessage("请确保所有操作已完成");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    public void run() {
                        boolean is_crt = false;
                        ArrayList<String> cmd = new ArrayList();
                        cmd.add(task_id);
                        cmd.add(getIntent().getStringExtra("uid"));
                        String url = FormatArry.formatArryToUrl("1004",cmd);
                        System.out.println(url);
                        //SystemClock.sleep(1000);
                        String result = HttpUtils.doGet(url);

                        try {
                            if(result!=null) {
                                System.out.println(result);
                                JSONObject jsonObject = new JSONObject(result);
                                is_crt = (boolean) jsonObject.get("IsSuccess");
                                if(is_crt) {
                                    if (mTaskDAO.deleteTask( "_id=?", new String[]{task_id + ""}) > 0) {

                                        Snackbar.make(mLvCnts, "任务已经完成", Snackbar.LENGTH_SHORT).show();

                                        // 广播通知
                                        Intent intent = new Intent();
                                        intent.setAction("action.refresh");
                                        sendBroadcast(intent);

                                        SystemClock.sleep(1000);
                                        finish();

                                    }
                                }else{
                                    System.out.println("结束任务失败！");
                                }
                            }else{
                                System.out.println("结束任务失败！");
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        builder.setNegativeButton("取消", null);
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * 刷新
     */
    private void refresh() {

        Intent intent = new Intent(ContainerActivity.this, ContainerActivity.class);
        intent.putExtra("task_id", getIntent().getStringExtra("task_id"));
        String userName = getIntent().getStringExtra("userName");//SignupActivity.SEND_USER_NAME;
        intent.putExtra("userName", userName);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));
        intent.putExtra("mid", getIntent().getStringExtra("mid"));
        startActivity(intent);
        this.finish();
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("adapter.refresh"))
            {
                System.out.println("adapter.refresh");

                //mAdapter.notifyDataSetChanged();
                getLoaderManager().restartLoader(0, null, ContainerActivity.this);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
