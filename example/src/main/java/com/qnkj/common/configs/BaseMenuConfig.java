package com.qnkj.common.configs;

import com.github.restapi.models.WebException;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * create by 徐雁
 */
@Slf4j
@Component("BaseMenuConfig")
public class BaseMenuConfig {
    private static final List<Program> programsettings = new ArrayList<>();
    private static final List<Program> programs = new ArrayList<>();
    private static final List<ParentTab> menusettings = new ArrayList<>();
    private static final HashMap<String, Object> menus = new HashMap<>();

    public static List<Program> getPrograms() {
        return programs;
    }
    public static List<Program> getProgramSettings() {
        return programsettings;
    }
    public static List<ParentTab> getParentMenus() {
        return menusettings;
    }

    private BaseMenuConfig() {

    }

    public static void clear() {
        programs.clear();
        menus.clear();
    }

    public static List<Object> get(String key) throws Exception {
        if (!Utils.isEmpty(menus.get(key))) {
            return (List<Object>) menus.get(key);
        } else {
            log.info("key: {} menus:  {}",key, menus);
            throw new WebException("无法获取菜单");
        }
    }
    public static void set(Object menu) {
        if(menu instanceof ParentTab){
            if (!Utils.isEmpty(menus.get(((ParentTab) menu).program))) {
                List<Object> menuList = (List<Object>) menus.get(((ParentTab) menu).program);
                menuList.add(menu);
            } else {
                boolean isfind = false;
                for(Map.Entry<String,Object> item: menus.entrySet()){
                    List<Object> menuList = (List<Object>) item.getValue();
                    for(Object parent: menuList){
                        if(parent instanceof ParentTab && ((ParentTab) parent).name.equals(((ParentTab) menu).program)){
                            menuList.add(menu);
                            isfind = true;
                            break;
                        }
                    }
                }
                if(!isfind) {
                    List<Object> menuList = new ArrayList<>(Collections.singleton(menu));
                    menus.put(((ParentTab) menu).program, menuList);
                }
            }
        }else if(menu instanceof ModuleMenu){
            if (Utils.isEmpty(menus.get(((ModuleMenu) menu).program))) {
                for(Map.Entry<String ,Object> item: menus.entrySet()){
                    List<Object> menuList = (List<Object>) item.getValue();
                    for(Object parent: menuList){
                        if(parent instanceof ParentTab && ((ParentTab) parent).name.equals(((ModuleMenu) menu).program)){
                            menuList.add(menu);
                            break;
                        }
                    }
                }
            }else {
                ArrayList<Object> group = (ArrayList<Object>) menus.get(((ModuleMenu) menu).program);
                group.add(menu);
            }
        }else if(menu instanceof Program){
            programs.add((Program) menu);
        }
    }

    public static void addProgram(Program program) {
        programsettings.removeIf(item -> item.group.equals(program.group));
        programsettings.add(program);
    }
    public static void addParentMenu(ParentTab group) {
        menusettings.removeIf(item -> item.program.equals(group.program) && item.name.equals(group.name));
        menusettings.add(group);
    }
    public static void addParentMenu(List<ParentTab> group) {
        menusettings.addAll(group);
    }
    public static String getProgram(String authorize) {
        for(Program item: programs){
            if(item.authorize.contains(authorize)){
                return item.group;
            }
        }
        return "";
    }
    public static String getDefaultProgram() {
        for(Program item: programsettings){
            if(item.authorize.contains("supplier") && item.group.compareTo("supplier") != 0){
                return item.group;
            }
        }
        for(Program item: programsettings){
            if(item.authorize.contains("supplier")){
                return item.group;
            }
        }
        for(Program item: programsettings){
            if(item.authorize.contains("general")){
                return item.group;
            }
        }
        return "";
    }
    public static Boolean isAuthorizeProgram(List<String> authorizes, String menu) {
        for(Program item: programs){
            if(item.group.equals(menu) && authorizes.contains(item.authorize)){
                return true;
            }
        }
        return false;
    }
}
