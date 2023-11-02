package com.qnkj.common.services.impl;

import com.qnkj.common.configs.BaseSupplierPickListConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.common.services.*;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

@Service
public class ModuleServicesImpl implements IModuleServices {
    private final ITabServices tabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final IBlockServices blockServices;
    private final ITabFieldServices tabFieldServices;
    private final ICustomViewServices customViewServices;
    private final IEntityLinkServices entityLinkServices;
    private final IOutsideLinkServices outsideLinkServices;
    private final ISearchColumnServices searchColumnServices;
    private final IPopupDialogServices popupDialogServices;
    private final IActionServices actionServices;
    private final IModentityNumServices modentityNumServices;
    private final IPickListServices pickListServices;
    private final IParentTabServices parentTabServices;
    private final IProgramServices programServices;
    private final ILayoutServices layoutServices;
    private final IDataPermissionServices dataPermissionServices;
    private final IApiServices apiServices;

    public ModuleServicesImpl(ITabServices tabServices,
                              IModuleMenuServices moduleMenuServices,
                              IBlockServices blockServices,
                              ITabFieldServices tabFieldServices,
                              ICustomViewServices customViewServices,
                              IEntityLinkServices entityLinkServices,
                              IOutsideLinkServices outsideLinkServices,
                              ISearchColumnServices searchColumnServices,
                              IPopupDialogServices popupDialogServices,
                              IActionServices actionServices,
                              IModentityNumServices modentityNumServices,
                              IPickListServices pickListServices,
                              IParentTabServices parentTabServices,
                              IProgramServices programServices,
                              ILayoutServices layoutServices,
                              IDataPermissionServices dataPermissionServices,
                              IApiServices apiServices) {
        this.tabServices = tabServices;
        this.moduleMenuServices = moduleMenuServices;
        this.blockServices = blockServices;
        this.tabFieldServices = tabFieldServices;
        this.customViewServices = customViewServices;
        this.entityLinkServices = entityLinkServices;
        this.outsideLinkServices = outsideLinkServices;
        this.searchColumnServices = searchColumnServices;
        this.popupDialogServices = popupDialogServices;
        this.actionServices = actionServices;
        this.modentityNumServices = modentityNumServices;
        this.pickListServices = pickListServices;
        this.parentTabServices = parentTabServices;
        this.programServices = programServices;
        this.layoutServices = layoutServices;
        this.dataPermissionServices = dataPermissionServices;
        this.apiServices = apiServices;
    }

    @Override
    public Module get(String modulename) throws Exception {
        Module module = new Module();
        module.Tabinfo = tabServices.get(modulename);
        module.moduleMenu = moduleMenuServices.get(modulename);
        module.Blocks = blockServices.list(modulename);
        module.CustomViews = customViewServices.list(modulename);
        module.Fields = tabFieldServices.list(modulename);
        module.Entitylink = entityLinkServices.get(modulename);
        module.OutsideLinks = outsideLinkServices.list(modulename);
        module.SearchColumns = searchColumnServices.list(modulename);
        module.Popupdialog = popupDialogServices.get(modulename);
        module.Modentitynum = modentityNumServices.get(modulename);
        module.dataPermission = dataPermissionServices.get(modulename);
        module.api = apiServices.get(modulename);
        module.picklists = this.getAllPickLists();
        module.supplierPickLists = this.getSupplierPickLists();
        module.actions = actionServices.list(modulename, -1);
        module.MenuGroup = this.getMenuGroup();
        module.Layouts = this.getLayout(modulename,module.Fields);
        return module;
    }

    @Override
    public List<PickList> getAllPickLists() {
        return pickListServices.list();
    }

    private List<SupplierPickList> getSupplierPickLists() {
        return BaseSupplierPickListConfig.getPicklists();
    }

    @Override
    public List<Object> getMenuGroup(){
        List<Object> result = new ArrayList<>();
        List<Program> programs = programServices.list();
        if(!Utils.isEmpty(programs)) {
            for (Program program : programs) {
                result.add(program);
                List<Object> parentTabs = getParentTabs(program.group);
                if(!Utils.isEmpty(parentTabs)){
                    result.addAll(parentTabs);
                }
            }
        }
        return result;
    }

    private List<Object> getParentTabs(String group){
        List<Object> result = new ArrayList<>();
        if(!Utils.isEmpty(group)) {
            List<ParentTab> parentTabs = parentTabServices.list(group);
            if(!Utils.isEmpty(parentTabs)) {
                for(ParentTab parentTab: parentTabs){
                    if("developmentmanager".equals(parentTab.name)) {
                        continue;
                    }
                    result.add(parentTab);
                    List<Object> subtabs = getParentTabs(parentTab.name);
                    if(!Utils.isEmpty(subtabs)){
                        result.addAll(subtabs);
                    }
                }
            }
        }
        return result;
    }

    private List<Layout> getLayout(String modulename, List<TabField> fields) {
        List<Layout> layouts = layoutServices.list(modulename);
        if(Utils.isEmpty(layouts) && !Utils.isEmpty(fields)){
            layouts = new ArrayList<>();
            int index = 0;
            for(TabField field: fields){
                if(field.block < 0) {
                    continue;
                }
                if(field.displaytype == 2) {
                    continue;
                }
                int row = index / 2;
                Layout layout;
                if(field.merge_column == 1){
                    layout = new Layout();
                    layout.columns = Collections.singletonList("12");
                    layout.fields.add(field.fieldname);
                    layout.sequence = row;
                    layouts.add(layout);
                    if(index % 2 == 1) {
                        index++;
                    }
                    index+=2;
                    continue;
                }
                if(field.merge_column == 2) {
                    if(index % 2 == 1) {
                        index++;
                        row = index / 2;
                    }
                }
                if(layouts.size() <= row || Utils.isEmpty(layouts.get(row))){
                    layout = new Layout();
                    layout.columns = Arrays.asList("6","6");
                    layout.fields = Arrays.asList("","");
                    layout.sequence = row;
                    layouts.add(layout);
                } else {
                    layout = layouts.get(row);
                }
                layout.fields.set(index % 2,field.fieldname);
                index++;
            }
        }
        return layouts;
    }
}
