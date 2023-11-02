package com.qnkj.core.base.modules.settings.importdata.Util;

public class SortObject implements Comparable<SortObject> {
    private String code;
    private String name;
    private String align;
    private String uitype;
    private String picklist;
    private int sequence;
    private String bitian;
    private String congfuchk;
    private String mrz;
    private String link;
    private String typeofdata;

    public SortObject(String code, String name, String align, String uitype, String picklist, int sequence, String bitian, String congfuchk, String mrz, String link, String typeofdata) {
        this.code = code;
        this.name = name;
        this.align = align;
        this.uitype = uitype;
        this.picklist = picklist;
        this.sequence = sequence;
        this.bitian = bitian;
        this.congfuchk = congfuchk;
        this.mrz = mrz;
        this.link = link;
        this.typeofdata = typeofdata;
    }

    // getter && setter
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAlign() {
        return align;
    }
    public void setAlign(String align) {
        this.align = align;
    }
    public String getUitype() {
        return uitype;
    }
    public void setUitype(String uitype) {
        this.uitype = uitype;
    }
    public String getPicklist() {
        return picklist;
    }
    public void setPicklist(String picklist) {
        this.picklist = picklist;
    }
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public String getBitian() {
        return bitian;
    }
    public void setBitian(String bitian) {
        this.bitian = bitian;
    }
    public String getCongfuchk() {
        return congfuchk;
    }
    public void setCongfuchk(String congfuchk) {
        this.congfuchk = congfuchk;
    }
    public String getMrz() {
        return mrz;
    }
    public void setMrz(String mrz) {
        this.mrz = mrz;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getTypeofdata() {
        return typeofdata;
    }
    public void setTypeofdata(String typeofdata) {
        this.typeofdata = typeofdata;
    }
    @Override
    public String toString() {
        return "SortObject [name=" + name + ",align=" + align + "uitype=" + uitype + ", sequence=" + sequence + "]";
    }

    @Override
    public int compareTo(SortObject sortObject) {//重写Comparable接口的compareTo方法，
        return this.sequence - sortObject.getSequence();//根据升序排列，降序修改相减顺序即可
    }
}
