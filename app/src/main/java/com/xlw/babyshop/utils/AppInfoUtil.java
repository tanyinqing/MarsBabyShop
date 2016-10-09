package com.xlw.babyshop.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by xinliwei on 2015/7/17.
 */
public class AppInfoUtil {
    Context context;

    public AppInfoUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取当前应用的版本名称
     *
     * @return  当前版本名称
     * @throws PackageManager.NameNotFoundException
     */
    public String getClientVersion() throws PackageManager.NameNotFoundException {
        // PackageManager类的作用:获得已安装的应用程序信息
        PackageManager packageManager = context.getPackageManager();
        // 根据包名获得该包的信息
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        // 返回版本名称
        return packageInfo.versionName;
    }

    /**
     * 获取当前应用的版本号
     *
     * @return  当前版本号
     * @throws PackageManager.NameNotFoundException
     */
    public int getClientVersionCode() throws PackageManager.NameNotFoundException {
        // PackageManager类的作用:获得已安装的应用程序信息
        PackageManager packageManager = context.getPackageManager();
        // 根据包名获得该包的信息
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        // 返回版本名称
        return packageInfo.versionCode;
    }
}
