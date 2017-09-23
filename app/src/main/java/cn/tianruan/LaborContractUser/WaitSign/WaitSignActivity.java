package cn.tianruan.LaborContractUser.WaitSign;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.tianruan.LaborContractUser.Config.BasicUrlPrefix;
import cn.tianruan.LaborContractUser.ContractDetails.ContractDetailsActivity;
import cn.tianruan.LaborContractUser.CustomView.MenuItemView;
import cn.tianruan.LaborContractUser.Login.LoginActivity;
import cn.tianruan.LaborContractUser.R;
import cn.tianruan.LaborContractUser.WaitSign.adapter.WaitSignAdapter;
import cn.tianruan.LaborContractUser.WaitSign.api.WaitSignAPI;
import cn.tianruan.LaborContractUser.WaitSign.javabean.waitsignjavabean;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WaitSignActivity extends AppCompatActivity {
    private static final String TAG = "WaitSignActivity";
    private static final int REQUEST_COUNT = 10;

    @InjectView(R.id.WaitSign_Recyclerview)
    LRecyclerView mRecyclerview;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.activityContent)
    LinearLayout activityContent;
    @InjectView(R.id.activityMenu)
    LinearLayout activityMenu;
    @InjectView(R.id.top_image)
    CircleImageView topImage;
    @InjectView(R.id.menu_exit)
    MenuItemView menuExit;


    //网络参数
    private OkHttpClient httpClient;
    private Retrofit retrofit;

    //SessionId
    private String sessionid;

    private List<waitsignjavabean> mData;
    private WaitSignAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    mAdapter = new WaitSignAdapter(WaitSignActivity.this);
                    mAdapter.setmData(mData);
                    mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
                    mRecyclerview.setLayoutManager(new LinearLayoutManager(WaitSignActivity.this));
                    mRecyclerview.setAdapter(mLRecyclerViewAdapter);

                    //刷新完毕
                    mRecyclerview.refreshComplete(10);

                    //设置点击Item点击事件
                    mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent ContractDetailsIntent = new Intent(WaitSignActivity.this, ContractDetailsActivity.class);
                            ContractDetailsIntent.putExtra("id", mData.get(position).getId());
                            ContractDetailsIntent.putExtra("ldhtTemplateID", mData.get(position).getLdhtTemplate().getId());
                            startActivity(ContractDetailsIntent);

                        }
                    });

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initSession();
        initNetParameter();

        initDrawerListener();

        setRecyclerview();

        //开始加载数据（下拉加载数据）
        mRecyclerview.refresh();


    }


    private void setRecyclerview() {
        //设置下拉刷新时间
        mRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

    }

    private void initSession() {
        SharedPreferences sessionidShare = getSharedPreferences("Sessionid", MODE_PRIVATE);
        sessionid = sessionidShare.getString("sessionid", null);
    }


    //初始化网络参数
    private void initNetParameter() {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
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


    private void initDrawerListener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //设置主布局随菜单滑动而滑动
                int drawerViewWidth = drawerView.getWidth();
                activityContent.setTranslationX(drawerViewWidth * slideOffset);

                //设置控件最先出现的位置
                double padingLeft = drawerViewWidth * (1 - 0.618) * (1 - slideOffset);
                activityMenu.setPadding((int) padingLeft, 0, 0, 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    private void initData() {
        WaitSignAPI waitSignAPI = retrofit.create(WaitSignAPI.class);
        Call<ResponseBody> waitSignData = waitSignAPI.getWaitSignData(sessionid, true,1);
        waitSignData.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    mData = gson.fromJson(response.body().string(),
                            new TypeToken<List<waitsignjavabean>>() {
                            }.getType());


                } catch (IOException e) {
                    e.printStackTrace();
                }

                mHandler.sendEmptyMessage(1);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + "解析失败");
            }
        });

    }


    @OnClick({R.id.top_image, R.id.menu_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_image:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确定退出账号?");
                builder.setTitle("退出登录");
                builder.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences share = getSharedPreferences("IdAndPwd", MODE_PRIVATE);
                        SharedPreferences.Editor edit = share.edit();
                        edit.putString("Id", null);
                        edit.putString("Password", null);
                        edit.commit();
                        dialog.dismiss();
                        Intent exitIntent = new Intent(WaitSignActivity.this, LoginActivity.class);
                        exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(exitIntent);
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
        }
    }
}
