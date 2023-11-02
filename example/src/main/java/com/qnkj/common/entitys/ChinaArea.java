package com.qnkj.common.entitys;

/**
* @author oldhand
* @date 2019-12-25
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChinaArea {
    private  String code;
    private  String name;
    private  String level;
    private  String parent;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"code\":\"").append(code).append("\"");
        sb.append(",\"name\":\"").append(name).append("\"");
        sb.append(",\"level\":\"").append(level).append("\"");
        sb.append(",\"parent\":\"").append(parent).append("\"");
        sb.append("}");
        return sb.toString();
    }
}