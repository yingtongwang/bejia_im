package www.beijia.com.cn.bejia_im.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import www.beijia.com.cn.bejia_im.cache.DataCacheManager;
import www.beijia.com.cn.bejia_im.cache.DemoCache;
import www.beijia.com.cn.bejia_im.R;
import www.beijia.com.cn.bejia_im.common.preferences.Preferences;
import www.beijia.com.cn.bejia_im.common.preferences.UserPreferences;
import www.beijia.com.cn.bejia_im.common.ui.dialog.DialogManager;
import www.beijia.com.cn.bejia_im.util.MD5;
import www.beijia.com.cn.bejia_im.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public class LoginActivity extends BaseActivity {
    private static final String KICK_OUT = "KICK_OUT";
    private static final String TAG = "LoginActivity";

    private com.rey.material.widget.Button switchModeBtn;
    private com.rey.material.widget.EditText loginAccountEdit;
    private com.rey.material.widget.EditText loginPasswordEdit;

    private com.rey.material.widget.EditText registerAccountEdit;
    private com.rey.material.widget.EditText registerNickNameEdit;
    private com.rey.material.widget.EditText registerPasswordEdit;

    private boolean registerMode = false; // 注册模式
    private boolean registerPanelInited = false; // 注册面板是否初始化

    private View loginLayout;
    private View registerLayout;

    private AbortableFuture<LoginInfo> loginRequest;
    private Menu mMenu;

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
        initView();
    }

    @Override
    protected void onDestroy() {
        if (DialogManager.isShowing())
            DialogManager.dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    protected void initView() {
        switchModeBtn = findView(R.id.register_login_tip);
        switchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });
        loginLayout = findView(R.id.login_layout);
        registerLayout = findView(R.id.register_layout);
        switchModeBtn = findView(R.id.register_login_tip);

        switchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });
        setupLoginPanel();
    }

    /**
     * 登录面板
     */
    private void setupLoginPanel() {
        loginAccountEdit = findView(R.id.edit_login_account);
        loginPasswordEdit = findView(R.id.edit_login_password);

        loginAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginAccountEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.addTextChangedListener(textWatcher);

        String account = Preferences.getUserAccount();
        loginAccountEdit.setText(account == null ? "xiaoying2012" : account);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 更新右上角按钮状态
            if (!registerMode) {
                // 登录模式
                boolean isEnable = loginAccountEdit.getText().length() > 0
                        && loginPasswordEdit.getText().length() > 0;
                updateRightTopBtn(mContext, isEnable);
            }
        }
    };

    private void updateRightTopBtn(Context context, boolean isEnable) {
        if (mMenu != null) {
            MenuItem menuItem = mMenu.findItem(R.id.action_settings);
            menuItem.setEnabled(isEnable);
        }
    }

    /**
     * ***************************************** 注册/登录切换 **************************************
     */
    private void switchMode() {
        registerMode = !registerMode;

        if (registerMode && !registerPanelInited) {
            registerAccountEdit = findView(R.id.edit_register_account);
            registerNickNameEdit = findView(R.id.edit_register_nickname);
            registerPasswordEdit = findView(R.id.edit_register_password);

            registerAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            registerNickNameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            registerPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

            registerAccountEdit.addTextChangedListener(textWatcher);
            registerNickNameEdit.addTextChangedListener(textWatcher);
            registerPasswordEdit.addTextChangedListener(textWatcher);

            registerPanelInited = true;
        }

        setTitle(registerMode ? R.string.register : R.string.login);
        loginLayout.setVisibility(registerMode ? View.GONE : View.VISIBLE);
        registerLayout.setVisibility(registerMode ? View.VISIBLE : View.GONE);
        switchModeBtn.setText(registerMode ? R.string.login_has_account : R.string.register);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_settings);
        menuItem.setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 当Menu有命令被选择时，会调用此方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if (registerMode) {
                    //register();
                } else {
                    login();
                }
                break;
        }
        // 返回false允许正常的菜单处理资源，若返回true，则直接在此毁灭它
        return super.onOptionsItemSelected(item);
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogManager.dismissProgressDialog();
    }

    /*
    *********************登陆
     */
    private void login() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                DialogManager.showProgressDialog(LoginActivity.this, null, "登陆中", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (loginRequest != null) {
                            loginRequest.abort();
                            onLoginDone();
                        }
                    }
                }).setCanceledOnTouchOutside(false);
            }
        });


        final String account = loginAccountEdit.getEditableText().toString().toLowerCase();
        final String token = tokenFromPassword(loginPasswordEdit.getEditableText().toString());

        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                    @Override
                    public void onSuccess(LoginInfo o) {
                        LogUtil.i(TAG, "login success");

                        onLoginDone();
                        DemoCache.setAccount(account);
                        Preferences.saveUserAccount(account);
                        Preferences.saveUserToken(token);

                        // 初始化消息提醒
                        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                        // 初始化免打扰
                        if (UserPreferences.getStatusConfig() == null) {
                            UserPreferences.setStatusConfig(DemoCache.getNotificationConfig());
                        }
                        NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

                        // 构建缓存
                        DataCacheManager.buildDataCacheAsync();

                        // 进入主界面
                        //MainActivity.start(LoginActivity.this, null);
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {
                        onLoginDone();
                        if (i == 302 || i == 404) {
                            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败: " + i, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Toast.makeText(LoginActivity.this, "登录失败: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };

        //登陆
        NIMClient.getService(AuthService.class).login(new LoginInfo(account,token)).setCallback(callback);
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("www.beijia.com.cn.bejia_im.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }
}
