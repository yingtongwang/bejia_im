package www.beijia.com.cn.bejia_im.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public class SysInfoUtil {
    /**
     * 程序是否在运行。
     *
     * @param context
     * @return
     */
    public static boolean stackResumed(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningTaskInfo> recentTaskInfos = manager.getRunningTasks(1);
        if (recentTaskInfos != null && recentTaskInfos.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = recentTaskInfos.get(0);
            if (taskInfo.baseActivity.getPackageName().equals(packageName) && taskInfo.numActivities > 1) {
                return true;
            }
        }
        return false;
    }
}
