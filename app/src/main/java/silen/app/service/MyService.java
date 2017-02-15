package silen.app.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import silen.app.broadcast.MyBroadcastReveicer;
import silen.app.db.ContainerDAO;
import silen.app.db.TaskDAO;
import silen.app.utils.FormatArry;
import silen.app.utils.HttpUtils;
import silen.app.view.GuestActivity;

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

public class MyService extends Service {

    private String no = "1";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        no = intent.getStringExtra("no");
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        new Thread(new Runnable() {
            public void run() {
                System.out.println("服务运行了");

                boolean is_crt;
                int dataNum = 0;
                ArrayList<String> cmd = new ArrayList();

                cmd.add(no);
                String url = FormatArry.formatArryToUrl("1002",cmd);
                String result = HttpUtils.doGet(url);
                //System.out.println(FormatArry.getUrl());
                try {
                    if(result!=null) {

                        JSONObject jsonObject = new JSONObject(result);
                        is_crt = (boolean) jsonObject.get("IsSuccess");
                        if(is_crt) {
                            JSONArray taskArr = (JSONArray) jsonObject.get("Obj");
                            TaskDAO taskDao = new TaskDAO(MyService.this);
                            for (int i = 0; i < taskArr.length(); i++) {
                                JSONObject taskObj = (JSONObject) taskArr.get(i);
                                JSONObject masterObj = taskObj.getJSONObject("master");
                                //Log.i("任务ID", masterObj.getInt("id")+"");

                                Cursor taskC = taskDao.queryTask("_id=?", new String[]{masterObj.getString("id")});
                                dataNum = taskC.getCount();
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
                                ContainerDAO cntDAO = new ContainerDAO(MyService.this);
                                for (int j = 0; j < detailArr.length(); j++) {
                                    JSONObject boxObj = (JSONObject) detailArr.get(j);
                                    //Log.i("箱号ID", boxObj.getInt("id") + "");

                                    Cursor cntC = cntDAO.queryContainers("_id=?", new String[]{boxObj.getString("id")});
                                    if(cntC.getCount()<1) {
                                        ContentValues cvalues = new ContentValues();
                                        cvalues.put("_id", boxObj.getInt("id"));
                                        if(boxObj.isNull("querenrenid") || boxObj.get("querenrenid").toString().isEmpty()) {
                                            cvalues.put("state", "false");
                                        }
                                        else{
                                            cvalues.put("state", "true");
                                        }
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
                            SystemClock.sleep(3000);
                            // 广播通知
                            Intent intent = new Intent();
                            intent.setAction("action.refresh");
                            sendBroadcast(intent);

                        }else{
                            System.out.println("获取任务失败！");
                        }
                    }else{
                        System.out.println("获取任务失败！");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent2 = new Intent(this, MyBroadcastReveicer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+1000*60, pendingIntent);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
