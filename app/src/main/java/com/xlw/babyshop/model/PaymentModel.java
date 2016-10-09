package com.xlw.babyshop.model;

/**
 * 支付方式
 *
 *  支付类型，1=>货到付款 2=>货到POS机    3=>支付宝(待定)
 */
public class PaymentModel {

    private int type;

    public PaymentModel() {
    }

    public PaymentModel(int type) {
        super();
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
