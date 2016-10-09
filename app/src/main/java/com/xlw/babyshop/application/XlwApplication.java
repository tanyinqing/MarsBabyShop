package com.xlw.babyshop.application;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tandong.sa.zUImageLoader.cache.disc.impl.UnlimitedDiscCache;
import com.tandong.sa.zUImageLoader.cache.disc.naming.Md5FileNameGenerator;
import com.tandong.sa.zUImageLoader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.ImageLoaderConfiguration;
import com.tandong.sa.zUImageLoader.core.assist.QueueProcessingType;
import com.tandong.sa.zUImageLoader.core.download.BaseImageDownloader;
import com.xlw.babyshop.service.IShopManager;
import com.xlw.babyshop.service.ShopServiceManager;
import com.xlw.babyshop.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinliwei on 2015/7/19.
 */
public class XlwApplication extends Application {

    private final String TAG = "XlwApplication.class";

    // 使用单例模式
    private static XlwApplication instance;
    public static XlwApplication getInstance(){
        return instance;
    }

    /* 构建一个全局的RequestQueue请求队列
       用于使用Volley请求网络数据
     */
    public RequestQueue requestQueue;

    /* 本就用需要缓存的地方主要是以下情形:
        1. 当查看旅行记忆时,ListView最好是先从缓存中查找照片,所以这里要应用缓存策略
        2. 查看照片画廊或照片干墙时,因为图片比较多,所以需要缓存照片,并使用缩略图显示
     */
    LruCache<String,Bitmap> lruCache;

    private ImageLoader imageLoader;    // SmartAndroid提供的ImageLoader对象

    private List<Activity> records = new ArrayList<Activity>();
    public String cacheDir;             // 缓存路径

    private IShopManager shopManager;   // 管理购物历史记录

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 获得图像加载器的单例
        imageLoader = ImageLoader.getInstance();

        bindService(new Intent(this, ShopServiceManager.class), new ShopServiceConnection(), Context.BIND_AUTO_CREATE);

        initVolleryRequestQueue();

        initCacheDirPath(); // 初始化缓存路径

        initImageLoaderConfig();

    }

    private void initVolleryRequestQueue(){
        requestQueue = Volley.newRequestQueue(this);
    }

    private void initCacheDirPath() {
        File f;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            f = new File(Environment.getExternalStorageDirectory() + "/.marsbaby/");
            if (!f.exists()) {
                f.mkdir();
            }
        } else {
            f = getApplicationContext().getCacheDir();
        }
        cacheDir = f.getAbsolutePath();
    }

    // 初始化ImageLoader
    private void initImageLoaderConfig(){
        // 首先需要配置全局的图片加载处理策略，当然你也可以单独为每一个用到的地方写一个配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
//                .memoryCacheExtraOptions(400, 300)      // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(4)          //    线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiscCache(new File(cacheDir)))      //自定义缓存路径
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())     //将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)    //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()   // Remove for release app
                .build();           //开始构建

        imageLoader.init(config);//全局初始化此配置
    }

    /**
     * Activity管理
     */
    public void addActvity(Activity activity) {
        records.add(activity);
        Logger.d(TAG, "Current Acitvity Size :" + getCurrentActivitySize());
    }

    public void removeActvity(Activity activity) {
        records.remove(activity);
        Logger.d(TAG, "Current Acitvity Size :" + getCurrentActivitySize());
    }

    public void exit() {
        for (Activity activity : records) {
            activity.finish();
        }
    }

    public int getCurrentActivitySize() {
        return records.size();
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public IShopManager getShopManager() {
        return shopManager;
    }

    private class ShopServiceConnection  implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d(TAG, "onServiceConnected");
            shopManager = (IShopManager) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
