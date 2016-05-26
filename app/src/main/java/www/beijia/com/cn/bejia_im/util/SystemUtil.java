package www.beijia.com.cn.bejia_im.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public class SystemUtil {
    /**
     * 获取当前进程
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        /**
         * 循环判断 寻找当前进程的Name，如果没有找到就暂停后 再次寻找.
         */
        while (true) {
            for (ActivityManager.RunningAppProcessInfo processErrorStateInfo : am.getRunningAppProcesses()) {
                if (processErrorStateInfo.pid == android.os.Process.myPid()) {
                    processName = processErrorStateInfo.processName;
                    break;
                }
            }

            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            try {
                Thread.sleep(100L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
