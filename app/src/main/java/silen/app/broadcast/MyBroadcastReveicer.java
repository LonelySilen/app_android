package silen.app.broadcast;

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import silen.app.service.MyService;

public class MyBroadcastReveicer extends BroadcastReceiver {
    public MyBroadcastReveicer() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent i = new Intent(context, MyService.class);
        context.startService(i);
    }
}