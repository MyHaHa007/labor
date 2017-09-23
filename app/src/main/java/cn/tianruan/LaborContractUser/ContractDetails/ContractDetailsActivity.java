package cn.tianruan.LaborContractUser.ContractDetails;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rm.freedrawview.FreeDrawView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.tianruan.LaborContractUser.Config.BasicUrlPrefix;
import cn.tianruan.LaborContractUser.ContractDetails.api.ContractDetailsAPI;
import cn.tianruan.LaborContractUser.ContractDetails.javabean.contractdetailsjavabean;
import cn.tianruan.LaborContractUser.R;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ContractDetailsActivity extends SwipeBackActivity implements SeekBar.OnSeekBarChangeListener, FreeDrawView.DrawCreatorListener {
    @InjectView(R.id.bookreturn_returnBtn)
    ImageView ReturnBtn;



    private final static int REQUEST_CODE = 1;
    @InjectView(R.id.contractdetails_contracturl)
    LinearLayout Contracturl;
    @InjectView(R.id.comfireContractBtn)
    FancyButton btnPreview;
    @InjectView(R.id.comfireContractBtn)
    FancyButton comfireContractBtn;

    private static final String TAG = "ContractDetailsActivity";
    @InjectView(R.id.mDrawView)
    FreeDrawView mDrawView;
    @InjectView(R.id.stroke_width)
    SeekBar strokeWidthSeekBar;
    @InjectView(R.id.btn_clear_all)
    Button btnClearAll;

    //签名图片
    private Bitmap bitmap;

    //网络参数
    private OkHttpClient httpClient;
    private Retrofit retrofit;

    //SessionId
    private String sessionid;

    //合同id
    private String conItractId;

    //加载框
    private LoadingDialog loadingDialog;

    //返回数据


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    loadingDialog.close();

                    break;
                case 2:
                    loadingDialog.loadSuccess();
                    loadingDialog.close();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractdetails);
        ButterKnife.inject(this);
        initSession();
        initParameter();
        initNetParameter();
        setWidthSeekBar();
        initData();
    }

    private void initSession() {
        SharedPreferences sessionidShare = getSharedPreferences("Sessionid", MODE_PRIVATE);
        sessionid = sessionidShare.getString("sessionid", null);
    }


    private void initParameter() {
        Intent intent = getIntent();
        conItractId = intent.getStringExtra("id");
        ldhtTemplateID=intent.getStringExtra("ldhtTemplateID");
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

    //设置画笔宽度，监听WithSeekBar
    private void setWidthSeekBar() {
        strokeWidthSeekBar.setOnSeekBarChangeListener(null);
        strokeWidthSeekBar.setMax(80);
        strokeWidthSeekBar.setProgress(40);
        strokeWidthSeekBar.setOnSeekBarChangeListener(this);
    }


    private void initData() {
        //加载框
//        loadingDialog = new LoadingDialog(this);
//        loadingDialog.setLoadingText("加载中...");
//        loadingDialog.show();
        Log.e(TAG, "initData: conItractId "+conItractId );
        ContractDetailsAPI contractDetailsAPI = retrofit.create(ContractDetailsAPI.class);
        Call<contractdetailsjavabean> contractDetailsData = contractDetailsAPI.getContractDetailsData(sessionid, true, conItractId,ldhtTemplateID);
        contractDetailsData.enqueue(new Callback<contractdetailsjavabean>() {
            @Override
            public void onResponse(Call<contractdetailsjavabean> call, Response<contractdetailsjavabean> response) {
                contractName = response.body().getLdhtTemplate().getName();
                companyAName = response.body().getCompany().getName();
                userBName = response.body().getName();
                DepartmentBname = response.body().getOffice().getName();
                startDate = response.body().getContractStartDate();
                endDate = response.body().getContractEndDate();
                url = response.body().getLdhtTemplate().getUrl();
                contractId = response.body().getId();

                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<contractdetailsjavabean> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.bookreturn_returnBtn, R.id.contractdetails_contracturl,
            R.id.comfireContractBtn, R.id.btn_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookreturn_returnBtn:
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;

            case R.id.contractdetails_contracturl:
                Intent urlIntent = new Intent(ContractDetailsActivity.this, ContractUrlActivity.class);
                urlIntent.putExtra("url", url);
                startActivity(urlIntent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.comfireContractBtn:
                mDrawView.getDrawScreenshot(this);

                break;
            case R.id.btn_clear_all:
                mDrawView.undoAll();
                break;
        }
    }

    private void comfireContract() {
        Log.e(TAG, "comfireContract: "+bitmap );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        String headPicBase64 = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        byte[] byteArray = Base64.decode(headPicBase64, Base64.DEFAULT);

        Log.e(TAG, "comfireContract: " + byteArray);

        //加载框
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setLoadingText("签约中...");
        loadingDialog.setSuccessText("签约成功");
        loadingDialog.show();

        ContractDetailsAPI contractDetailsAPI = retrofit.create(ContractDetailsAPI.class);
        Call<ResponseBody> call = contractDetailsAPI.postImg(sessionid, true, conItractId, byteArray);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    mHandler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == SignatureActivity.RESULT_CODE) {
//                Bundle bundle = data.getExtras();
//                String str = bundle.getString("back");
//                initImg();
//            }
//        }
//    }
//
//    private void initImg() {
//        SharedPreferences sharedPreferences = getSharedPreferences("Bitmap", Context.MODE_PRIVATE);
//        //第一步:取出字符串形式的Bitmap
//        String imageString = sharedPreferences.getString("Bitmap", "");
//        //第二步:利用Base64将字符串转换为ByteArrayInputStream
//        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
//        //第三步:利用ByteArrayInputStream生成Bitmap
//        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
//
//    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == strokeWidthSeekBar.getId()) {
            mDrawView.setPaintWidthPx(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onDrawCreated(Bitmap draw) {
        this.bitmap=draw;
        comfireContract();
    }

    @Override
    public void onDrawCreationError() {

    }
}
