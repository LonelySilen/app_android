package silen.app.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import silen.app.R;
import silen.app.db.ContainerDAO;
import silen.app.db.ContainerProvider;
import silen.app.utils.FormatArry;
import silen.app.utils.HttpUtils;
import silen.app.view.ContainerActivity;
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

/**
 * Created by admin on 2016/10/24.
 */

public class ContainerAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private View mRoot;
    private boolean mIsPaying;

    private String []argvs;
    private String id;

    public ContainerAdapter(Context context, Cursor c, int flags, String [] argvs) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
        this.argvs = argvs;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mRoot = mInflater.inflate(R.layout.item_containers, null, false);
        //mIsPaying = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
        ViewHolder holder = new ViewHolder();
        holder.xianghao = (TextView) mRoot.findViewById(R.id.id_tv_cnts_no);
//        holder.riqi = (TextView) mRoot.findViewById(R.id.id_tv_cnts_time);
//        holder.fazhan = (TextView) mRoot.findViewById(R.id.id_tv_cnts_start);
//        holder.daozhan = (TextView) mRoot.findViewById(R.id.id_tv_cnts_end);
//        holder.tuoyuanren = (TextView) mRoot.findViewById(R.id.id_tv_cnts_shipper);
//
//        holder.pinming = (TextView) mRoot.findViewById(R.id.id_tv_cnts_name);
//        holder.xiangshu = (TextView) mRoot.findViewById(R.id.id_tv_cnts_boxnum);
//        holder.zhongliang = (TextView) mRoot.findViewById(R.id.id_tv_cnts_weight);
//        holder.chezhong = (TextView) mRoot.findViewById(R.id.id_tv_cnts_class);
//        holder.chexing = (TextView) mRoot.findViewById(R.id.id_tv_cnts_type);

        holder.mIvPaying = (ImageView) mRoot.findViewById(R.id.id_iv_is_paying);

        holder.btnFinish = (Button)mRoot.findViewById(R.id.id_finish_cnt);

        mRoot.setTag(holder);
        return mRoot;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        mIsPaying = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
        holder.xianghao.setText(cursor.getString(cursor.getColumnIndex("xianghao")));
//        holder.riqi.setText(cursor.getString(cursor.getColumnIndex("riqi")));
//        holder.fazhan.setText(cursor.getString(cursor.getColumnIndex("fazhan")));
//        holder.daozhan.setText(cursor.getString(cursor.getColumnIndex("daozhan")));
//        holder.tuoyuanren.setText(cursor.getString(cursor.getColumnIndex("tuoyuanren")));
//
//        holder.pinming.setText(cursor.getString(cursor.getColumnIndex("pinming")));
//        holder.xiangshu.setText(cursor.getString(cursor.getColumnIndex("xiangshu")));
//        holder.zhongliang.setText(cursor.getString(cursor.getColumnIndex("zhongliang")));
//        holder.chezhong.setText(cursor.getString(cursor.getColumnIndex("chezhong")));
//        holder.chexing.setText(cursor.getString(cursor.getColumnIndex("chexing")));
        mIsPaying = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
        holder.btnFinish.setTag(cursor.getString(cursor.getColumnIndex("_id")));

        if (mIsPaying) {
            //holder.mIvPaying.setImageResource(R.drawable.item_is_paying);
            holder.btnFinish.setText("已操作");
            holder.btnFinish.setBackgroundColor(Color.parseColor("#ABABAB"));
            holder.btnFinish.setOnClickListener(null);
        }else{
            holder.btnFinish.setText("确认操作");
            holder.btnFinish.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Button bt = (Button) v;
                    bt.setClickable(false);
                    bt.setBackgroundColor(Color.parseColor("#ABABAB"));
                    id = bt.getTag().toString();
                    boolean isSuc = false;
                    //System.out.println("ZZZZZZZZZ");
                    new Thread(new Runnable() {
                        public void run() {
                            if(argvs!=null && argvs.length>0){
                                System.out.println("argvs[0]:"+argvs[0]);
                                System.out.println("argvs[1]:"+argvs[1]);
                            }

                            String mendiaohao = argvs[1];
                            Date currentTime = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                            String dateString = formatter.format(currentTime);

                            ArrayList<String> command = new ArrayList();
                            command.add(id);
                            command.add(mendiaohao);
                            command.add(argvs[0]);
                            command.add(dateString);
                            String cur_url = FormatArry.formatArryToUrl("1006",command);
                            System.out.println(cur_url);
                            String cur_result = HttpUtils.doGet(cur_url);


                            try {
                                if(cur_result!=null) {
                                    System.out.println("返回结果："+cur_result);
                                    JSONObject jsonObject = new JSONObject(cur_result);
                                    boolean is_crt = (boolean) jsonObject.get("IsSuccess");
                                    if(is_crt) {
                                        ContainerDAO cntDAO = new ContainerDAO(context);
                                        ContentValues values = new ContentValues();
                                        values.put("state", "true");
                                        cntDAO.updateContainers(values, "_id=?", new String[]{id + ""});

                                        //SystemClock.sleep(1000);

                                        Snackbar.make(mRoot, "处理成功", Snackbar.LENGTH_SHORT).show();
                                    }else{
                                        Snackbar.make(mRoot, "操作失败！", Snackbar.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Snackbar.make(mRoot, "网络异常,操作失败！", Snackbar.LENGTH_SHORT).show();
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                Snackbar.make(mRoot, "网络异常！", Snackbar.LENGTH_SHORT).show();
                            }

                        }
                    }).start();
                    SystemClock.sleep(300);
                    notifyDataSetChanged();
                    // 广播通知
                    Intent intent = new Intent();
                    intent.setAction("adapter.refresh");
                    context.sendBroadcast(intent);

                    bt.setClickable(true);
                    bt.setBackgroundColor(Color.parseColor("#DC143C"));
                }
            });
        }
    }


    /**
     * 怕判断当前行是否可以单击
     */
    @Override
    public boolean isEnabled(int position) {

        return super.isEnabled(position);
    }

    public static class ViewHolder {
        public TextView xianghao;
        public TextView riqi;
        public TextView fazhan;
        public TextView daozhan;
        public TextView tuoyuanren;
        public TextView pinming;
        public TextView xiangshu;
        public TextView zhongliang;
        public TextView chezhong;
        public TextView chexing;
        public Button btnFinish;
        public ImageView mIvPaying;
    }
}
