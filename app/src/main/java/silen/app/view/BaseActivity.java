package silen.app.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
 * 使用Toolbar替代Actionbar
 * 添加back键
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setViewLayout());
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar_base);
        mToolbar.setTitle(setLabel());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initData();
        initView();
        initEvent();
    }

    protected abstract void initEvent();

    // 设置标题
    protected abstract String setLabel();

    // 设置布局文件
    protected abstract int setViewLayout();


    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化组件
     */
    abstract protected void initView();

}
