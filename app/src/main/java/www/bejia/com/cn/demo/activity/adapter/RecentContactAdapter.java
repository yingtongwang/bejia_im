package www.bejia.com.cn.demo.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import www.bejia.com.cn.demo.R;
import www.bejia.com.cn.demo.util.EaseCommonUtils;
import www.bejia.com.cn.demo.util.EaseSmileUtils;
import www.bejia.com.cn.demo.util.EaseUserUtils;

/**
 * Created by Wangyingbao on 2016/6/15.
 */
public class RecentContactAdapter extends BaseAdapter {
    private Context context;
    private int mIndex;
    protected List<EMConversation> mConversationList = new ArrayList<EMConversation>();
    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;

    public RecentContactAdapter(Context mContext, List<EMConversation> conversationList) {
        mConversationList = conversationList;
        context = mContext;
    }

    public int getCount() {
        return mConversationList.size();
    }

    public Object getItem(int arg0) {
        return mConversationList.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nim_recent_contact_list_item,
                    null);
            holder = new ViewHolder();
            holder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
            holder.new_message_indicator = (ImageView) convertView.findViewById(R.id.new_message_indicator);
            holder.msgState = (ImageView) convertView.findViewById(R.id.img_msg_status);

            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_number_tip);
            holder.name = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            holder.time = (TextView) convertView.findViewById(R.id.tv_date_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EMConversation conversation = mConversationList.get(position);
        String username = conversation.getUserName();

        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            // 群聊消息，显示群聊头像
            holder.img_head.setImageResource(R.drawable.ease_group_icon);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);
        } else if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
            holder.img_head.setImageResource(R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
        } else {
            EaseUserUtils.setUserAvatar(context, username, holder.img_head);
            EaseUserUtils.setUserNick(username, holder.name);
        }

        if (conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            holder.tv_message.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(lastMessage, (context))),
                    TextView.BufferType.SPANNABLE);

            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        //设置自定义属性
//        holder.name.setTextColor(primaryColor);
//        holder.tv_message.setTextColor(secondaryColor);
//        holder.time.setTextColor(timeColor);
//        if (primarySize != 0)
//            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
//        if (secondarySize != 0)
//            holder.tv_message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
//        if (timeSize != 0)
//            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);
        return convertView;
    }

    public class ViewHolder {
        public ImageView img_head, new_message_indicator, msgState;
        public TextView unreadLabel, name, tv_message, time;

    }
}
