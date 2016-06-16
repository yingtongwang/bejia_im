package www.bejia.com.cn.demo.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import www.bejia.com.cn.demo.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/6/15.
 */
public class TFragment extends Fragment {
    private static final Handler handler = new Handler();

    private boolean destroyed;

    protected <T extends View> T findView(int layoutId) {
        return (T) getView().findViewById(layoutId);
    }

    protected final boolean isDestroyed() {
        return destroyed;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);

        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onActivityCreated()");

        destroyed = false;
    }

    public void onDestroy() {

        super.onDestroy();

        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onDestroy()");

        destroyed = true;
    }

    protected final Handler getHandler() {
        return handler;
    }

    protected final void postRunnable(final Runnable runnable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        });
    }

    protected final void postDelayed(final Runnable runnable, long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        }, delay);
    }
}
