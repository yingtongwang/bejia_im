package www.beijia.com.cn.bejia_im.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Wangyingbao on 2016/5/19. 基类
 */
public class TActivity extends Activity {
    private boolean destroyed = false;

    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
}
