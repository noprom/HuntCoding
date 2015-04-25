package com.huntdreams.coding;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.huntdreams.coding.common.PhoneType;
import com.huntdreams.coding.common.Unread;
import com.huntdreams.coding.model.AccountInfo;
import com.huntdreams.coding.model.UserObject;

import java.util.List;

/**
 * 全局自定义Application
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
public class MyApp extends Application {

    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;
    public static int sHeightPix;

    public static int sEmojiNormal;
    public static int sEmojiMonkey;

    public static UserObject sUserObject;
    public static Unread sUnread;

    public static boolean sMainCreate = false;

    public static void setMainActivityState(boolean create) {
        sMainCreate = create;
    }

    public static boolean getMainActivityState() {
        return sMainCreate;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        initImageLoader(this);

        if (!PhoneType.isX86()) {
            // x86的机器上会抛异常，因为百度没有提供x86的.so文件
            // 只在主进程初始化lbs
            if (this.getPackageName().equals(getProcessName(this))) {
                SDKInitializer.initialize(this);
            }
        }

        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;
        sWidthDp = (int) (sWidthPix / sScale);

        sEmojiNormal = getResources().getDimensionPixelSize(R.dimen.emoji_normal);
        sEmojiMonkey = getResources().getDimensionPixelSize(R.dimen.emoji_monkey);

        sUserObject = AccountInfo.loadAccount(this);
        sUnread = new Unread();
    }

//    public static void initImageLoader(Context context) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
//                .diskCacheFileCount(300)
//                .imageDownloader(new MyImageDownloader(context))
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
////                .writeDebugLogs() // Remove for release app
//                .diskCacheExtraOptions(sWidthPix / 3, sWidthPix / 3, null)
//                .build();
//
//        ImageLoader.getInstance().init(config);
//    }


    private static String getProcessName(Context context) {
        ActivityManager actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = actMgr.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appList) {
            if (info.pid == android.os.Process.myPid()) {
                return info.processName;
            }
        }
        return "";
    }
}
