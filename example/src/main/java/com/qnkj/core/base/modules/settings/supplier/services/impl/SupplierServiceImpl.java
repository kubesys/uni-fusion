package com.qnkj.core.base.modules.settings.supplier.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_ModentityNum;
import com.github.restapi.XN_Profile;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.StringUtils;
import com.qnkj.core.base.modules.home.BuiltinNotices.utils.MyNoticeUtils;
import com.qnkj.core.base.modules.settings.supplier.entitys.Supplier;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
import com.qnkj.core.utils.RolesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * create by Auto Generator
 * create date 2020-11-23
 */

@Slf4j
@Service
public class SupplierServiceImpl implements ISupplierService {


    @Override
    public Supplier load(String supplierid) throws Exception {
        try {
            Content supplier_info = XN_Content.load(supplierid,"suppliers");
            return new Supplier(supplier_info);
        } catch(Exception e) {
            throw e;
        }
    }

    @Override
    public Profile submitAgreeAndInit(Supplier info, String approver) throws Exception {
        try {
            if (info.username.isEmpty()) {
                throw new Exception("用户名称必填！");
            }
            if (info.mobile.isEmpty()) {
                throw new Exception("手机号码必填！");
            }
            if (info.password.isEmpty()) {
                throw new Exception("密码必填！");
            }
            if (info.givenname.isEmpty()) {
                throw new Exception("姓名必填！");
            }
            if (info.email.isEmpty()) {
                throw new Exception("用户邮箱必填！");
            }
            if (info.suppliers_name.isEmpty()) {
                throw new Exception("客户全称必填！");
            }
            if (info.legal_person_name.isEmpty()) {
                throw new Exception("企业法人必填！");
            }
            if (info.legal_person_identity_card.isEmpty()) {
                throw new Exception("法人身份证号码必填！");
            }
            if (info.business_license.isEmpty()) {
                throw new Exception("营业执照必填！");
            }
            if (info.business_license_img_url.isEmpty()) {
                throw new Exception("营业执照必须上传！");
            }
            if (info.legal_person_certificate_img_url.isEmpty()) {
                throw new Exception("法人身份证（正面）必须上传！");
            }
            if (info.legal_person_certificate_reverse_img_url.isEmpty()) {
                throw new Exception("法人身份证（反面）必须上传！");
            }
            XN_Query query = XN_Query.create ( "Content" ).tag("suppliers")
                    .filter( "type", "eic", "suppliers" )
                    .filter ("my.deleted", "=", 0)
                    .filter ("my.approvalstatus", "=", 2)
                    .filter ("my.suppliers_name", "=",info.suppliers_name)
                    .begin(0)
                    .end(1);
            List<Object> suppliers = query.execute();
            if (!suppliers.isEmpty() ) {
                throw new Exception("客户[" + info.suppliers_name + "]已经注册，不允许重复注册！");
            }
            List<Object> profiles = XN_Query.create("Profile").tag("profile")
                    .filter("type", "=", "supplier")
                    .filter("regioncode", "=", "86")
                    .filter("mobile", "=", info.mobile)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty() ) {
                throw new Exception("客户[" + info.suppliers_name + "]的联系手机[" + info.mobile + "]已经注册，不允许重复注册！");
            }

            profiles = XN_Query.create("Profile").tag("profile")
                    .filter("username", "=", info.username.toLowerCase())
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty() ) {
                throw new Exception("客户[" + info.suppliers_name + "]的账号[" + info.username + "]已经注册，不允许重复注册！");
            }

