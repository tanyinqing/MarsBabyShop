package com.xlw.babyshop.model;

import android.location.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinliwei on 2015/7/20.
 */
public class CheckoutModel {

    /** 地址信息 */
    private Address address_info;

    /** 支付方式 */
    private PaymentModel payment_info;

    /** 送货时间 */
    private DeliveryModel delivery_info;

    /** 发票信息 */
    private InvoiceInfoModel invoice_info;

    /** 商品列表 */
    private List<CartProductModel> productlist = new ArrayList<>();

    /** 享受促销信息 */
    private List<String> checkout_prom = new ArrayList<String>();

    /** 总计 */
    private CheckoutAddupModel checkout_addup;

    public CheckoutModel() {
    }

    public CheckoutModel(Address address_info, PaymentModel payment_info, DeliveryModel delivery_info, InvoiceInfoModel invoice_info,
                    List<CartProductModel> productlist, List<String> checkout_prom, CheckoutAddupModel checkout_addup) {
        super();
        this.address_info = address_info;
        this.payment_info = payment_info;
        this.delivery_info = delivery_info;
        this.invoice_info = invoice_info;
        this.productlist = productlist;
        this.checkout_prom = checkout_prom;
        this.checkout_addup = checkout_addup;
    }

    public Address getAddress_info() {
        return address_info;
    }

    public void setAddress_info(Address address_info) {
        this.address_info = address_info;
    }

    public PaymentModel getPayment_info() {
        return payment_info;
    }

    public void setPayment_info(PaymentModel payment_info) {
        this.payment_info = payment_info;
    }

    public DeliveryModel getDelivery_info() {
        return delivery_info;
    }

    public void setDelivery_info(DeliveryModel delivery_info) {
        this.delivery_info = delivery_info;
    }

    public InvoiceInfoModel getInvoice_info() {
        return invoice_info;
    }

    public void setInvoice_info(InvoiceInfoModel invoice_info) {
        this.invoice_info = invoice_info;
    }

    public List<CartProductModel> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<CartProductModel> productlist) {
        this.productlist = productlist;
    }

    public List<String> getCheckout_prom() {
        return checkout_prom;
    }

    public void setCheckout_prom(List<String> checkout_prom) {
        this.checkout_prom = checkout_prom;
    }

    public CheckoutAddupModel getCheckout_addup() {
        return checkout_addup;
    }

    public void setCheckout_addup(CheckoutAddupModel checkout_addup) {
        this.checkout_addup = checkout_addup;
    }
}
