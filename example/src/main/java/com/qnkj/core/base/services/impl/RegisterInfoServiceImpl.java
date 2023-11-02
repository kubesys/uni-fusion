package com.qnkj.core.base.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.core.base.entitys.RegisterInfo;
import com.qnkj.core.base.modules.settings.supplier.entitys.Supplier;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
import com.qnkj.core.base.services.IRegisterInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oldhand
 */
@Slf4j
@Service
public class RegisterInfoServiceImpl implements IRegisterInfo {

    private final ISupplierService supplierService;

    public RegisterInfoServiceImpl(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public void update(RegisterInfo info) {
        try {
            XN_Query query = XN_Query.create ( "Content" ).tag("suppliers")
                    .filter( "type", "eic", "suppliers" )
                    .filter ("my.deleted", "=",2)
                    .filter ("my.mobile", "=",info.mobile)
                    .end(1);
            List<Object> intendedsuppliers = query.execute();
            if (intendedsuppliers.isEmpty() ) {
                query = XN_Query.create ( "Content" ).tag("suppliers")
                        .filter( "type", "eic", "suppliers" )
                        .filter ("my.deleted", "=",0)
                        .filter ("my.approvalstatus", "!=",2)
                        .filter ("my.mobile", "=",info.mobile)
                        .end(1);
                intendedsuppliers = query.execute();
            }
            if (intendedsuppliers.isEmpty() ) {
                Content newcontent = XN_Content.create("suppliers","","");
                newcontent.add("deleted","2");
                newcontent.add("username",info.username);
                newcontent.add("regioncode",info.regioncode);
                newcontent.add("mobile",info.mobile);
                newcontent.add("password",info.password);
                newcontent.add("givenname",info.givenname);
                newcontent.add("email",info.email);
                newcontent.add("system",info.system);
                newcontent.add("browser",info.browser);
                newcontent.add("regip",info.regip);
                newcontent.add("province",info.province);
                newcontent.add("city",info.city);
                newcontent.add("suppliers_name",info.suppliers_name);
                newcontent.add("legal_person_name",info.legal_person_name);
                newcontent.add("business_license",info.business_license);
                newcontent.add("business_license_img_url",info.business_license_img_url);
                newcontent.add("legal_person_identity_card",info.legal_person_identity_card);
                newcontent.add("legal_person_certificate_img_url",info.legal_person_certificate_img_url);
                newcontent.add("legal_person_certificate_reverse_img_url",info.legal_person_certificate_reverse_img_url);
                newcontent.save("suppliers");
            } else {
                Content register_info = (Content)intendedsuppliers.get(0);
                register_info.my.put("username", info.username);
                register_info.my.put("regioncode",info.regioncode);
                register_info.my.put("mobile",info.mobile);
                register_info.my.put("password",info.password);
                register_info.my.put("givenname",info.givenname);
                register_info.my.put("email",info.email);
                register_info.my.put("system",info.system);
                register_info.my.put("browser",info.browser);
                register_info.my.put("regip", info.regip);
                register_info.my.put("province",info.province);
                register_info.my.put("city",info.city);
                register_info.my.put("suppliers_name",info.suppliers_name);
                register_info.my.put("legal_person_name",info.legal_person_name);
                register_info.my.put("business_license",info.business_license);
                register_info.my.put("business_license_img_url",info.business_license_img_url);
                register_info.my.put("legal_person_identity_card",info.legal_person_identity_card);
                register_info.my.put("legal_person_certificate_img_url",info.legal_person_certificate_img_url);
                register_info.my.put("legal_person_certificate_reverse_img_url",info.legal_person_certificate_reverse_img_url);
                register_info.save("suppliers");
            }
        } catch(Exception ignored) {

        }
    }

    @Override
    public RegisterInfo get(String mobile) throws Exception {
        try {
            XN_Query query = XN_Query.create ( "Content" ).tag("suppliers")
                    .filter( "type", "eic", "suppliers" )
                    .filter ("my.deleted", "=",0)
                    .filter ("my.approvalstatus", "!=",2)
                    .filter ("my.mobile", "=",mobile)
                    .end(1);
            List<Object> suppliers = query.execute();
            if (suppliers.isEmpty()) {
                query = XN_Query.create ( "Content" ).tag("suppliers")
                        .filter( "type", "eic", "suppliers" )
                        .filter ("my.deleted", "=",2)
                        .filter ("my.mobile", "=",mobile)
                        .end(1);
                suppliers = query.execute();
                if (suppliers.isEmpty()) {
                    throw new Exception("no RegisterInfo");
                }
            }
            Content supplier_info = (Content)suppliers.get(0);
            RegisterInfo register_info = new RegisterInfo();
            register_info.id = supplier_info.id;
            if (supplier_info.my.get("username") != null ) {
                register_info.username = supplier_info.my.get("username").toString();
            }
            if (supplier_info.my.get("regioncode") != null ) {
                register_info.regioncode = supplier_info.my.get("regioncode").toString();
            }
            if (supplier_info.my.get("mobile") != null ) {
                register_info.mobile = supplier_info.my.get("mobile").toString();
            }
            if (supplier_info.my.get("password") != null ) {
                register_info.password = supplier_info.my.get("password").toString();
            }
            if (supplier_info.my.get("givenname") != null ) {
                register_info.givenname = supplier_info.my.get("givenname").toString();
            }
            if (supplier_info.my.get("email") != null ) {
                register_info.email = supplier_info.my.get("email").toString();
            }
            if (supplier_info.my.get("system") != null ) {
                register_info.system = supplier_info.my.get("system").toString();
            }
            if (supplier_info.my.get("browser") != null ) {
                register_info.browser = supplier_info.my.get("browser").toString();
            }
            if (supplier_info.my.get("regip") != null ) {
                register_info.regip = supplier_info.my.get("regip").toString();
            }
            if (supplier_info.my.get("province") != null ) {
                register_info.province = supplier_info.my.get("province").toString();
            }
            if (supplier_info.my.get("city") != null ) {
                register_info.city = supplier_info.my.get("city").toString();
            }
            if (supplier_info.my.get("suppliers_name") != null ) {
                register_info.suppliers_name = supplier_info.my.get("suppliers_name").toString();
            }
            if (supplier_info.my.get("legal_person_name") != null ) {
                register_info.legal_person_name = supplier_info.my.get("legal_person_name").toString();
            }
            if (supplier_info.my.get("business_license") != null ) {
                register_info.business_license = supplier_info.my.get("business_license").toString();
            }
            if (supplier_info.my.get("business_license_img_url") != null ) {
                register_info.business_license_img_url = supplier_info.my.get("business_license_img_url").toString();
            }
            if (supplier_info.my.get("legal_person_identity_card") != null ) {
                register_info.legal_person_identity_card = supplier_info.my.get("legal_person_identity_card").toString();
            }
            if (supplier_info.my.get("legal_person_certificate_img_url") != null ) {
                register_info.legal_person_certificate_img_url = supplier_info.my.get("legal_person_certificate_img_url").toString();
            }
            if (supplier_info.my.get("legal_person_certificate_reverse_img_url") != null ) {
                register_info.legal_person_certificate_reverse_img_url = supplier_info.my.get("legal_person_certificate_reverse_img_url").toString();
            }
            return register_info;
        } catch(Exception e) {
            throw e;
        }
    }


    @Override
    public void submit(String mobile) throws Exception {
        try {
            Supplier supplier;
            XN_Query query = XN_Query.create ( "Content" ).tag("suppliers")
                    .filter( "type", "eic", "suppliers" )
                    .filter ("my.deleted", "=",2)
                    .filter ("my.mobile", "=",mobile)
                    .end(1);
            List<Object> settings = query.execute();
            if (settings.isEmpty()) {
                 query = XN_Query.create ( "Content" ).tag("suppliers")
                        .filter( "type", "eic", "suppliers" )
                        .filter ("my.deleted", "=","0")
                        .filter ("my.approvalstatus", "!=",2)
                        .filter ("my.mobile", "=",mobile)
                        .end(1);
                settings = query.execute();
                if (settings.isEmpty()) {
                    throw new Exception("no RegisterInfo");
                } else {
                    Content supplier_info = (Content) settings.get(0);
                    supplier_info.my.put("deleted", "0");
                    supplier_info.my.put("suppliers_status", "Approvaling");
                    supplier_info.my.put("approvalstatus", "1");
                    supplier_info.my.put("submitapprovaldatetime", DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm"));
                    supplier_info.save("suppliers");
                    supplier = new Supplier(supplier_info);
                }
            } else {
                Content supplier_info = (Content) settings.get(0);
                supplier_info.my.put("deleted", "0");
                supplier_info.my.put("suppliers_status", "Approvaling");
                supplier_info.my.put("approvalstatus", "1");
                supplier_info.my.put("submitapprovaldatetime", DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm"));
                supplier_info.save("suppliers");
                supplier = new Supplier(supplier_info);
            }

            try {
                String approver = "system";
                supplierService.submitAgreeAndInit(supplier,approver);
            } catch(Exception e) {
                log.error("submitAgreeAndInit : " + e.getMessage());
            }
        } catch(Exception e) {
            throw e;
        }
    }


    @Override
    public boolean isExistSupplier(String mobile, String name) throws Exception {
        try {
            XN_Query query = XN_Query.create ( "Content" ).tag("suppliers")
                    .filter( "type", "eic", "suppliers" )
                    .filter ("my.deleted", "in", Arrays.asList(0,2))
                    .filter ("my.mobile", "!=",mobile)
                    .filter ("my.suppliers_name", "=",name)
                    .begin(0)
                    .end(1);
            List<Object> settings = query.execute();
            if (!settings.isEmpty() ) {
                return true;
            }
        } catch(Exception e) {
            throw e;
        }
        return false;
    }

    @Override
    public boolean isExist(String mobile, String type, String value) throws Exception {
        try {
            XN_Query query = XN_Query.create ( "Content" ).tag("suppliers")
                    .filter( "type", "eic", "suppliers" )
                    .filter ("my.deleted", "in", Arrays.asList(0,2))
                    .filter ("my.mobile", "!=",mobile)
                    .begin(0)
                    .end(1);
            if (type.compareTo("mobile") == 0) {
                query.filter("mobile", "=", value);
            } else if (type.compareTo("username") == 0) {
                query.filter("username", "=", value.toLowerCase());
            } else if (type.compareTo("email") == 0) {
                query.filter("email", "=", value);
            }
            List<Object> settings = query.execute();
            if (!settings.isEmpty()) {
                return true;
            }
        } catch(Exception e) {
            throw e;
        }
        return false;
    }
}
