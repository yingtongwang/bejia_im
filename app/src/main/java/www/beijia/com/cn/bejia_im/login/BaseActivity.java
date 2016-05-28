package www.beijia.com.cn.bejia_im.login;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import www.beijia.com.cn.bejia_im.R;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Toolbar mToolbar;
    private FrameLayout layoutContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_top_bar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }

//        mToolbar.setLogo(R.drawable.logo);
//        mToolbar.setNavigationIcon(R.drawable.logo);
//        mToolbar.setTitleTextColor(0xffffffff);
//        mToolbar.setSubtitleTextColor(0xffffffff);

        layoutContainer = (FrameLayout) findViewById(R.id.layout_container);
    }

    public void setContentChildView(int layoutID) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View mView = layoutInflater.inflate(layoutID, null);
        layoutContainer.addView(mView, 0);
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    ///////////////////////////////////////////////////////////////////////////
    // Abstract Method In Activity
    ///////////////////////////////////////////////////////////////////////////

    protected abstract void initView();

    protected abstract void initData();

///////////////////////////////////////////////////////////////////////////
// Common Operation
///////////////////////////////////////////////////////////////////////////
}
