package www.bejia.com.cn.demo.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import www.bejia.com.cn.demo.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/6/15.
 */
public abstract class TFragment extends Fragment {
    private static final Handler handler = new Handler();

    private boolean destroyed;

    protected <T extends View> T findView(int layoutId) {
        return (T) getView().findViewById(layoutId);
    }

    protected final boolean isDestroyed() {
        return destroyed;
    }

    public <T extends View> T findViewById(int viewID) {
        return (T) getView().findViewById(viewID);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);

        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onActivityCreated()");

        destroyed = false;

        initView();

        initData();
    }

    public abstract void initView();

    public abstract void initData();

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

    protected void showKeyboard(boolean isShow) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }
    }
}
