package www.beijia.com.cn.bejia_im;

import android.content.Context;

import www.beijia.com.cn.bejia_im.common.widget.ImageLoaderKit;

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

    /**
     * 设置当前登录用户的帐号
     *
     * @param account 帐号
     */
    public static void setAccount(String account) {
        NimUIKit.account = account;
    }

    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        NimUIKit.context = context.getApplicationContext();
        NimUIKit.imageLoaderKit = new ImageLoaderKit(context, null);
    }
}
