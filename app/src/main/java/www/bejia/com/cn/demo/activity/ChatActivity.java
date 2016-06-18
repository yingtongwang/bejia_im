package www.bejia.com.cn.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.util.EasyUtils;

import www.bejia.com.cn.demo.R;
import www.bejia.com.cn.demo.activity.fragment.ChatFragment;
import www.bejia.com.cn.demo.activity.login.BaseActivity;
import www.bejia.com.cn.demo.util.Constant;
import www.bejia.com.cn.demo.util.EaseUserUtils;

/**
 * Created by Wangyingbao on 2016/6/17.
 */
public class ChatActivity extends BaseActivity {

    public static ChatActivity chatActivity;
    String toChatUsername;
    //群聊类型
    protected int chatType;
    ChatFragment chatFragment;

    /**
     * onCreate 初始化Fragment
     *
     * @param arg0
     */
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentChildView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chatActivity = this;
        //---------------加载ChatFragment
        chatFragment = new ChatFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(Constant.EXTRA_USER_ID, toChatUsername);
        mBundle.putInt(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
        chatFragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatActivity = null;
    }

    @Override
    public void setmToolbarTile() {
        toChatUsername = "yingtong";//getIntent().getExtras().getString("userId");
        chatType = 1;// getIntent().getIntExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
        mToolbar.setTitle(toChatUsername);
        if (chatType == Constant.CHATTYPE_SINGLE) { // 单聊
            // 设置标题
            if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
                mToolbar.setTitle(EaseUserUtils.getUserInfo(toChatUsername).getNick());
            }

        } else {

            if (chatType == Constant.CHATTYPE_GROUP) {
                // 群聊
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    mToolbar.setTitle(group.getGroupName());
                // 监听当前会话的群聊解散被T事件
            } else {
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }
}
