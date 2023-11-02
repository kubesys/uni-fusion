package com.qnkj.common.entitys;

/**
* @author oldhand
* @date 2019-12-25
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BankInfo {
    private  String bankName;
    private  String cnapsCode;
    private  String cipsCode;
    private  String cityCode;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"bankName\":\"").append(bankName).append("\"");
        sb.append(",\"cnapsCode\":\"").append(cnapsCode).append("\"");
        sb.append(",\"cipsCode\":\"").append(cipsCode).append("\"");
        sb.append(",\"cityCode\":\"").append(cityCode).append("\"");
        sb.append("}");
        return sb.toString();
    }
}