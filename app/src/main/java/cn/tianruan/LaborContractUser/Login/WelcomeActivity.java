package cn.tianruan.LaborContractUser.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.tianruan.LaborContractUser.Config.BasicUrlPrefix;
import cn.tianruan.LaborContractUser.Login.javabean.loginsuccessful;
import cn.tianruan.LaborContractUser.Login.api.LoginAPI;
import cn.tianruan.LaborContractUser.WaitSign.WaitSignActivity;
import cn.tianruan.LaborContractUser.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/11.
 */
public class WelcomeActivity extends AppCompatActivity {
    //账号密码
    String id;
    String password;

    //网络参数
    private OkHttpClient httpClient;
    private Retrofit retrofit;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkIdAndPwd();
        initNetParameter();
        initLogin();
    }


    private void checkIdAndPwd() {
        SharedPreferences idAndPwdShare = getSharedPreferences("IdAndPwd", MODE_PRIVATE);
        id = idAndPwdShare.getString("Id", null);
        password = idAndPwdShare.getString("Password", null);
    }

    private void initNetParameter() {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
        retrofit=new Retrofit.Builder()
                .baseUrl(BasicUrlPrefix.getUrlprefix())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    //监控请求和响应
    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }


    //判断跳转
    private void initLogin() {
        if (id==null&&password==null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else{
            AutoLogin();
        }
    }

    private void AutoLogin(){
        LoginAPI loginAPI = retrofit.create(LoginAPI.class);
        Call<loginsuccessful> loginCall = loginAPI.login(id,password, true);
        loginCall.enqueue(new Callback<loginsuccessful>() {
            @Override
            public void onResponse(Call<loginsuccessful> call, Response<loginsuccessful> response) {
                if(response.body().getSessionid()!=null){
                    SharedPreferences sessionidShare = getSharedPreferences("Sessionid", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sessionidShare.edit();
                    edit.putString("sessionid",response.body().getSessionid());
                    edit.commit();
                    Intent homeIntent = new Intent(WelcomeActivity.this, WaitSignActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                }else {
                    Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }

            }

            @Override
            public void onFailure(Call<loginsuccessful> call, Throwable t) {

            }
        });
    }

}
