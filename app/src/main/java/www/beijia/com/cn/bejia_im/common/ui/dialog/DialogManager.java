package www.beijia.com.cn.bejia_im.common.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import www.beijia.com.cn.bejia_im.util.log.LogUtil;

/**
 * Created by Wangyingbao on 2016/5/28. Dialog管理
 */
public class DialogManager {
    private static ProgressDialog bejiaProgressDialog;

    public static ProgressDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context, null, message, true, null);
    }

    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        return showProgressDialog(context, null, message, cancelable, null);
    }

    @Deprecated
    public static ProgressDialog showProgressDialog(Context context, String title, String message, boolean canCancelable, DialogInterface
            .OnCancelListener listener) {
        if (bejiaProgressDialog == null) {
            bejiaProgressDialog = new ProgressDialog(context, message);
        } else if (bejiaProgressDialog.getContext() != context) {
            LogUtil.e("dialog", "there is a leaked window here,orign context: "
                    + bejiaProgressDialog.getContext() + " now: " + context);
            dismissProgressDialog();
            bejiaProgressDialog = new ProgressDialog(context, message);
        }
        bejiaProgressDialog.setCancelable(canCancelable);
        bejiaProgressDialog.setOnCancelListener(listener);
        bejiaProgressDialog.show();
        return bejiaProgressDialog;
    }

    public static void dismissProgressDialog() {
        if (bejiaProgressDialog == null) {
            return;
        }
        if (bejiaProgressDialog.isShowing()) {
            try {
                bejiaProgressDialog.dismiss();
                bejiaProgressDialog = null;
            } catch (Exception e) {

            }
        }
    }


    public static void setMessage(String message) {
        if (null != bejiaProgressDialog && bejiaProgressDialog.isShowing()
                && !TextUtils.isEmpty(message)) {
            bejiaProgressDialog.setMessage(message);
        }
    }

    public static void updateLoadingMessage(String message) {
        if (null != bejiaProgressDialog && bejiaProgressDialog.isShowing()
                && !TextUtils.isEmpty(message)) {
            bejiaProgressDialog.updateLoadingMessage(message);
        }
    }

    public static boolean isShowing() {
        return (bejiaProgressDialog != null && bejiaProgressDialog.isShowing());
    }
}
