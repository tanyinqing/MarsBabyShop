package com.xlw.babyshop.model;

/**
 * 账户中心 UserInfo 用户信息
 */
public class UserModel {
    private int userId;             // 会员ID
    private int bonus;              // 会员积分
    private String level;           // 会员等级
    private String usersession;     // session MD5
    private int ordercount;         // 所下订单数
    private int favoritescount;     // 收藏总数

    public UserModel() {
    }

    public UserModel(int userId, int bonus, String level, String usersession, int ordercount, int favoritescount) {
        super();
        this.userId = userId;
        this.bonus = bonus;
        this.level = level;
        this.usersession = usersession;
        this.ordercount = ordercount;
        this.favoritescount = favoritescount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUsersession() {
        return usersession;
    }

    public void setUsersession(String usersession) {
        this.usersession = usersession;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public int getFavoritescount() {
        return favoritescount;
    }

    public void setFavoritescount(int favoritescount) {
        this.favoritescount = favoritescount;
    }
}
