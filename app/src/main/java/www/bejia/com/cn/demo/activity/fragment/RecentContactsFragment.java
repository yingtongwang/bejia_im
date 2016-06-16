package www.bejia.com.cn.demo.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import www.bejia.com.cn.demo.R;
import www.bejia.com.cn.demo.activity.adapter.RecentContactAdapter;

/**
 * Created by Wangyingbao on 2016/6/15. 最近联系人
 */
public class RecentContactsFragment extends TFragment {
    private final static int MSG_REFRESH = 2;
    protected boolean isConflict;
    // view
    private ListView listView;

    private View emptyBg;

    private TextView emptyHint;

    private FrameLayout errorItemContainer;

    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();

    private RecentContactAdapter adapter;


    public void onActivityCreated(Bundle s) {
        super.onActivityCreated(s);
        findViews();
        initMessageList();
        requestMessages(true);
        registerObservers(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_contacts, container, false);
    }

    private void findViews() {
        errorItemContainer = findView(R.id.fl_error_item);
        listView = findView(R.id.lvMessages);
        emptyBg = findView(R.id.emptyBg);
        emptyHint = findView(R.id.message_list_empty_hint);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        conversationList.clear();
        conversationList.addAll(loadConversationList());
        adapter = new RecentContactAdapter(getContext(), conversationList);
        listView.setAdapter(adapter);
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    adapter.notifyDataSetChanged();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 连接断开
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 获取会话列表
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {

        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * 请求消息
     *
     * @param delay
     */
    private void requestMessages(boolean delay) {
    }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
    }
}
