package com.qnkj.core.base.modules.settings.supplier.utils;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.services.ISupplierPickListServices;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.supplier.entitys.Supplier;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.configure.WebConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class SupplierUtils {

    @Autowired
    public ISupplierService supplierservice;

    @Autowired
    public ISupplierPickListServices supplierPickListServices;

    private static SupplierUtils supplierutils;

    @PostConstruct
    public void init() {
        supplierutils = this;
        supplierutils.supplierservice = this.supplierservice;
        supplierutils.supplierPickListServices = this.supplierPickListServices;
    }
    public static String getSupplierid() {
        try {
            Supplier result = getSupplierInfo();
            return result != null ? result.id : "0";
        } catch(Exception ignored) {}
        return "0";
    }

    public static String getSupplierid(String profileid) {
        try {
            return supplierutils.supplierservice.getSupplierid(profileid);
        } catch(Exception ignored) {}
        return "0";
    }

    public static String getSupplierName() {
        try {
            Supplier result = getSupplierInfo();
            return result != null ? result.suppliers_name : "";
        } catch(Exception ignored) {}
        return "";
    }


    public static Supplier getSupplierInfo() throws Exception {
        Profile profile = ProfileUtils.getCurrentUser();
        if ("supplier".equals(profile.type)) {
            Object supplierInRedis = RedisUtils.get(WebConstant.SUPPLIER_PREFIX + profile.id);
            if (supplierInRedis != null) {
                return new Supplier(supplierInRedis.toString());
            } else {
                List<Object> users = XN_Query.contentQuery().tag("supplier_users")
                        .filter("type", "eic", "supplier_users")
                        .filter("my.profileid", "=", profile.id)
                        .begin(0).notDelete()
                        .end(1)
                        .execute();
                if (users.isEmpty()) {
                    throw new Exception("该用户不是企业用户");
                }
                Content user_info = (Content)users.get(0);
                String supplierid = user_info.my.get("supplierid").toString();
                if(!Utils.isEmpty(supplierid)) {
                    Content supplier_info = XN_Content.load(supplierid, "suppliers");
                    Supplier supplier = new Supplier(supplier_info);
                    RedisUtils.set(WebConstant.SUPPLIER_PREFIX + profile.id, supplier.toString());
                    return supplier;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static void initPickLists() {
        String domain = BaseSaasConfig.getDomain();
        ExecutorService pool = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        pool.execute(() -> {
            BaseSaasConfig.existDomain(domain);
            supplierutils.supplierPickListServices.init(getSupplierid());
        });
        pool.shutdown();
    }
}
