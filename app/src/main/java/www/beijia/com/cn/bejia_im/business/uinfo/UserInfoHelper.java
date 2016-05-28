package www.beijia.com.cn.bejia_im.business.uinfo;

import java.util.List;

import www.beijia.com.cn.bejia_im.NimUIKit;

/**
 * Created by Wangyingbao on 2016/5/28.
 */
public class UserInfoHelper {
    private static UserInfoObservable userInfoObservable;

    /**
     * 当用户资料发生改动时，请调用此接口，通知更新UI
     *
     * @param accounts 有用户信息改动的帐号列表
     */
    public static void notifyChanged(List<String> accounts) {
        if (userInfoObservable != null) {
            userInfoObservable.notifyObservers(accounts);
        }
    }

    /**
     * 注册用户资料变化观察者。<br>
     * 注意：不再观察时(如Activity destroy后)，要unregister，否则会造成资源泄露
     *
     * @param observer 观察者
     */
    public static void registerObserver(UserInfoObservable.UserInfoObserver observer) {
        if (userInfoObservable == null) {
            userInfoObservable = new UserInfoObservable(NimUIKit.getContext());
        }
        userInfoObservable.registerObserver(observer);
    }

    /**
     * 注销用户资料变化观察者。
     *
     * @param observer 观察者
     */
    public static void unregisterObserver(UserInfoObservable.UserInfoObserver observer) {
        if (userInfoObservable != null) {
            userInfoObservable.unregisterObserver(observer);
        }
    }
}
