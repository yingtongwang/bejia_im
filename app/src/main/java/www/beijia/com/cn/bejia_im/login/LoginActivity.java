package www.beijia.com.cn.bejia_im.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import www.beijia.com.cn.bejia_im.R;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public class LoginActivity extends BaseActivity {
    private static final String KICK_OUT = "KICK_OUT";

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KICK_OUT, kickOut);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_login);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
