package www.beijia.com.cn.bejia_im.cache;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import www.beijia.com.cn.bejia_im.NimUIKit;
import www.beijia.com.cn.bejia_im.util.key.UIKitLogTag;
import www.beijia.com.cn.bejia_im.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/5/19.
 */
public class NimUserInfoCache {
    // 重复请求处理
    private Map<String, List<RequestCallback<NimUserInfo>>> requestUserInfoMap = new ConcurrentHashMap<String, List<RequestCallback<NimUserInfo>>>();
    private Map<String, NimUserInfo> account2UserMap = new ConcurrentHashMap<String, NimUserInfo>();

    public static class InstanceHolder {
        final static NimUserInfoCache instance = new NimUserInfoCache();
    }

    public static NimUserInfoCache getInstance() {
        return InstanceHolder.instance;
    }

    public String getAlias(String account) {
//        Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
//        if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
//            return friend.getAlias();
//        }
        return null;
    }

    /**
     * 构建缓存与清理
     */
    public void buildCache() {
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();
        addOrUpdateUsers(users, false);
        LogUtil.i(UIKitLogTag.USER_CACHE, "build NimUserInfoCache completed, users count = " + account2UserMap.size());
    }

    /**
     * *************************************** User缓存管理与变更通知 ********************************************
     */

    private void addOrUpdateUsers(final List<NimUserInfo> users, boolean notify) {
        if (users == null || users.isEmpty()) {
            return;
        }

        // update cache
        for (NimUserInfo u : users) {
            account2UserMap.put(u.getAccount(), u);
        }

        // log
        List<String> accounts = getAccounts(users);
        DataCacheManager.Log(accounts, "on userInfo changed", UIKitLogTag.USER_CACHE);

        // 通知变更
        if (notify && accounts != null && !accounts.isEmpty()) {
            NimUIKit.notifyUserInfoChanged(accounts); // 通知到UI组件
        }
    }

    private List<String> getAccounts(List<NimUserInfo> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        List<String> accounts = new ArrayList<String>(users.size());
        for (NimUserInfo user : users) {
            accounts.add(user.getAccount());
        }

        return accounts;
    }

    public NimUserInfo getUserInfo(String account) {
        if (TextUtils.isEmpty(account) || account2UserMap == null) {
            LogUtil.e(UIKitLogTag.USER_CACHE, "getUserInfo null, account=" + account + ", account2UserMap=" + account2UserMap);
            return null;
        }

        return account2UserMap.get(account);
    }

    public void clear() {
        clearUserCache();
    }

    private void clearUserCache() {
        account2UserMap.clear();
    }

    /**
     * 从云信服务器获取用户信息（重复请求处理）[异步]
     */
    public void getUserInfoFromRemote(final String account, final RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        if (requestUserInfoMap.containsKey(account)) {
            if (callback != null) {
                requestUserInfoMap.get(account).add(callback);
            }
            return; // 已经在请求中，不要重复请求
        } else {
            List<RequestCallback<NimUserInfo>> cbs = new ArrayList<RequestCallback<NimUserInfo>>();
            if (callback != null) {
                cbs.add(callback);
            }
            requestUserInfoMap.put(account, cbs);
        }

        List<String> accounts = new ArrayList<String>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {

            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                NimUserInfo user = null;
                boolean hasCallback = requestUserInfoMap.get(account).size() > 0;
                if (code == ResponseCode.RES_SUCCESS && users != null && !users.isEmpty()) {
                    user = users.get(0);
                    // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                }

                // 处理回调
                if (hasCallback) {
                    List<RequestCallback<NimUserInfo>> cbs = requestUserInfoMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            cb.onSuccess(user);
                        } else {
                            cb.onFailed(code);
                        }
                    }
                }

                requestUserInfoMap.remove(account);
            }
        });
    }
}
