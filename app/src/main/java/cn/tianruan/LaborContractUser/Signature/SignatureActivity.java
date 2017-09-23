package cn.tianruan.LaborContractUser.Signature;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.rm.freedrawview.FreeDrawView;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.tianruan.LaborContractUser.R;

/**
 * Created by Administrator on 2017/7/10.
 */
public class SignatureActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, FreeDrawView.DrawCreatorListener {
    @InjectView(R.id.mDrawView)
    FreeDrawView mDrawView;
    @InjectView(R.id.stroke_width)
    SeekBar strokeWidthSeekBar;
    @InjectView(R.id.saveBtn)
    Button previewBtn;
    @InjectView(R.id.btn_clear_all)
    Button btnClearAll;

    public final static int RESULT_CODE=1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ButterKnife.inject(this);
        setWidthSeekBar();

    }

    //设置画笔宽度，监听WithSeekBar
    private void setWidthSeekBar() {
        strokeWidthSeekBar.setOnSeekBarChangeListener(null);
        strokeWidthSeekBar.setMax(80);
        strokeWidthSeekBar.setProgress(40);
        strokeWidthSeekBar.setOnSeekBarChangeListener(this);
    }


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


    @OnClick({R.id.btn_clear_all, R.id.saveBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear_all:
                mDrawView.undoAll();
                break;
            case R.id.saveBtn:
                takeAndShowScreenshot();
                break;
        }
    }


    private void takeAndShowScreenshot() {
        mDrawView.getDrawScreenshot(this);
    }

    @Override
    public void onDrawCreated(Bitmap draw) {
        //将图片存到本地
        SharedPreferences sharedPreferences = getSharedPreferences("Bitmap", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        draw.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        String headPicBase64 = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        editor.putString("Bitmap", headPicBase64);
        editor.commit();


        Intent intent=new Intent();
        intent.putExtra("back", "Back Data");
        setResult(RESULT_CODE, intent);
        finish();


    }

    @Override
    public void onDrawCreationError() {

    }


}
