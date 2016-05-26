package www.beijia.com.cn.bejia_im.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import www.beijia.com.cn.bejia_im.R;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    private Toolbar mToolbar;
    private FrameLayout layoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_top_bar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutContainer = (FrameLayout) findViewById(R.id.layout_container);
    }

    public void setContentChildView(int layoutID) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View mView = layoutInflater.inflate(layoutID, null);
        layoutContainer.addView(mView, 0);
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
