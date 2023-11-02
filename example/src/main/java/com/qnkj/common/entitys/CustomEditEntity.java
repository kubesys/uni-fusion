package com.qnkj.common.entitys;

/**
 * create by 徐雁
 * create date 2021/7/7
 */

public class CustomEditEntity {
    public Integer uitype;
    public Object onevalue;
    public Object twovalue;
    public Object threevalue;
    public Object fourvalue;

    public CustomEditEntity uitype(Integer uitype) {
        this.uitype = uitype;
        return this;
    }
    public CustomEditEntity one(Object one) {
        this.onevalue = one;
        return this;
    }
    public CustomEditEntity two(Object twovalue) {
        this.twovalue = twovalue;
        return this;
    }
    public CustomEditEntity three(Object threevalue) {
        this.threevalue = threevalue;
        return this;
    }
    public CustomEditEntity four(Object fourvalue) {
        this.fourvalue = fourvalue;
        return this;
    }

}
