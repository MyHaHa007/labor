package cn.tianruan.LaborContractUser.ContractDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.tianruan.LaborContractUser.R;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/7/13.
 */
public class ContractUrlActivity extends SwipeBackActivity {
    @InjectView(R.id.contracturl_returnBtn)
    ImageView ReturnBtn;
    @InjectView(R.id.contracturl_webview)
    WebView mWebview;
    private static final String TAG = "ContractUrlActivity";
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracturl);
        ButterKnife.inject(this);
        getUrl();
        Log.e(TAG, "onCreate: "+"http://10.0.26.2:8083"+url);
        mWebview.loadUrl("http://10.0.26.2:8083/xxzx/userfiles/1/files/files/2017/07/2017年劳动合同书范本.pdf");
    }

    private void getUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }

    @OnClick(R.id.contracturl_returnBtn)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }
}
