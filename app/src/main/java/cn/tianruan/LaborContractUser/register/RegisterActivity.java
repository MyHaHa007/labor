package cn.tianruan.LaborContractUser.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.tianruan.LaborContractUser.Config.BasicUrlPrefix;
import cn.tianruan.LaborContractUser.R;
import cn.tianruan.LaborContractUser.register.api.RegisterAPI;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.cv_add)
    CardView cvAdd;
    @InjectView(R.id.register_mobile_ed)
    EditText MobileEd;
    @InjectView(R.id.register_newPassword_ed)
    EditText NewPasswordEd;
    @InjectView(R.id.register_confirmNewPassword_ed)
    EditText ConfirmNewPasswordEd;
    @InjectView(R.id.register_name_ed)
    EditText NameEd;
    @InjectView(R.id.register_identityCard_ed)
    EditText IdentityCardEd;
    @InjectView(R.id.register_permanentAddress_ed)
    EditText PermanentAddressEd;
    @InjectView(R.id.register_correspondenceAddress_ed)
    EditText CorrespondenceAddressEd;
    @InjectView(R.id.register_xlxwType_ed)
    EditText XlxwTypeEd;
    @InjectView(R.id.register_phone_ed)
    EditText PhoneEd;
    @InjectView(R.id.register_email_ed)
    EditText EmailEd;
    @InjectView(R.id.register_addIDCardFrontImg_tv)
    TextView AddIDCardFrontImgTv;
    @InjectView(R.id.register_idCardFront_img)
    ImageView IdCardFrontImg;
    @InjectView(R.id.register_addIDCardBackImg_tv)
    TextView AddIDCardBackImgTv;
    @InjectView(R.id.register_idCardBack_img)
    ImageView IdCardBackImg;
    @InjectView(R.id.register_addXlxwImg_tv)
    TextView AddXlxwImgTv;
    @InjectView(R.id.register_xlxw_img)
    ImageView XlxwImg;
    @InjectView(R.id.register_btn)
    Button registerBtn;

    private static final String TAG = "RegisterActivity";

    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    //选中图片Intent
    private Intent selectPhotoIntent;

    //判断加载那个图片  1为身份证正面照，2为身份证反面照，3为学位/学历照
    private int selectphoto;

    //Base64Photo
    private String Base64IDCardFront;
    private String Base64IDCardBack;
    private String Base64XlxwImg;

    //网络参数
    private OkHttpClient httpClient;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

        initSelectPhotoIntent();
        initNetParameter();
    }

    private void initSelectPhotoIntent() {
        selectPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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



    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @OnClick({R.id.register_addIDCardFrontImg_tv, R.id.register_addIDCardBackImg_tv, R.id.register_addXlxwImg_tv, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_addIDCardFrontImg_tv:
                selectphoto=1;
                startActivityForResult(selectPhotoIntent,IMAGE);
                break;
            case R.id.register_addIDCardBackImg_tv:
                selectphoto=2;
                startActivityForResult(selectPhotoIntent,IMAGE);
                break;
            case R.id.register_addXlxwImg_tv:
                selectphoto=3;
                startActivityForResult(selectPhotoIntent,IMAGE);
                break;
            case R.id.register_btn:

                if (Base64IDCardFront!=null&&Base64IDCardBack!=null&&Base64XlxwImg!=null){
                    register();
                }

                break;
        }
    }

    private void register() {
        RegisterAPI registerAPI = retrofit.create(RegisterAPI.class);
        Call<ResponseBody> registerCallBack = registerAPI.register(MobileEd.getText().toString(),
                NewPasswordEd.getText().toString(),
                ConfirmNewPasswordEd.getText().toString(),
                NameEd.getText().toString(),
                IdentityCardEd.getText().toString(),
                PermanentAddressEd.getText().toString(),
                CorrespondenceAddressEd.getText().toString(),
                XlxwTypeEd.getText().toString(),
                PhoneEd.getText().toString(),
                EmailEd.getText().toString(),
                Base64IDCardFront,
                Base64IDCardBack,
                Base64XlxwImg);

        registerCallBack.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: "+"注册成功" );
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: "+"注册失败");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
//            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bm = BitmapFactory.decodeFile(imagePath,options);


            if (selectphoto==1){
                Base64IDCardFront=Base64Photo(bm);
                IdCardFrontImg.setImageBitmap(bm);
            }else if (selectphoto==2){
                Base64IDCardBack=Base64Photo(bm);
                IdCardBackImg.setImageBitmap(bm);
            }else if(selectphoto==3){
                Base64XlxwImg=Base64Photo(bm);
                XlxwImg.setImageBitmap(bm);
            }
            c.close();
        }
    }

    private String Base64Photo(Bitmap bitmap){
        //Base64图片
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);


    }
}
