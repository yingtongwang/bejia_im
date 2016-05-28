package www.beijia.com.cn.bejia_im.common.framework;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import www.beijia.com.cn.bejia_im.NimUIKit;

/**
 * Created by Wangyingbao on 2016/5/28.
 */
public class SingleThreadExecutor {
    private static SingleThreadExecutor instance;

    private Handler handler;

    private Executor executor;

    public SingleThreadExecutor() {
        handler = new Handler(NimUIKit.getContext().getMainLooper());
        executor = Executors.newSingleThreadExecutor();
    }

    public synchronized static SingleThreadExecutor getInstance() {
        if (instance == null) {
            instance = new SingleThreadExecutor();
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        if (executor != null) {
            executor.execute(runnable);
        }
    }
}
