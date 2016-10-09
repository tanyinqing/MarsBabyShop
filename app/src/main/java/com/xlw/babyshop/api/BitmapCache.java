package com.xlw.babyshop.api;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.tandong.sa.vl.toolbox.ImageLoader;

/**
 * Created by xinliwei on 2015/7/18.
 *
 * 对图片进行防OOM处理, 位图缓存
 */
public class BitmapCache implements ImageLoader.ImageCache{

    private LruCache<String,Bitmap> mCache;

    public BitmapCache(){
        int maxSize = 10*1024*1024;
        mCache = new LruCache<String, Bitmap>(maxSize){
            protected int sizeOf(String key,Bitmap bitmap){
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url,bitmap);
    }
}
