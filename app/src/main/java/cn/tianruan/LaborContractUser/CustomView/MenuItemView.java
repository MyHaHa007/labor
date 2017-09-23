package cn.tianruan.LaborContractUser.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.tianruan.LaborContractUser.R;


/**
 * Created by Administrator on 2017/5/22.
 */
public class MenuItemView extends LinearLayout {

    private TextView mTvText;
    private ImageView mIvIcon;

    public MenuItemView(Context context) {
        this(context, null);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.home_menu_item, this);
        mTvText = (TextView) findViewById(R.id.menuItemTv);
        mIvIcon = (ImageView) findViewById(R.id.menuItemImg);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);

        String text = typedArray.getString(R.styleable.MenuItemView_itemText);
        int src = typedArray.getResourceId(R.styleable.MenuItemView_itemSrc, -1);

        if (text != null && !TextUtils.isEmpty(text)) {
            mTvText.setText(text);
        }

        if (src != -1) {
            mIvIcon.setImageResource(src);
        }
        typedArray.recycle();
    }

    public void setImageResource(int resId) {
        mIvIcon.setImageResource(resId);
    }

    public void setText(@StringRes int resId) {
        mTvText.setText(resId);
    }

    public void setText(String text) {
        mTvText.setText(text);
    }

    public void getText() {
        mTvText.getText();
    }
}
