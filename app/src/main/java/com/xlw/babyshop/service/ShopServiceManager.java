package com.xlw.babyshop.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.xlw.babyshop.dao.ProductDao;
import com.xlw.babyshop.model.ProdcutHistory;
import com.xlw.babyshop.utils.Logger;

import java.util.List;

public class ShopServiceManager extends Service {

    private MyShopCManager myShopCManager;
    private ProductDao productHistroyDao;
    private static final String TAG = "ShopServiceManager";

    public ShopServiceManager() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myShopCManager = new MyShopCManager();
        productHistroyDao = new ProductDao(this);

        Logger.d(TAG, "ShopServiceManager is start");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(TAG, "onBind ");
        return myShopCManager;
    }

    private class MyShopCManager extends Binder implements IShopManager {

        @Override
        public void addProductToHistory(ProdcutHistory history) {
            Logger.d(TAG, "addProductToHistory" + history.toString());

            if (productHistroyDao.findById(history.getId()))
                productHistroyDao.update(history);
            else
                productHistroyDao.add(history);
        }

        @Override
        public void clearProductHistory() {
            Logger.d(TAG, "clearProductHistory");
            productHistroyDao.deleteAll();
        }

        @Override
        public List<ProdcutHistory> getAllProductHistory() {
            Logger.d(TAG, "getAllProductHistory");
            return productHistroyDao.getAll();
        }
    }
}
