/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package www.bejia.com.cn.demo.activity.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import www.bejia.com.cn.demo.bean.InviteMessage;
import www.bejia.com.cn.demo.db.DBManager;

public class InviteMessgeDao {
    public static final String TABLE_NAME = "new_friends_msgs";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_FROM = "username";
    public static final String COLUMN_NAME_GROUP_ID = "groupid";
    public static final String COLUMN_NAME_GROUP_Name = "groupname";

    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_REASON = "reason";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
    public static final String COLUMN_NAME_GROUPINVITER = "groupinviter";

    public static final String COLUMN_NAME_UNREAD_MSG_COUNT = "unreadMsgCount";


    public InviteMessgeDao(Context context) {
    }

    /**
     * 保存message
     *
     * @param message
     * @return 返回这条messaged在db中的id
     */
    public Integer saveMessage(InviteMessage message) {
        return DBManager.getInstance().saveMessage(message);
    }

    /**
     * 更新message
     *
     * @param msgId
     * @param values
     */
    public void updateMessage(int msgId, ContentValues values) {
        DBManager.getInstance().updateMessage(msgId, values);
    }

    /**
     * 获取messges
     *
     * @return
     */
    public List<InviteMessage> getMessagesList() {
        return DBManager.getInstance().getMessagesList();
    }

    public void deleteMessage(String from) {
        DBManager.getInstance().deleteMessage(from);
    }

    public int getUnreadMessagesCount() {
        return DBManager.getInstance().getUnreadNotifyCount();
    }

    public void saveUnreadMessageCount(int count) {
        DBManager.getInstance().setUnreadNotifyCount(count);
    }
}
