package com.qnkj.core.base.modules.settings.onlinedevelopments.service.impl;

import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.services.*;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.onlinedevelopments.service.IOnLineDevelopmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

@Slf4j
@Service
public class OnLineDevelopmentsServiceImpl implements IOnLineDevelopmentsService {
    private final IProgramServices programServices;
    private final IParentTabServices parentTabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final IModuleServices moduleServices;
    private final IBlockServices blockServices;
    private final ITabFieldServices fieldServices;
    private final ITabServices tabServices;
    private final ICustomViewServices customViewServices;
    private final ISearchColumnServices searchColumnServices;
    private final IActionServices actionServices;
    private final IOutsideLinkServices outsideLinkServices;
    private final IPopupDialogServices popupDialogServices;
    private final IModentityNumServices modentityNumServices;
    private final IEntityLinkServices entityLinkServices;
    private final IPickListServices pickListServices;
    private final ILayoutServices layoutServices;

    public OnLineDevelopmentsServiceImpl(IProgramServices programServices, IParentTabServices parentTabServices, IModuleMenuServices moduleMenuServices, IModuleServices moduleServices, IBlockServices blockServices, ITabFieldServices fieldServices, ITabServices tabServices, ICustomViewServices customViewServices, ISearchColumnServices searchColumnServices, IActionServices actionServices, IOutsideLinkServices outsideLinkServices, IPopupDialogServices popupDialogServices, IModentityNumServices modentityNumServices, IEntityLinkServices entityLinkServices, IPickListServices pickListServices,ILayoutServices layoutServices) {
        this.programServices = programServices;
        this.parentTabServices = parentTabServices;
        this.moduleMenuServices = moduleMenuServices;
        this.moduleServices = moduleServices;
        this.blockServices = blockServices;
        this.fieldServices = fieldServices;
        this.tabServices = tabServices;
        this.customViewServices = customViewServices;
        this.searchColumnServices = searchColumnServices;
        this.actionServices = actionServices;
        this.outsideLinkServices = outsideLinkServices;
        this.popupDialogServices = popupDialogServices;
        this.modentityNumServices = modentityNumServices;
        this.entityLinkServices = entityLinkServices;
        this.pickListServices = pickListServices;
        this.layoutServices = layoutServices;
    }

    private List<Object> getParentTab(List<Object> parenttabs,String parentname, String parentid) {
        List<Object> result = new ArrayList<>();
        List<ParentTab> parentTabs = new ArrayList<>();
        for(Object item: parenttabs){
            if(Utils.isEmpty(((Content)item).my.get("modulename")) && ((Content)item).my.get("program").equals(parentname)){
                parentTabs.add(new ParentTab(item));
            }
        }
        if(!Utils.isEmpty(parentTabs)) {
            for (ParentTab tab : parentTabs) {
                if("developmentmanager".equals(tab.name) || tab.builtin) {
                    continue;
                }
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", parentid);
                infoMap.put("id", tab.id);
                infoMap.put("title", tab.label);
                infoMap.put("name", tab.label);
                infoMap.put("key",tab.name);
                infoMap.put("builtin",false);
                infoMap.put("menulevel", "parent");
                infoMap.put("iconSkin","catalogue");
                result.add(infoMap);
                List<ModuleMenu> moduleMenus = new ArrayList<>();
                for(Object item: parenttabs){
                    if(!Utils.isEmpty(((Content)item).my.get("parent")) && ((Content)item).my.get("parent").equals(tab.name) && ((Content)item).my.get("program").equals(parentname)){
                        moduleMenus.add(new ModuleMenu(item));
                    }
                }
                if(!Utils.isEmpty(moduleMenus)) {
                    for (ModuleMenu menu : moduleMenus) {
                        if("developments".equals(menu.modulename) || menu.builtin) {
                            continue;
                        }
                        Map<String,Object> deveMap = new HashMap<>(1);
                        deveMap.put("pId", tab.id);
                        deveMap.put("id", menu.id);
                        deveMap.put("title", menu.label);
                        deveMap.put("name", menu.label);
                        deveMap.put("key",menu.modulename);
                        deveMap.put("builtin",false);
                        deveMap.put("menulevel", "module");
                        deveMap.put("iconSkin","module");
                        result.add(deveMap);
                    }
                }
                List<Object> sub = getParentTab(parenttabs,tab.name, tab.id);
                if(sub.size()>0){
                    result.addAll(sub);
                }
            }
        }
        return result;
    }

    @Override
    public Object getTree() {
        List<Object> result = new ArrayList<>();
        List<Program> programs = programServices.list();
        if(!Utils.isEmpty(programs)) {
            List<Object> parenttabs = new ArrayList<>();
            try {
                parenttabs = XN_Query.contentQuery().tag("parenttabs")
                        .filter("type","eic","parenttabs")
                        .notDelete()
                        .order("my.order","A_N")
                        .end(-1).execute();
            }catch (Exception ignored){}

            for (Program item : programs) {
                if(item.builtin) {
                    continue;
                }
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", "");
                infoMap.put("id", item.id);
                infoMap.put("title", item.label);
                infoMap.put("name", item.label);
                infoMap.put("key",item.group);
                infoMap.put("builtin",false);
                infoMap.put("menulevel", "program");
                infoMap.put("iconSkin","catalogue");
                result.add(infoMap);

                List<Object> sub = getParentTab(parenttabs,item.group, item.id);
                if(sub.size()>0){
                    result.addAll(sub);
                }
            }
        }
        if(result.size() <= 0){
            try {
                SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                String departmentRoot = saasUtils.getCompanyNickname();
                Program program = new Program().group("module").label(departmentRoot).order(1).authorize("general").icon("lcdp-icon-wangzhanshezhi");
                programServices.update(program);
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", "");
                infoMap.put("id", program.id);
                infoMap.put("title", program.label);
                infoMap.put("name", program.label);
                infoMap.put("key",program.group);
                infoMap.put("builtin",program.builtin);
                infoMap.put("menulevel", "program");
                infoMap.put("iconSkin","catalogue");
                result.add(infoMap);
            }catch (Exception ignored) {}
        }
        return result;
    }

}
