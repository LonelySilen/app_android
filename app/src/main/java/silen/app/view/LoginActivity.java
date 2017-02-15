package silen.app.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import silen.app.R;
import silen.app.db.GuestDAO;
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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private GuestDAO mGuestDAO;
    private Handler mHandler;

    // UI references.
    private EditText mEtUser;
    private EditText mEtPwd;
    private EditText mEtNo;
    private View mProgressView;
    private View mLoginFormView;
    private Button mBtnLogin;
    private Toolbar mToolbar;

    @Override
    protected void onStart() {
        super.onStart();
        mGuestDAO = new GuestDAO(this);
        mHandler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar_login);
        mToolbar.setTitle("用户登陆");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//true,隐藏返回箭头
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initView();
        mEtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mBtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnLogin.setClickable(false);
                mBtnLogin.setBackgroundColor(Color.parseColor("#ABABAB"));
                attemptLogin();
                mBtnLogin.setBackgroundColor(Color.parseColor("#DC143C"));
            }
        });

        String url = FormatArry.getUrl();
        mEtNo.setText(url.substring(7,url.length()-19));

    }

    private void initView() {
        mEtUser = (EditText) findViewById(R.id.id_et_guest_name);
        mEtPwd = (EditText) findViewById(R.id.id_et_guest_pwd);
        mEtNo = (EditText) findViewById(R.id.id_et_guest_no);
        mBtnLogin = (Button) findViewById(R.id.id_btn_guest_login);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if(val.equals("false")) {
                showProgress(false);
                Log.i("mylog", "请求结果-->" + val);
                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                mBtnLogin.setClickable(true);
                //mBtnLogin.setBackgroundColor(Color.parseColor("#DC143C"));
            }else{
                String userName = data.getString("userName");

                LoginActivity.this.finish();
                Intent intent = new Intent(LoginActivity.this, GuestActivity.class);
                intent.putExtra(SignupActivity.SEND_USER_NAME, userName);
                intent.putExtra("uid", data.getString("uid"));
                intent.putExtra("no", data.getString("no"));
                intent.putExtra("realname", data.getString("realname"));
                intent.putExtra("mname", data.getString("mname"));


                startActivity(intent);
            }
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            // Store values at the time of the login attempt.
            final String userName = mEtUser.getText().toString();
            String password = mEtPwd.getText().toString();
            String ip = mEtNo.getText().toString();
            Pattern pattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))([:]\\d{2,5}))");//Pattern.compile("^[-+]?[0-9]");
            //System.out.println("IP格式："+(ip!=null)+"-"+!ip.isEmpty()+"-"+pattern.matcher(ip).matches()+"");
            if(ip!=null && !ip.isEmpty()){
                if(pattern.matcher(ip).matches()) {
                    FormatArry.setUrl(ip);
                    //System.out.println("修改IP地址为" + ip);
                }else{
                    Snackbar.make(mLoginFormView, "服务器地址格式错误", Snackbar.LENGTH_SHORT).show();
                    mBtnLogin.setClickable(true);
                    return;
                }
            }

            ConnectivityManager con=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = con.getActiveNetworkInfo();
            boolean internet = false;
            if (mNetworkInfo != null) {
                internet = mNetworkInfo.isAvailable();
            }

            if(internet){
                //执行相关操作
                System.out.println("亲，网络已连接");
                //
                // TODO: http request.
                //
                boolean is_crt = false;
                String uid = "001";
                String realname = "未知";
                String no = "1";
                String mname = "未知门吊";
                ArrayList<String> cmd = new ArrayList();
                cmd.add(userName);
                cmd.add(password);
                String url = FormatArry.formatArryToUrl("1001",cmd);
                String result = HttpUtils.doGet(url);

                try {
                    if(result!=null) {
                        System.out.println(result);
                        JSONObject jsonObject = new JSONObject(result);

                        is_crt = (boolean) jsonObject.get("IsSuccess");
                        if(is_crt) {
                            JSONObject uObj = jsonObject.getJSONObject("Obj");
                            uid = uObj.getString("userid");
                            realname = uObj.getString("realname");
                            no = uObj.getString("mendiaoid");
                            mname = uObj.getString("mendiaomingcheng");
                        }else{
                            System.out.println("登陆失败！");
                        }
                    }else{
                        System.out.println("登陆失败！");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", String.valueOf(is_crt));
                data.putString("userName", userName);
                data.putString("uid", uid);
                data.putString("realname", realname);
                data.putString("no", no);
                data.putString("mname", mname);
                msg.setData(data);
                handler.sendMessage(msg);

            }else{
                System.out.println("亲，网络连了么？");
                // 登陆操作
                Cursor c = mGuestDAO.queryGuest("guest_name=? and guest_pwd=?"
                        , new String[]{userName, password});
                // 没有查询结果时,表示登陆失败
                if (!c.moveToNext()) {
                    Snackbar.make(mLoginFormView, "用户名或密码错误", Snackbar.LENGTH_SHORT).show();
                    mBtnLogin.setClickable(true);
                } else {
                    Snackbar.make(mLoginFormView, "登陆成功", Snackbar.LENGTH_SHORT).show();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, GuestActivity.class);
                            intent.putExtra(SignupActivity.SEND_USER_NAME, userName);

                            intent.putExtra("no", "1");

                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                }
            }


        }
    };

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEtUser.setError(null);
        mEtPwd.setError(null);

        // Store values at the time of the login attempt.
        final String userName = mEtUser.getText().toString();
        String password = mEtPwd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mEtPwd.setError("密码不合法,必须大于3位");
            focusView = mEtPwd;
            cancel = true;
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(userName)) {
            mEtUser.setError("用户名不能为空");
            focusView = mEtUser;
            cancel = true;
        } else if (!isUserValid(userName)) {
            mEtUser.setError("用户名必须大于3位");
            focusView = mEtUser;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            mBtnLogin.setClickable(true);
            //mBtnLogin.setBackgroundColor(Color.parseColor("#DC143C"));
        } else {

            new Thread(runnable).start();


        }
    }

    private boolean isUserValid(String user) {
        return user.length() >= 3;
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void tv_signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}

