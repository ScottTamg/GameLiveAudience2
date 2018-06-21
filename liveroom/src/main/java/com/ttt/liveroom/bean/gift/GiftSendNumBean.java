package com.ttt.liveroom.bean.gift;

/**
 * Created by mrliu on 2018/6/7.
 * 此类用于:
 */

public class GiftSendNumBean {

    private String num ;
    private String name ;

    public GiftSendNumBean(String num, String name) {
        this.num = num;
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
