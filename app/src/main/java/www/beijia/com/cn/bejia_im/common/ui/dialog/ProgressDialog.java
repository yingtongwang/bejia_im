package www.beijia.com.cn.bejia_im.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import www.beijia.com.cn.bejia_im.R;

/**
 * Created by Wangyingbao on 2016/5/28. 自定义Dialog
 */
public class ProgressDialog extends Dialog {

    private String mMessage;

    private int mLayoutId;

    private TextView message;

    private com.rey.material.widget.ProgressView progressView;


    public ProgressDialog(Context context, int style, int layout) {
        super(context, style);
        mLayoutId = layout;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
        layoutParams.height = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes(layoutParams);
    }

    public ProgressDialog(Context context, int layout, String msg) {
        this(context, R.style.easy_dialog_style, layout);
        setMessage(msg);

    }

    public ProgressDialog(Context context, String msg) {
        this(context, R.style.easy_dialog_style, R.layout.dialog_easy_progress);
        setMessage(msg);
    }

    public void updateLoadingMessage(String msg) {
        mMessage = msg;
        showMessage();
    }

    public void setMessage(String msg) {
        mMessage = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutId);
        progressView = (com.rey.material.widget.ProgressView) findViewById(R.id.easy_progress_bar);
        message = (TextView) findViewById(R.id.easy_progress_dialog_message);
        showMessage();
        progressView.start();
    }

    protected void showMessage() {
        if (message != null && !TextUtils.isEmpty(mMessage)) {
            message.setVisibility(View.VISIBLE);
            message.setText(mMessage);
        }
    }
}
