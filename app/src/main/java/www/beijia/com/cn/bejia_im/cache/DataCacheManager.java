package www.beijia.com.cn.bejia_im.cache;

import android.content.Context;
import android.os.Handler;

import com.netease.nimlib.sdk.Observer;

import java.util.ArrayList;
import java.util.List;

import www.beijia.com.cn.bejia_im.NimUIKit;
import www.beijia.com.cn.bejia_im.common.framework.SingleThreadExecutor;
import www.beijia.com.cn.bejia_im.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/5/28.
 */
public class DataCacheManager {
    private static final String TAG = "DataCacheManager";

    /**
     * 本地缓存构建(异步)
     */
    public static void buildDataCacheAsync() {
        buildDataCacheAsync(null, null);
    }

    /**
     * 本地缓存构建(异步)
     */
    public static void buildDataCacheAsync(final Context context, final Observer<Void> buildCompletedObserver) {
        SingleThreadExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                buildDataCache();

                // callback
                if (context != null && buildCompletedObserver != null) {
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            buildCompletedObserver.onEvent(null);
                        }
                    });
                }

                LogUtil.i(TAG, "build data cache completed");
            }
        });
    }

    /**
     * 输出缓存数据变更日志
     */
    public static void Log(List<String> accounts, String event, String logTag) {
        StringBuilder sb = new StringBuilder();
        sb.append(event);
        sb.append(" : ");
        for (String account : accounts) {
            sb.append(account);
            sb.append(" ");
        }
        sb.append(", total size=" + accounts.size());

        LogUtil.i(logTag, sb.toString());
    }

    /**
     * 清空缓存（同步）
     */
    public static void clearDataCache() {
        // clear user/friend/team data cache
        FriendDataCache.getInstance().clear();
        NimUserInfoCache.getInstance().clear();
        TeamDataCache.getInstance().clear();

        // clear avatar cache
        NimUIKit.getImageLoaderKit().clear();
    }

    /**
     * 本地缓存构建（同步）
     */
    public static void buildDataCache() {
        // clear
        clearDataCache();

        // build user/friend/team data cache
        FriendDataCache.getInstance().buildCache();
        NimUserInfoCache.getInstance().buildCache();
        TeamDataCache.getInstance().buildCache();

        // build self avatar cache
        List<String> accounts = new ArrayList<String>(1);
        accounts.add(NimUIKit.getAccount());
        NimUIKit.getImageLoaderKit().buildAvatarCache(accounts);
    }
}
