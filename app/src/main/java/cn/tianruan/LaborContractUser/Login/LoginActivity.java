package cn.tianruan.LaborContractUser.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.tianruan.LaborContractUser.Config.BasicUrlPrefix;
import cn.tianruan.LaborContractUser.Login.javabean.loginsuccessful;
import cn.tianruan.LaborContractUser.Login.api.LoginAPI;
import cn.tianruan.LaborContractUser.R;
import cn.tianruan.LaborContractUser.WaitSign.WaitSignActivity;
import cn.tianruan.LaborContractUser.register.RegisterActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    private static final String TAG = "LoginActivity";

    //网络参数
    private OkHttpClient httpClient;
    private Retrofit retrofit;

    //加载框
    private LoadingDialog loadingDialog;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    loadingDialog.close();
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    //登录成功后把密码和账号保存到本地
                    SharedPreferences idAndPwdShare = getSharedPreferences("IdAndPwd", MODE_PRIVATE);
                    SharedPreferences.Editor edit = idAndPwdShare.edit();
                    edit.putString("Id",etUsername.getText().toString());
                    edit.putString("Password",etPassword.getText().toString());
                    edit.commit();

                    //加载框消失
                    loadingDialog.close();

                    //跳转到首页
                    Intent i2 = new Intent(LoginActivity.this,WaitSignActivity.class);
                    i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initNetParameter();
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

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                if (etUsername.getText().length()!=0&&etPassword.getText().length()!=0){
                    //登录
                    Login();
                }else if (etUsername.getText().length()==0&&etPassword.getText().length()!=0){
                    Toast.makeText(LoginActivity.this, "登录名不能为空", Toast.LENGTH_SHORT).show();
                }else if (etUsername.getText().length()!=0&&etPassword.getText().length()==0){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "登录名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void Login(){
        loadingDialog = new LoadingDialog(this);
        loadingDialog
                .setLoadingText("登录中...")//设置loading时显示的文字
                .show();

        LoginAPI loginAPI = retrofit.create(LoginAPI.class);
        Call<loginsuccessful> loginCall = loginAPI.login(etUsername.getText().toString(), etPassword.getText().toString(), true);
        loginCall.enqueue(new Callback<loginsuccessful>() {
            @Override
            public void onResponse(Call<loginsuccessful> call, Response<loginsuccessful> response) {
                if(response.body().getSessionid()!=null){
                    SharedPreferences sessionidShare = getSharedPreferences("Sessionid", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sessionidShare.edit();
                    edit.putString("sessionid",response.body().getSessionid());
                    edit.commit();
                    mHandler.sendEmptyMessage(0);
                }else {
                    mHandler.sendEmptyMessage(-1);
                }

            }

            @Override
            public void onFailure(Call<loginsuccessful> call, Throwable t) {

            }
        });
    }
}
