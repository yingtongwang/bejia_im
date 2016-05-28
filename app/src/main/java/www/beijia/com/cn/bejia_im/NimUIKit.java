package www.beijia.com.cn.bejia_im;

import android.content.Context;
import android.util.Log;

import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.List;

import www.beijia.com.cn.bejia_im.business.uinfo.UserInfoHelper;
import www.beijia.com.cn.bejia_im.common.widget.ImageLoaderKit;
import www.beijia.com.cn.bejia_im.util.log.LogUtil;
import www.beijia.com.cn.bejia_im.util.storage.StorageType;
import www.beijia.com.cn.bejia_im.util.storage.StorageUtil;

/**
 * Created by Wangyingbao on 2016/5/19.
 */
public class NimUIKit {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        NimUIKit.context = context;
    }

    private static String account;

    // 图片加载、缓存与管理组件
    private static ImageLoaderKit imageLoaderKit;

    // 用户信息提供者
    private static UserInfoProvider userInfoProvider;

    /**
     * 设置当前登录用户的帐号
     *
     * @param account 帐号
     */
    public static void setAccount(String account) {
        NimUIKit.account = account;
    }

    public static ImageLoaderKit getImageLoaderKit() {
        return imageLoaderKit;
    }

    public static UserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        NimUIKit.context = context.getApplicationContext();
        NimUIKit.imageLoaderKit = new ImageLoaderKit(context, null);
        // init tools
        StorageUtil.init(context, null);
//        ScreenUtil.init(context);
//        StickerManager.getInstance().init();
        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);
    }

    /**
     * 当用户资料发生改动时，请调用此接口，通知更新UI
     *
     * @param accounts 有用户信息改动的帐号列表
     */
    public static void notifyUserInfoChanged(List<String> accounts) {
        UserInfoHelper.notifyChanged(accounts);
    }

    public static String getAccount() {
        return account;
    }
}