            profiles = XN_Query.create("Profile").tag("profile")
                    .filter("type", "=", "supplier")
                    .filter("email", "=", info.email.toLowerCase())
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty() ) {
                throw new Exception("客户[" + info.suppliers_name + "]的邮箱[" + info.email + "]已经注册，不允许重复注册！");
            }
            Content supplier_info = XN_Content.load(info.id,"suppliers");

            Profile profile = XN_Profile.create( info.email, info.password);
            profile.fullname = info.username;
            profile.type = "supplier";
            profile.regioncode =  info.regioncode;
            profile.mobile =  info.mobile;
            profile.status = true;
            profile.givenname = info.givenname;
            profile.link = "";
            profile.province = info.province;
            profile.city = info.city;
            profile.system = info.system;
            profile.browser = info.browser;
            profile.reg_ip = info.regip;
            profile = profile.save("profile");
            String profileid = profile.id;

            info.profileid = profileid;
            info.suppliers_no = XN_ModentityNum.get("supplier");

            supplier_info.my.put("userid", profileid);
            supplier_info.my.put("profileid", profileid);
            supplier_info.my.put("suppliersstatus", "Agree");
            supplier_info.my.put("approvalstatus", "2");
            supplier_info.my.put("finishapprover", approver);
            supplier_info.my.put("suppliers_no", info.suppliers_no);
            supplier_info.my.put("submitapprovaldatetime", DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm"));
            supplier_info.save("suppliers");

            Content newdepartment = XN_Content.create("supplier_departments","", "");
            newdepartment.add("deleted","0");
            newdepartment.add("sequence","100");
            newdepartment.add("parentid","");
            newdepartment.add("supplierid",info.id);
            newdepartment.add("name",info.suppliers_name);
            newdepartment.add("leadership","");
            newdepartment.add("mainleadership","");
            newdepartment.save("supplier_departments");

            Content newuser = XN_Content.create("supplier_users","","");
            newuser.add("deleted","0");
            newuser.add("supplierid",info.id);
            newuser.add("username",info.givenname);
            newuser.add("account",info.username);
            newuser.add("mailbox",info.email);
            newuser.add("profileid",profileid);
            newuser.add("mobile",info.mobile);
            newuser.add("usertype","boss");
            newuser.add("status","Active");
            newuser.add("roleid", RolesUtils.getRoleidByName("企业权限"));
            newuser.add("approvalstatus","2");
            newuser.add("supplierusersstatus","Agree");
            newuser.add("finishapprover", approver);
            newuser.add("submitapprovaldatetime", DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm"));
            newuser.add("departmentid",newdepartment.id);
            newuser.add("sequence","100");
            newuser.save("supplier_users");

            SaaSUtils saaSUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            String title = "恭喜你成功注册" + saaSUtils.getPlatformName();
            String body = "恭喜你成功注册" + saaSUtils.getPlatformName() + "，你的登录账号为【"+info.username+"】，下次登录请使用该账号或手机号码进行登录。";
            MyNoticeUtils.supplierCreate(info.id,title,body);

            try {
                 CallbackUtils.invoke("registerCallback", info);
            } catch (Exception e) {
                log.error("RegisterCallback : {}",e.getMessage());
            }
            return profile;
        } catch(Exception e) {
            throw e;
        }
    }

    @Override
    public void disAgree(String supplierid,String approver) throws Exception {
        Content supplierInfo = XN_Content.load(supplierid,"suppliers");
        supplierInfo.my.put("deleted", "2");
        supplierInfo.my.put("suppliersstatus", "Disagree");
        supplierInfo.my.put("approvalstatus", "3");
        supplierInfo.my.put("finishapprover", approver);
        supplierInfo.my.put("submitapprovalreplydatetime", DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm"));
        supplierInfo.save("suppliers");
    }

    @Override
    public Supplier update(String id, HashMap<String, Object> httpRequest) throws Exception {
        if(StringUtils.isBlank(id)){
            throw new Exception("id is null");
        }
        Content content = XN_Content.load(id,"suppliers");
        Supplier supplier = new Supplier();
        supplier.fromContent(content);
        supplier.fromRequest(httpRequest);
        String checkResult = checkSupplier(supplier);
        if(StringUtils.isNotBlank(checkResult)){
            throw new Exception(checkResult);
        }
        supplier.toContent(content);
        content.save("suppliers");
        supplier.fromContent(content);
        return supplier;
    }


    @Override
    public String checkSupplier(Supplier supplier) throws Exception{
        String result = "";
        //检查项
        String profileid = supplier.profileid;
        String email = supplier.email;
        String mobile = supplier.mobile;
        String suppliersName = supplier.suppliers_name;
        //参数
        String contentType = "Content";
        String tag = "suppliers";
        String type = "suppliers";
        if(StringUtils.isBlank(supplier.id)){
            if(StringUtils.isEmpty(profileid)){
                result = "系统错误:profileid is null";
                return result;
            }
            List<Object> suppliers = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("profileid", "=", profileid)
                    .filter ("my.deleted", "=", 0)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!suppliers.isEmpty() ) {
                result = "profileid：" + profileid  + "已经注册，不允许重复！";
                return result;
            }
            if(StringUtils.isEmpty(email)){
                result = "系统错误:email is null";
                return result;
            }
            suppliers = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("email", "=", email)
                    .filter ("my.deleted", "=", 0)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!suppliers.isEmpty() ) {
                result = "账户邮箱：" + email  + "已经注册，不允许重复！";
                return result;
            }
            if(StringUtils.isEmpty(mobile)){
                result = "系统错误:mobile is null";
                return result;
            }
            suppliers = XN_Query.create(contentType).tag(tag)
                    .filter( "type", "eic", type)
                    .filter("mobile", "=", mobile)
                    .filter ("my.deleted", "=", 0)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!suppliers.isEmpty() ) {
                result = "手机号码：" + mobile  + "已经注册，不允许重复！";
                return result;
            }
            if(StringUtils.isEmpty(suppliersName)){
                result = "系统错误:suppliers_name is null";
                return result;
            }
            suppliers = XN_Query.create(contentType).tag(tag)
                    .filter( "type", "eic", type)
                    .filter("suppliers_name", "=", suppliersName)
                    .filter ("my.deleted", "=", 0)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!suppliers.isEmpty() ) {
                result = "客户全称：" + suppliersName  + "已经注册，不允许重复！";
                return result;
            }
        }else {
            List<Object> suppliers ;
            if(StringUtils.isNotBlank(profileid)){
                suppliers = XN_Query.create(contentType).tag(tag)
                        .filter("type", "=", type)
                        .filter("profileid", "=", profileid)
                        .filter("id", "!=", supplier.id)
                        .filter ("my.deleted", "=", 0)
                        .begin(0)
                        .end(1)
                        .execute();
                if (!suppliers.isEmpty() ) {
                    result = "profileid：" + profileid  + "已经注册，不允许重复！";
                    return result;
                }
            }
            if(StringUtils.isNotBlank(email)){
                suppliers = XN_Query.create(contentType).tag(tag)
                        .filter("type", "=", type)
                        .filter("email", "=", email)
                        .filter("id", "!=", supplier.id)
                        .filter ("my.deleted", "=", 0)
                        .begin(0)
                        .end(1)
                        .execute();
                if (!suppliers.isEmpty() ) {
                    result = "账户邮箱：" + email  + "已经注册，不允许重复！";
                    return result;
                }
            }
            if(StringUtils.isNotBlank(mobile)){
                suppliers = XN_Query.create(contentType).tag(tag)
                        .filter( "type", "eic", type)
                        .filter("mobile", "=", mobile)
                        .filter("id", "!=", supplier.id)
                        .filter ("my.deleted", "=", 0)
                        .begin(0)
                        .end(1)
                        .execute();
                if (!suppliers.isEmpty() ) {
                    result = "手机号码：" + mobile  + "已经注册，不允许重复！";
                    return result;
                }
            }
            if(StringUtils.isNotBlank(suppliersName)){
                suppliers = XN_Query.create(contentType).tag(tag)
                        .filter( "type", "eic", type)
                        .filter("suppliers_name", "=", suppliersName)
                        .filter("id", "!=", supplier.id)
                        .filter ("my.deleted", "=", 0)
                        .begin(0)
                        .end(1)
                        .execute();
                if (!suppliers.isEmpty() ) {
                    result = "客户全称：" + suppliersName  + "已经注册，不允许重复！";
                    return result;
                }
            }
        }
        return result;
    }


    @Override
    public String getSuppliersName(String id) {
        String suppliersName = "";
        if(org.apache.commons.lang3.StringUtils.isEmpty(id)){
            return  suppliersName;
        }
        Supplier suppliers = null;
        Content result = null;
        try {
            result = XN_Content.load(id, "suppliers");
            if(result != null) {
                suppliers = new Supplier(result);
                suppliersName = suppliers.suppliers_name;
            }
        } catch (Exception e) {
            return  suppliersName;
        }
        return suppliersName;
    }

    @Override
    public List<Supplier> getSuppliersBySuppliersName(String suppliers_name){
        if(StringUtils.isBlank(suppliers_name)){
            return null;
        }
        String contentType = "Content";
        String tag = "suppliers";
        String type = "suppliers";
        List<Supplier> result = new ArrayList<>();
        try {
            List<Object> suppliers = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("my.suppliers_name", "like", suppliers_name)
                    .execute();
            if(!suppliers.isEmpty()){
                suppliers.forEach(item -> {
                    result.add(new Supplier(item));
                });
            }
        } catch (Exception ignored) {}
        return result;
    }

    @Override
    public String getSupplierid(String profileid) throws Exception {
        try {
            List<Object> users = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("my.profileid", "=", profileid)
                    .notDelete().begin(0)
                    .end(1)
                    .execute();
            if (users.isEmpty()) {
                throw new Exception("该用户不是企业用户");
            }
            Content user_info = (Content)users.get(0);
            String supplierid = user_info.my.get("supplierid").toString();
            return supplierid;
        } catch(Exception e) {
            throw e;
        }
    }

    @Override
    public List<Supplier> findSuppliers(List<String> ids) {
        ArrayList<Supplier> lists = new ArrayList<>();
        HashSet idsSet = new HashSet(lists);
        XN_Query query = XN_Query.create("content").tag("supplier_users")
                .filter("type", "eic", "supplier_users")
                .notDelete().filter("id", "in", new ArrayList<String>(idsSet));
        try {
            List<Object> result = query.execute();
            if (!result.isEmpty()) {
                result.forEach(item -> lists.add(new Supplier(item)));
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
        return lists;
    }
}
