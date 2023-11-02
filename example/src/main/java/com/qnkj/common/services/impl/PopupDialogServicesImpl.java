package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.PopupDialog;
import com.qnkj.common.services.IPopupDialogServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Service
public class PopupDialogServicesImpl implements IPopupDialogServices {

    private static Map<String,Map<String, PopupDialog>> cachePopupDialogs = new HashMap<>();

    @Override
    public void clear() {
        cachePopupDialogs.clear();
    }

    @Override
    public void clear(String modulename) {
        String application = BaseSaasConfig.getApplication();
        if (cachePopupDialogs.containsKey(application)) {
            cachePopupDialogs.get(application).remove(modulename);
        }
    }

    private void init(String modulename){
        if(Utils.isEmpty(modulename)) {
            init();
        } else {
            String application = BaseSaasConfig.getApplication();
            if (!Utils.isEmpty(application)) {
                if(cachePopupDialogs.containsKey(application)){
                    if(!cachePopupDialogs.get(application).containsKey(modulename)) {
                        try {
                            Map<String, PopupDialog> dialogs = cachePopupDialogs.get(application);
                            List<Object> popupdialogs = XN_Query.contentQuery().tag("popupdialogs")
                                    .filter("type", "eic", "popupdialogs")
                                    .filter("my.modulename", "eic", modulename)
                                    .notDelete().end(-1).execute();
                            if (!popupdialogs.isEmpty()) {
                                for (Object popupdialog : popupdialogs) {
                                    List<Object> columns = XN_Query.contentQuery().tag("popupdialogcolumns")
                                            .filter("type", "eic", "popupdialogcolumns")
                                            .filter("my.record", "=", ((Content) popupdialog).id)
                                            .order("my.sequence", "A_N")
                                            .notDelete().end(-1).execute();
                                    List<String> columnlist = new ArrayList<>();
                                    for (Object column : columns) {
                                        columnlist.add(((Content) column).my.get("columnname").toString());
                                    }
                                    PopupDialog popupDialog = new PopupDialog();
                                    popupDialog.columns(columnlist).modulename(modulename);
                                    popupDialog.id = ((Content) popupdialog).id;
                                    if (!Utils.isEmpty(((Content) popupdialog).my.get("search"))) {
                                        if (((Content) popupdialog).my.get("search") instanceof String) {
                                            popupDialog.search(Collections.singletonList(((Content) popupdialog).my.get("search").toString()));
                                        } else {
                                            popupDialog.search((List<String>) ((Content) popupdialog).my.get("search"));
                                        }
                                    }
                                    dialogs.put(modulename, popupDialog);
                                }
                            }
                            cachePopupDialogs.put(application, dialogs);
                        } catch (Exception ignored) {}
                    }
                } else {
                    init();
                }
            }
        }
    }

    private void init() {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!Utils.isEmpty(application) && !cachePopupDialogs.containsKey(application)) {
                Map<String, PopupDialog> dialogs = new HashMap<>();
                List<Object> popupdialogs = XN_Query.contentQuery().tag("popupdialogs")
                        .filter("type", "eic", "popupdialogs")
                        .notDelete().end(-1).execute();
                if (!popupdialogs.isEmpty()) {
                    List<Object> columns = XN_Query.contentQuery().tag("popupdialogcolumns")
                            .filter("type", "eic", "popupdialogcolumns")
                            .order("my.sequence","A_N")
                            .notDelete().end(-1).execute();

                    for(int i=0;i<popupdialogs.size();i++) {
                        Content item = (Content)popupdialogs.get(i);
                        String modulename = item.my.get("modulename").toString();
                        List<String> columnlist = new ArrayList<>();
                        if(!columns.isEmpty()){
                            for(Object pitem: columns){
                                if (((Content)pitem).my.get("record").toString().compareTo(item.id) ==0) {
                                    columnlist.add(((Content)pitem).my.get("columnname").toString());
                                }
                            }
                            PopupDialog popupDialog = new PopupDialog();
                            popupDialog.columns(columnlist).modulename(modulename);
                            popupDialog.id = item.id;
                            if(!Utils.isEmpty(item.my.get("search"))) {
                                if (item.my.get("search") instanceof String) {
                                    popupDialog.search(Collections.singletonList(item.my.get("search").toString()));
                                } else {
                                    popupDialog.search((List<String>) item.my.get("search"));
                                }
                            }
                            dialogs.put(modulename,popupDialog);
                        }
                    }
                }
                cachePopupDialogs.put(application,dialogs);
            }

        } catch (Exception ignored) {
        }
    }


    @Override
    public PopupDialog get(String modulename) {
        try {
            init(modulename);
            String application = BaseSaasConfig.getApplication();
            if (cachePopupDialogs.containsKey(application)) {
                if(cachePopupDialogs.get(application).containsKey(modulename)) {
                    return cachePopupDialogs.get(application).get(modulename);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void update(PopupDialog popupDialog) throws Exception {
        if(Utils.isEmpty(popupDialog.modulename)) {
            throw new Exception("模块名称不能为空");
        }
        int index = 1;
        if(Utils.isEmpty(popupDialog.id)){
            List<String> columnlist = popupDialog.columns;
            if(Utils.isEmpty(columnlist)) {
                throw new Exception("显示列不能为空！");
            }
            this.clear(popupDialog.modulename);
            Content tabs = XN_Content.create("popupdialogs", "");
            String viewid = tabs.add("modulename", popupDialog.modulename)
                    .add("search", popupDialog.search)
                    .add("deleted", popupDialog.deleted)
                    .save("popupdialogs").id;
            for(Object column: columnlist){
                Content cvcolumnlists = XN_Content.create("popupdialogcolumns", "");
                cvcolumnlists.add("modulename",popupDialog.modulename).add("columnname",column).add("sequence",index).add("record",viewid).save("popupdialogcolumns");
                index++;
            }
            popupDialog.id = viewid;
        } else {
            this.clear(popupDialog.modulename);
            Content conn = XN_Content.load(popupDialog.id,"popupdialogs");
            if(popupDialog.deleted == 0) {
                conn.add("search", popupDialog.search).add("modulename", popupDialog.modulename).add("deleted", popupDialog.deleted).save("popupdialogs");
            } else {
                conn.delete("popupdialogs");
            }
            List<Object> query;
            do {
                query = XN_Query.contentQuery().tag("popupdialogcolumns")
                        .filter("type", "eic", "popupdialogcolumns")
                        .filter("my.record","=",popupDialog.id)
                        .begin(0).end(100).execute();
                XN_Content.delete(query, "popupdialogcolumns");
            } while (query.size() == 100);
            if(popupDialog.deleted == 0) {
                List<String> columnlist = popupDialog.columns;
                for (Object column : columnlist) {
                    Content cvcolumnlists = XN_Content.create("popupdialogcolumns", "");
                    cvcolumnlists.add("modulename", popupDialog.modulename).add("columnname", column).add("sequence", index).add("record", popupDialog.id).save("popupdialogcolumns");
                    index++;
                }
            }
        }
        if(popupDialog.deleted == 0) {
            String application = BaseSaasConfig.getApplication();
            Map<String, PopupDialog> dialogs = new HashMap<>();
            dialogs.put(popupDialog.modulename, popupDialog);
            cachePopupDialogs.put(application, dialogs);
        }
    }
}
