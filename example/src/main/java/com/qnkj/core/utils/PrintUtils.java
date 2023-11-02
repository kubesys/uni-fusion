package com.qnkj.core.utils;


import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PrintUtils {

    //是否有打印配置
    public static Boolean hasPrint(String module) {
        try{
            XN_Query query = XN_Query.contentQuery().tag("invoice_print")
                    .filter("type","eic","invoice_print")
                    .notDelete()
                    .filter("my.printmodule","=",module)
                    .end(1);
            if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
                query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
            } else {
                query.filter( "my.supplierid", "=", "0");
            }
            List<Object> list = query.execute();
            if (!list.isEmpty()) {
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return false;
    }

    public static String getPrintConfig(String module) {
        try{
            XN_Query query = XN_Query.contentQuery().tag("invoice_print")
                    .filter("type","eic","invoice_print")
                    .notDelete()
                    .filter("my.printmodule","=",module)
                    .end(1);
            if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
                query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
            } else {
                query.filter( "my.supplierid", "=", "0");
            }
            List<Object> list = query.execute();
            if (!list.isEmpty())  {
                Content invoicePrint = (Content)list.get(0);
                return invoicePrint.id;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "";
    }
    public static String getPrintConfig(String module,String key) {
        try{
            XN_Query query = XN_Query.contentQuery().tag("invoice_print")
                    .filter("type","eic","invoice_print")
                    .notDelete()
                    .filter("my.printmodule","=",module)
                    .filter("my.printkeyname","=",key)
                    .end(1);
            if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
                query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
            } else {
                query.filter( "my.supplierid", "=", "0");
            }
            List<Object> list = query.execute();
            if (!list.isEmpty())  {
                Content invoicePrint = (Content)list.get(0);
                return invoicePrint.id;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "";
    }
}
