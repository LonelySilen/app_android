package silen.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import silen.app.R;

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

public class TaskAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private View mRoot;
    private boolean mIsPaying;

    public TaskAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mRoot = mInflater.inflate(R.layout.item_tasks, null, false);
        //mIsPaying = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
        ViewHolder holder = new ViewHolder();
        holder.mendiaohao = (TextView) mRoot.findViewById(R.id.id_tv_orders_starting);
        holder.mendiaomingcheng = (TextView) mRoot.findViewById(R.id.id_tv_orders_ending);
        holder.renwunum = (TextView) mRoot.findViewById(R.id.id_tv_orders_seat_id);
        holder.faburenxingming = (TextView) mRoot.findViewById(R.id.id_tv_orders_seat_info);
        holder.fabushijian = (TextView) mRoot.findViewById(R.id.id_tv_orders_flight_time);
        holder.mIvPaying = (ImageView) mRoot.findViewById(R.id.id_iv_is_paying);
        mRoot.setTag(holder);
        return mRoot;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mendiaohao.setText(cursor.getString(cursor.getColumnIndex("mendiaohao")));
        holder.mendiaomingcheng.setText(cursor.getString(cursor.getColumnIndex("mendiaomingcheng")));
        holder.renwunum.setText(cursor.getString(cursor.getColumnIndex("renwunum")));
        holder.faburenxingming.setText(cursor.getString(cursor.getColumnIndex("faburenxingming")));
        holder.fabushijian.setText(cursor.getString(cursor.getColumnIndex("fabushijian")));
        mIsPaying = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
        if (mIsPaying) {
            holder.mIvPaying.setImageResource(R.drawable.item_is_paying);
        }else{
            holder.mIvPaying.setImageResource(0);
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
        public TextView mendiaohao;
        public TextView mendiaomingcheng;
        public TextView renwunum;
        public TextView faburenxingming;
        public TextView fabushijian;
        public ImageView mIvPaying;
    }
}
