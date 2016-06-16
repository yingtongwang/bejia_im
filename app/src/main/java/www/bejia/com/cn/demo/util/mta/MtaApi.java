package www.bejia.com.cn.demo.util.mta;

import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import www.bejia.com.cn.demo.activity.NimApplication;
import www.bejia.com.cn.demo.util.log.LogUtil;

/**
 * 腾讯云分析
 * 做一些mta的初始化工作。
 * Created by wangchao on 2015/11/16.
 */
public class MtaApi {
    private static final String TAG = "MtaApi";
    private static final MtaApi sMtaApi = new MtaApi();

    public static MtaApi getInstance() {
        return sMtaApi;
    }


    private MtaApi() {
        // androidManifest.xml指定本activity最先启动
        // 因此，MTA的初始化工作需要在本onCreate中进行
        // 在startStatService之前调用StatConfig配置类接口，使得MTA配置及时生效
//        StatConfig.setDebugEnable(true);
        StatConfig.setStatSendStrategy(StatReportStrategy.INSTANT);
        //bugly更好用。
        StatConfig.setAutoExceptionCaught(false);
        String appkey = "A4LWN4MP8S7Z";
        // 初始化并启动MTA
        // 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式或MTA分配的代码。
        // 其它普通的app可自行选择是否调用
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(NimApplication.getApplication(), appkey,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
//            logger.error("MTA start failed.");
//            logger.error("e");
            LogUtil.e(TAG, e.getMessage());
        }
    }
}
