package www.bejia.com.cn.demo.util.mta;

import android.content.Context;

import com.tencent.stat.StatAppMonitor;
import com.tencent.stat.StatService;

import java.util.Properties;

import www.bejia.com.cn.demo.activity.NimApplication;
import www.bejia.com.cn.demo.util.log.LogUtil;

/**
 * 网络接口添加上报步骤：
 * 1. http://mta.qq.com/mta/custom/ctr_event_conf?app_id=3101980559
 * 到该网址，“新增事件”添加一个id
 * 2. 该类中添加一个常量ID，值就是上面的事件id
 * 3. 调用ReportSuccess，把常量ID输进来啊。
 * <p/>
 * Created by wangchao on 2015/10/28.
 */
public class ReportManager {


    public static final int ERROR_CODE_DEFAULT = 1000;

    private static final ReportManager sReportManager = new ReportManager();
    private static final Context sContext = NimApplication.getApplication();
    private static final String TAG = "ReportManager";

    public static ReportManager getInstance() {
        return sReportManager;
    }

    private ReportManager() {
        MtaApi.getInstance();
    }

    public void reportClick(String id) {
        //mta
        Properties prop = new Properties();
        prop.setProperty("name", "ok"); // 活动页面
        StatService.trackCustomKVEvent(sContext, id, prop);

        //guoshuang
    }

    /**
     * @param id 后台定义的时间id
     */
    public void reportSuccess(String id) {
        Properties prop = new Properties();
        prop.setProperty("ErrorCode", "0"); // 活动页面
        StatService.trackCustomKVEvent(sContext, id, prop);
    }

    public void reportException(String msg, Throwable e) {
        LogUtil.e(TAG, msg, e);
//        StatService.reportError(sContext, msg);
        StatService.reportException(sContext, e);
    }

    /**
     * @param id
     * @param errorCode 必须大于0
     */
    public void reportFail(String id, int errorCode) {
        //上报错误，可以告警
        StatService.reportError(sContext, id);
        Properties prop = new Properties();
        prop.setProperty("ErrorCode", String.valueOf(errorCode)); // 活动页面
        StatService.trackCustomKVEvent(sContext, id, prop);
    }

    public void reportFail(String tag, String msg) {
        //上报错误，可以告警
        StatService.reportError(sContext, tag + ": " + msg);
        LogUtil.e(tag, msg);
    }

    /**
     * 不记录日志，只上报
     *
     * @param msg
     * @param e
     */
    public void reportExceptionOnly(String msg, Throwable e) {
        StatService.reportException(sContext, e);
    }
}


