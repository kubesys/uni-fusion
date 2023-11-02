package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.WebException;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.DataPermission;
import com.qnkj.common.entitys.Expression;
import com.qnkj.common.services.IDataPermissionServices;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by 徐雁
 * create date 2021/12/14
 * create time 3:45 下午
 */

@Service
public class DataPermissionImpl implements IDataPermissionServices {
    private static Map<String, Map<String,DataPermission>> cacheValidationRoles = new HashMap<>();

    @Override
    public void clear() {
        String application = BaseSaasConfig.getApplication();
        cacheValidationRoles.remove(application);
    }

    @Override
    public void clear(String modulename) {
        String application = BaseSaasConfig.getApplication();
        if(cacheValidationRoles.containsKey(application)){
            cacheValidationRoles.get(application).remove(modulename);
        }
    }

    @Override
    public DataPermission get(String modulename) {
        String application = BaseSaasConfig.getApplication();
        if(cacheValidationRoles.containsKey(application) && cacheValidationRoles.get(application).containsKey(modulename)){
            return cacheValidationRoles.get(application).get(modulename);
        } else {
            try{
                List<Object> validationroles = XN_Query.contentQuery().tag("datapermissions")
                        .filter("type", "eic", "datapermissions")
                        .filter("my.modulename", "eic", modulename)
                        .order("my.sequence","A_N").notDelete()
                        .end(-1).execute();
                if(!validationroles.isEmpty()) {
                    DataPermission validationRole = new DataPermission();
                    validationRole.setModulename(modulename);
                    for(Object item: validationroles) {
                        String rolename = ((Content)item).get("rolename").toString();
                        Expression expression = new Expression(item);
                        if(validationRole.getExpressions().containsKey(rolename)){
                            validationRole.getExpressions().get(rolename).add(expression);
                        } else {
                            validationRole.getExpressions().put(rolename,new ArrayList<>(Collections.singletonList(expression)));
                        }
                    }
                    if(cacheValidationRoles.containsKey(application)){
                        cacheValidationRoles.get(application).put(modulename,validationRole);
                    } else {
                        Map<String, DataPermission> info = new HashMap<>(1);
                        info.put(modulename,validationRole);
                        cacheValidationRoles.put(application,info);
                    }
                    return validationRole;
                }
            }catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public void updateAll(DataPermission validationRole) throws Exception {
        if(validationRole.getModulename() == null || validationRole.getModulename().isEmpty()) {
            throw new WebException("模块名称不能为空");
        }
        this.clear(validationRole.getModulename());
        try{
            if(!validationRole.getExpressions().isEmpty()) {
                List<Object> Save = new ArrayList<>(1);
                for (String rolename : validationRole.getExpressions().keySet()) {
                    int sequence = 0;
                    for (Expression expression : validationRole.getExpressions().get(rolename)) {
                        Content expConn = XN_Content.create("datapermissions", "");
                        expConn.set("modulename", validationRole.getModulename());
                        expConn.set("rolename", rolename);
                        expConn.set("fieldname", expression.getFieldName());
                        expConn.set("fieldlabel", expression.getFieldLabel());
                        expConn.set("leftbrackets", expression.getLeftBrackets());
                        expConn.set("logic", expression.getLogic().getValue());
                        expConn.set("fieldvalue", expression.getValue());
                        expConn.set("rightbrackets", expression.getRightBrackets());
                        expConn.set("symbol", expression.getSymbol().getValue());
                        expConn.set("valuelabel", expression.getValueLabel());
                        expConn.set("sequence", sequence);
                        Save.add(expConn);
                        sequence++;
                    }
                }
                if (!Save.isEmpty()) {
                    XN_Content.batchsave(Save, "datapermissions");
                }
            }
        }catch (Exception ignored) {}
    }

    @Override
    public void update(DataPermission validationRole) throws Exception {
        if(validationRole.getModulename() == null || validationRole.getModulename().isEmpty()) {
            throw new WebException("模块名称不能为空");
        }
        this.clear(validationRole.getModulename());
        try{
            if(!validationRole.getExpressions().isEmpty()) {
                List<Object> Save = new ArrayList<>(1);
                for (String rolename : validationRole.getExpressions().keySet()) {
                    int page = 0;
                    List<Object> validationroles;
                    do {
                        validationroles = XN_Query.contentQuery().tag("datapermissions")
                                .filter("type", "eic", "datapermissions")
                                .filter("my.modulename", "eic", validationRole.getModulename())
                                .filter("my.rolename","=",rolename)
                                .begin(page * 100).end((page + 1) * 100)
                                .end(-1).notDelete().execute();
                        if(!validationroles.isEmpty()) {
                            XN_Content.delete(validationroles, "datapermissions");
                        }
                        page++;
                    }while (validationroles.size() == 100);

                    int sequence = 0;
                    for (Expression expression : validationRole.getExpressions().get(rolename)) {
                        Content expConn = XN_Content.create("datapermissions", "");
                        expConn.set("modulename", validationRole.getModulename());
                        expConn.set("rolename", rolename);
                        expConn.set("fieldname", expression.getFieldName());
                        expConn.set("fieldlabel", expression.getFieldLabel());
                        expConn.set("leftbrackets", expression.getLeftBrackets());
                        expConn.set("logic", expression.getLogic().getValue());
                        expConn.set("fieldvalue", expression.getValue());
                        expConn.set("rightbrackets", expression.getRightBrackets());
                        expConn.set("symbol", expression.getSymbol().getValue());
                        expConn.set("valuelabel", expression.getValueLabel());
                        expConn.set("sequence", sequence);
                        Save.add(expConn);
                        sequence++;
                    }
                }
                if (!Save.isEmpty()) {
                    XN_Content.batchsave(Save, "datapermissions");
                }
            }
        }catch (Exception ignored) {}
    }

    @Override
    public void update(List<DataPermission> validationRoles) throws Exception {
        for(DataPermission item: validationRoles){
            this.update(item);
        }
    }

    @Override
    public void delete(String modulename) {
        try {
            this.clear(modulename);
            int page = 0;
            List<Object> validationroles;
            do {
                validationroles = XN_Query.contentQuery().tag("datapermissions")
                        .filter("type", "eic", "datapermissions")
                        .filter("my.modulename", "eic", modulename)
                        .begin(page * 100).end((page + 1) * 100)
                        .end(-1).notDelete().execute();
                if (!validationroles.isEmpty()) {
                    XN_Content.delete(validationroles, "datapermissions");
                }
                page++;
            } while (validationroles.size() == 100);
        }catch (Exception ignored) {}
    }
}
