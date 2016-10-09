package com.xlw.babyshop.service;

import com.xlw.babyshop.model.ProdcutHistory;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/20.
 */
public interface IShopManager {
    void addProductToHistory(ProdcutHistory history);
    void clearProductHistory();
    List<ProdcutHistory> getAllProductHistory();
}
