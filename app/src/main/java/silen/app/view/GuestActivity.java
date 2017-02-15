package silen.app.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import silen.app.R;
import silen.app.bean.Container;
import silen.app.db.ContainerDAO;
import silen.app.db.TaskDAO;
import silen.app.service.MyService;
import silen.app.utils.FormatArry;
import silen.app.utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class GuestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Intent intent;
    private GridView mGvMenu;
    private Toolbar mToolbar;
    private TextView mTvUser;
    private SimpleAdapter mAdapter;
    private String mUserName;
    private String[] mLabels = new String[]{

            "清空数据","信息查询", "获取任务", "历史信息","启动服务",

//            "尚未开放", "清空数据", "尚未开放",//"任务查询", "任务列表", "任务退回",
//            "信息查询", "获取任务", "箱号列表",
//            "尚未开放", "启动服务", "尚未开放",
    };
    private int[] mImages = new int[]{
            R.drawable.menu_flight_info, R.drawable.menu_search,
            R.drawable.menu_reserve, R.drawable.menu_ticket,
            R.drawable.menu_flight_info,

//            R.drawable.menu_dispark, R.drawable.menu_flight_info, R.drawable.menu_dispark,//R.drawable.menu_flight_info, R.drawable.menu_ticket, R.drawable.menu_reserve,
//            R.drawable.menu_search, R.drawable.menu_reserve, R.drawable.menu_ticket,
//            R.drawable.menu_dispark, R.drawable.menu_flight_info, R.drawable.menu_dispark,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar_guest);
        mToolbar.setTitle("任务管理");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mGvMenu = (GridView) findViewById(R.id.id_gv_menu);
        mTvUser = (TextView) findViewById(R.id.id_tv_username);
        initData();
        mTvUser.setText("欢迎你, " + mUserName);
        mGvMenu.setAdapter(mAdapter);
        mGvMenu.setOnItemClickListener(this);
    }

    public void webService() {

        ConnectivityManager con=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();
        boolean internet = false;
        if (mNetworkInfo != null) {
            internet = mNetworkInfo.isAvailable();
        }

        if(internet){
            //执行相关操作
            System.out.println("亲，网络已连接");


        }else{
            System.out.println("亲，网络连了么？");
        }
    }

    public void startService() {
        System.out.println("启动服务。。。。。");
        intent = new Intent(this, MyService.class);
        intent.putExtra("no", getIntent().getStringExtra("no"));
        startService(intent);
    }


    public void stopService() {
        if(intent!=null) {
            boolean is_stop = stopService(intent);
            System.out.println("亲，服务停止状态："+is_stop);
        }else {
            System.out.println("对不起，服务未能停止");
        }
    }


    private void initData() {
        String[] from = {"image", "text"};
        int[] to = {R.id.id_item_menu_img, R.id.id_item_menu_label};
        List<Map<String, Object>> datas = new ArrayList<>();
        for (int i = 0; i < mLabels.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", mImages[i]);
            map.put("text", mLabels[i]);
            datas.add(map);
        }
        mAdapter = new SimpleAdapter(this, datas, R.layout.item_menu, from, to);
        mUserName = getIntent().getStringExtra(SignupActivity.SEND_USER_NAME);
        startService();
        switchToActivity(TaskActivity.class);
        this.finish();
    }


    /**
     * 菜单单击时
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case 0:
                clearDb();
                break;
            case 1:
                switchToActivity(TaskActivity.class);
                break;
            case 2:
                getTask();
                break;
            case 3:
                switchToActivity(HistoryActivity.class);
                break;
            case 4:
                startService();
                break;


//            case 0:
//                //switchToActivity(SearchActivity.class);
//                break;
//            case 1:
//                //switchToActivity(OrdersActivity.class);
//                clearDb();
//                break;
//            case 2:
//                //switchToActivity(RefundActivity.class);
//                break;
//            case 3:
//                switchToActivity(TaskActivity.class);
//                break;
//            case 4:
//                getTask();
//                break;
//            case 5:
//                switchToActivity(HistoryActivity.class);
//                break;
//            case 7:
//                startService();
//                break;
        }
    }

    /**
     * 切换到指定的Activity
     */
    private void switchToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra("userName", mUserName);
        intent.putExtra("uid", getIntent().getStringExtra("uid"));

        intent.putExtra("no", getIntent().getStringExtra("no"));
        intent.putExtra("realname", getIntent().getStringExtra("realname"));
        intent.putExtra("mname", getIntent().getStringExtra("mname"));

        startActivity(intent);
    }

    private void getTask(){
        new Thread(new Runnable() {
            public void run() {


                boolean is_crt;
                int dataNum = 0;
                ArrayList<String> cmd = new ArrayList();
                String no = getIntent().getStringExtra("no");
                cmd.add(no);
                String url = FormatArry.formatArryToUrl("1002",cmd);
                String result = HttpUtils.doGet(url);

                System.out.println("开始获取门吊号："+no+"的任务");

                try {
                    if(result!=null) {

                        JSONObject jsonObject = new JSONObject(result);
                        is_crt = (boolean) jsonObject.get("IsSuccess");
                        if(is_crt) {
                            JSONArray taskArr = (JSONArray) jsonObject.get("Obj");
                            TaskDAO taskDao = new TaskDAO(GuestActivity.this);
                            for (int i = 0; i < taskArr.length(); i++) {
                                JSONObject taskObj = (JSONObject) taskArr.get(i);
                                JSONObject masterObj = taskObj.getJSONObject("master");
                                Log.i("任务ID", masterObj.getInt("id")+"");
                                dataNum = taskArr.length();
                                Cursor taskC = taskDao.queryTask("_id=?", new String[]{masterObj.getString("id")});

                                if(taskC.getCount()<1) {
                                    ContentValues values = new ContentValues();
                                    values.put("_id", masterObj.getInt("id"));
                                    String  state = "false";
                                    if(!masterObj.isNull("dangqianzhuangtai") && masterObj.getInt("dangqianzhuangtai")==1){
                                        state = "true";
                                    }
                                    values.put("state", state);
                                    values.put("mendiaohao", masterObj.isNull("mendiaohao")?" ":masterObj.getString("mendiaohao"));
                                    values.put("mendiaomingcheng", masterObj.isNull("mendiaomingcheng")?" ":masterObj.getString("mendiaomingcheng"));
                                    values.put("fabushijian", masterObj.isNull("fabushijian")?" ":masterObj.getString("fabushijian"));
                                    values.put("renwunum", masterObj.isNull("renwunum")?" ":masterObj.getString("renwunum"));
                                    values.put("faburenid", masterObj.isNull("faburenid")?" ":masterObj.getString("faburenid"));
                                    values.put("faburenxingming", masterObj.isNull("faburenxingming")?" ":masterObj.getString("faburenxingming"));
                                    values.put("fabuflag", masterObj.isNull("fabuflag")?" ":masterObj.getString("fabuflag"));
                                    values.put("jieshourenid", masterObj.isNull("jieshourenid")?" ":masterObj.getString("jieshourenid"));
                                    values.put("jieshoushijian", masterObj.isNull("jieshoushijian")?" ":masterObj.getString("jieshoushijian"));
                                    values.put("dangqianzhuangtai", masterObj.isNull("dangqianzhuangtai")?" ":masterObj.getString("dangqianzhuangtai"));
                                    taskDao.insertTask(values);
                                }
                                JSONArray detailArr = (JSONArray) taskObj.get("detail");
                                ContainerDAO cntDAO = new ContainerDAO(GuestActivity.this);
                                for (int j = 0; j < detailArr.length(); j++) {
                                    JSONObject boxObj = (JSONObject) detailArr.get(j);
                                    Log.i("箱号ID", boxObj.getInt("id") + "");

                                    Cursor cntC = cntDAO.queryContainers("_id=?", new String[]{boxObj.getString("id")});
                                    if(cntC.getCount()<1) {
                                        ContentValues cvalues = new ContentValues();
                                        cvalues.put("_id", boxObj.getInt("id"));
                                        cvalues.put("state", "false");
                                        cvalues.put("taskmasterid", boxObj.isNull("taskmasterid")?" ":boxObj.getString("taskmasterid"));
                                        cvalues.put("yundanid", boxObj.isNull("yundanid")?" ":boxObj.getString("yundanid"));
                                        cvalues.put("yundanhao", boxObj.isNull("yundanhao")?" ":boxObj.getString("yundanhao"));
                                        cvalues.put("fazhan", boxObj.isNull("fazhan")?" ":boxObj.getString("fazhan"));
                                        cvalues.put("daozhan", boxObj.isNull("daozhan")?" ":boxObj.getString("daozhan"));
                                        cvalues.put("zhuanyongxianmingcheng", boxObj.isNull("zhuanyongxianmingcheng")?" ":boxObj.getString("zhuanyongxianmingcheng"));
                                        cvalues.put("tuoyuanren", boxObj.isNull("tuoyuanren")?" ":boxObj.getString("tuoyuanren"));
                                        cvalues.put("shouhuoren", boxObj.isNull("shouhuoren")?" ":boxObj.getString("shouhuoren"));
                                        cvalues.put("xiangshu", boxObj.isNull("xiangshu")?" ":boxObj.getString("xiangshu"));
                                        cvalues.put("pinming", boxObj.isNull("pinming")?" ":boxObj.getString("pinming"));
                                        cvalues.put("xiangxing", boxObj.isNull("xiangxing")?" ":boxObj.getString("xiangxing"));
                                        cvalues.put("xianghao", boxObj.isNull("xianghao")?" ":boxObj.getString("xianghao"));
                                        cvalues.put("zhongliang", boxObj.isNull("zhongliang")?" ":boxObj.getString("zhongliang"));
                                        cvalues.put("riqi", boxObj.isNull("riqi")?" ":boxObj.getString("riqi"));
                                        cvalues.put("chezhong", boxObj.isNull("chezhong")?" ":boxObj.getString("chezhong"));
                                        cvalues.put("chexing", boxObj.isNull("chexing")?" ":boxObj.getString("chexing"));
                                        cntDAO.insertContainers(cvalues);
                                    }
                                }
                            }
                            Snackbar.make(mGvMenu, "已获取"+dataNum+"条任务！", Snackbar.LENGTH_SHORT).show();

                        }else{
                            System.out.println("获取任务失败！");
                            Snackbar.make(mGvMenu, "获取任务失败！", Snackbar.LENGTH_SHORT).show();
                        }
                    }else{
                        System.out.println("获取任务失败！");
                        Snackbar.make(mGvMenu, "获取任务失败！", Snackbar.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void clearDb(){
        TaskDAO taskDao = new TaskDAO(GuestActivity.this);
        ContainerDAO cntDao = new ContainerDAO(GuestActivity.this);
        taskDao.deleteAll();
        cntDao.deleteAll();
        Toast.makeText(GuestActivity.this, "已清空数据！", Toast.LENGTH_SHORT).show();
    }
}
