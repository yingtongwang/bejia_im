package www.bejia.com.cn.demo.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import www.bejia.com.cn.demo.R;
import www.bejia.com.cn.demo.activity.adapter.EaseMessageAdapter;
import www.bejia.com.cn.demo.activity.dao.EaseEmojicon;
import www.bejia.com.cn.demo.util.Constant;
import www.bejia.com.cn.demo.util.EaseCommonUtils;
import www.bejia.com.cn.demo.util.EaseUserUtils;
import www.bejia.com.cn.demo.widget.datarow.EaseCustomChatRowProvider;
import www.bejia.com.cn.demo.widget.listview.MessageListView;

/**
 * Created by Wangyingbao on 2016/6/17.
 */
public class ChatFragment extends TFragment implements View.OnClickListener {

    //传入参数
    protected Bundle fragmentArgs;
    //群聊类型
    protected int chatType;
    protected String toChatUsername;

    // 按住说话录音控件
    ImageView voiceRecorderView;
    // 发送
    TextView buttonSendMessage;
    // 消息列表layout
    MessageListView messageList;

    protected EMConversation conversation;

    EaseMessageAdapter messageAdapter;

    protected int pagesize = 20;

    private boolean isMessageListInited;

    private EditText editTextMessage;

    protected EaseChatFragmentListener chatFragmentListener;

    public void setChatFragmentListener(EaseChatFragmentListener chatFragmentListener) {
        this.chatFragmentListener = chatFragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageAdapter.refresh();
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        fragmentArgs = getArguments();
        // 判断单聊还是群聊
        chatType = fragmentArgs.getInt(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
        // 会话人或群组id
        toChatUsername = fragmentArgs.getString(Constant.EXTRA_USER_ID);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initView() {
        // 按住说话录音控件
        voiceRecorderView = findView(R.id.buttonTextMessage);
        // 发送
        buttonSendMessage = findView(R.id.buttonSendMessage);
        // 消息列表layout
        messageList = findView(R.id.messageListView);

        editTextMessage = findView(R.id.editTextMessage);
        buttonSendMessage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSendMessage:
                //发送消息方法
                //==========================================================================
                EMMessage message = EMMessage.createTxtSendMessage(editTextMessage.getEditableText().toString(), toChatUsername);
                sendMessage(message);
                editTextMessage.setText("");
                break;
        }
    }

    @Override
    public void initData() {
        onConversationInit();
        //setRefreshLayoutListener();
//        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        messageAdapter = new EaseMessageAdapter(getContext(), toChatUsername, chatType, messageList);
        // 设置adapter显示消息
        messageList.setAdapter(messageAdapter);

        refreshSelectLast();

        isMessageListInited = true;
    }

    protected void onConversationInit() {
        // 获取当前conversation对象

        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
    }


    /**
     * 刷新列表
     */
    public void refresh() {
        if (messageAdapter != null) {
            messageAdapter.refresh();
        }
    }

    /**
     * 刷新列表，并且跳至最后一个item
     */
    public void refreshSelectLast() {
        if (messageAdapter != null) {
            messageAdapter.refreshSelectLast();
        }
    }

    /**
     * 刷新页面,并跳至给定position
     *
     * @param position
     */
    public void refreshSeekTo(int position) {
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }

    public void onBackPressed() {
        getActivity().finish();
        if (chatType == Constant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }
    }

    public interface MessageListItemClickListener {
        void onResendClick(EMMessage message);

        /**
         * 控件有对气泡做点击事件默认实现，如果需要自己实现，return true。
         * 当然也可以在相应的chatrow的onBubbleClick()方法里实现点击事件
         *
         * @param message
         * @return
         */
        boolean onBubbleClick(EMMessage message);

        void onBubbleLongClick(EMMessage message);

        void onUserAvatarClick(String username);
    }

    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        if (chatFragmentListener != null) {
            //设置扩展属性
            chatFragmentListener.onSetMessageAttributes(message);
        }
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == Constant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        //刷新ui
        if (isMessageListInited) {
            messageAdapter.refreshSelectLast();
        }
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }

                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(toChatUsername)) {
                    messageAdapter.refreshSelectLast();
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            if (isMessageListInited) {
                messageAdapter.refresh();
            }
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            if (isMessageListInited) {
                messageAdapter.refresh();
            }
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            if (isMessageListInited) {
                messageAdapter.refresh();
            }
        }
    };

    public interface EaseChatFragmentListener {
        /**
         * 设置消息扩展属性
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * 进入会话详情
         */
        void onEnterToChatDetails();

        /**
         * 用户头像点击事件
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * 消息气泡框点击事件
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * 消息气泡框长按事件
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * 扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * 设置自定义chatrow提供者
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }

}
