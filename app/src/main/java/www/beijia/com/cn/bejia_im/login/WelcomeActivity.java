package www.beijia.com.cn.bejia_im.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

import www.beijia.com.cn.bejia_im.DemoCache;
import www.beijia.com.cn.bejia_im.MainActivity;
import www.beijia.com.cn.bejia_im.R;
import www.beijia.com.cn.bejia_im.common.activity.TActivity;
import www.beijia.com.cn.bejia_im.common.preferences.Preferences;
import www.beijia.com.cn.bejia_im.util.SysInfoUtil;
import www.beijia.com.cn.bejia_im.util.log.LogUtil;

/**
 * 欢迎界面，加入Bugly...
 */
public class WelcomeActivity extends TActivity {

    private static final String TAG = "WelcomeActivity";

    //第一次进入
    private static boolean firstEnter = true;
    private static boolean customSplash = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreate----");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (savedInstanceState != null) {
            setIntent(new Intent());
        }
        if (!firstEnter) {
            onIntent();
        } else {
            showSplashView();
        }
    }

    protected void onResume() {
        super.onResume();
        if (firstEnter) {
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (canAutoLogin()) {
                        onIntent();
                    } else {
                        LoginActivity.start(WelcomeActivity.this);
                        finish();
                    }
                }
            };
            if (customSplash) {
                new Handler().postDelayed(runnable, 1000);
            } else {
                runnable.run();
            }
        }
    }

    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        Log.i(TAG, "get local sdk token =" + token);
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }

    private void onIntent() {
        LogUtil.i(TAG, "onIntent----");
        if (TextUtils.isEmpty(DemoCache.getAccount())) {
            if (!SysInfoUtil.stackResumed(this)) {
                //跳转到Login
                LoginActivity.start(this);
            }
            finish();
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    parseNotifyIntent(intent);
                    return;
                }
//                } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P) || intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
//                    parseNormalIntent(intent);
//                }
            }
            if (!firstEnter && intent == null) {
                finish();
            } else {
                //调转Main
                showMainActivity();
            }
        }
    }

    private void parseNormalIntent(Intent intent) {
        showMainActivity(intent);
    }

    private void parseNotifyIntent(Intent intent) {
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if (messages == null || messages.size() > 1) {
            showMainActivity(null);
        } else {
            showMainActivity(new Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages.get(0)));
        }
    }

    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
//        MainActivity.start(WelcomeActivity.this, intent);
//        finish();
    }

    private void showSplashView() {
        getWindow().setBackgroundDrawableResource(R.drawable.splash_bg);
        customSplash = true;
    }
}
