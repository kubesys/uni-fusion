package com.github.restapi.utils;

import com.github.restapi.XN_Rest;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 **/

@Slf4j
public class ContentUtils {

    /**
     * Content对象转化为Class对像
     * */
    public static void toObject(Content content, Object obj) {
        Field[] fields = obj.getClass().getFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            String fieldname = field.getName();
            try {
                switch (fieldname){
                    case "id":
                        field.set(obj,content.id);
                        break;
                    case "title":
                        field.set(obj,content.title);
                        break;
                    case "published":
                        field.set(obj,content.published);
                        break;
                    case "updated":
                        field.set(obj,content.updated);
                        break;
                    case "author":
                        field.set(obj,content.author);
                        break;
                    case "application":
                        field.set(obj,content.application);
                        break;
                    default:
                        if(field.getType().equals(String.class)){
                            field.set(obj,content.my.getOrDefault(fieldname,""));
                        }else if(field.getType().equals(int.class) || field.getType().equals(Integer.class)){
                            field.set(obj,Integer.parseInt(content.my.getOrDefault(fieldname,0).toString(),10));
                        }else if(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)){
                            field.set(obj, Integer.parseInt(content.my.getOrDefault(fieldname, 0).toString(), 10) != 0);
                        }else if(field.getType().equals(List.class)) {
                            Object fv = content.my.getOrDefault(fieldname,null);
                            if (!isEmpty(fv)){
                                if(fv instanceof String){
                                    field.set(obj,new ArrayList<>(ImmutableSet.of(fv)));
                                }else {
                                    field.set(obj, fv);
                                }
                            }
                        }else if(field.getType().equals(long.class) || field.getType().equals(Long.class)){
                            field.set(obj,Long.parseLong(content.my.getOrDefault(fieldname,0).toString()));
                        } else {
                            field.set(obj,content.my.getOrDefault(fieldname,""));
                        }
                        break;
                }
            } catch(Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Class对像转化为Content对象
     * */
    public static void fromObject(Content content, Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field: fields){
            String fieldname = field.getName();
            Object fieldtype = field.getType();
            try {
                if (fieldtype.equals(Boolean.class) || fieldtype.equals(boolean.class)) {
                    content.add(fieldname, field.get(obj).equals(true) ? "1" : "0");
                }else if(fieldtype.equals(List.class)) {
                    if(isEmpty(field.get(obj))){
                        content.add(fieldname,"");
                    } else {
                        content.add(fieldname, field.get(obj));
                    }
                }else {
                    content.add(fieldname, field.get(obj));
                }
            }catch (Exception ignored) {}
        }
        if (content.application.isEmpty()) {
            content.application = XN_Rest.getApplication();
        }
    }

    /**
     * Class对象转化为Json字符串
     * */
    public static String toJsonString(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Field[] fields = obj.getClass().getFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            String fieldname = field.getName();
            String fieldtype = field.getType().toString();
            if (i != 0) {
                sb.append(",");
            }
            try {
                if ("class java.lang.String".equals(fieldtype)) {
                    sb.append("\"").append(fieldname).append("\":\"").append(field.get(obj).toString()).append("\"");
                } else if ("int".equals(fieldtype)) {
                    sb.append("\"").append(fieldname).append("\":").append(field.get(obj).toString());
                } else if ("long".equals(fieldtype)) {
                    sb.append("\"" + fieldname + "\":" + field.get(obj).toString() + "");
                } else if ("interface java.util.List".equals(fieldtype)) {
                    sb.append("\"").append(fieldname).append("\":").append(String.join(",", (List) field.get(obj)));
                }else {
                    sb.append("\"").append(fieldname).append("\":\"").append(field.get(obj).toString()).append("\"");
                }
            } catch(Exception ignored) {}
        }
        sb.append("}");
        return sb.toString();
    }

    public static boolean isEmpty(Object object) {
        if(object == null) {
            return true;
        }
        if(object instanceof String){
            if(object.toString().isEmpty()) {
                return true;
            }
        }else if(object instanceof List){
            if(((List)object).isEmpty()) {
                return true;
            }
        }else if(object instanceof Map) {
            if(((Map)object).isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
