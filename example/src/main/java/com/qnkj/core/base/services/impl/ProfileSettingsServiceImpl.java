package com.qnkj.core.base.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseMenuConfig;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.entitys.ProfileSettings;
import com.qnkj.core.base.services.IProfileSettingsService;
import com.qnkj.core.utils.ProfileUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Oldhand
 */
@Service
public class ProfileSettingsServiceImpl implements IProfileSettingsService {
    private static Map<String,ProfileSettings> cacheProfileSettings = new HashMap<>();
    private String getKey(String profileid) {
        return BaseSaasConfig.getApplication() + "_" + profileid;
    }

    @Override
    public void updateSettings(String profileid, ProfileSettings info) throws Exception {
        XN_Query query = XN_Query.create ( "Content" ).tag("settings")
                .filter( "type", "eic", "settings" )
                .filter ("my.profileid", "=",profileid)
                .begin(0)
                .end(1);
        List<Object> settings = query.execute();
        if (settings.isEmpty() ) {
            Content newcontent = XN_Content.create("settings","",ProfileUtils.getCurrentProfileId());
            newcontent.add("deleted","0");
            newcontent.add("profileid",profileid);
            newcontent.add("theme",info.theme);
            newcontent.add("menu",info.menu);
            newcontent.add("pagelimit",info.pageLimit);
            newcontent.add("isdev",info.isDev?1:0);
            newcontent.save("settings");
        } else {
            Content settingInfo = (Content)settings.get(0);
            settingInfo.my.put("theme",info.theme);
            settingInfo.my.put("istab",info.istab);
            settingInfo.my.put("menu",info.menu);
            settingInfo.my.put("pagelimit",info.pageLimit);
            settingInfo.my.put("isdev",info.isDev?1:0);
            settingInfo.save("settings");
        }
        cacheProfileSettings.remove(getKey(profileid));
    }

    @Override
    public ProfileSettings get(String profileid) throws Exception {
        if (cacheProfileSettings.containsKey(getKey(profileid))) {
            return cacheProfileSettings.get(getKey(profileid));
        }
        XN_Query query = XN_Query.create ( "Content" ).tag("settings")
                .filter( "type", "eic", "settings" )
                .filter ("my.profileid", "=",profileid)
                .begin(0)
                .end(1);
        List<Object> settings = query.execute();
        if (settings.isEmpty() ) {
            ProfileSettings profilesettings = new ProfileSettings();
            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                profilesettings.menu = BaseMenuConfig.getProgram("supplier");
                profilesettings.isDev = false;
            }else if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()){
                profilesettings.menu = BaseMenuConfig.getProgram("system");
                profilesettings.isDev = true;
            }else {
                profilesettings.menu = BaseMenuConfig.getProgram("general");
                profilesettings.isDev = false;
            }
            profilesettings.pageLimit = 20;
            if(!Utils.isEmpty(profilesettings.menu)) {
                cacheProfileSettings.put(getKey(profileid), profilesettings);
            }
            return profilesettings;
        } else {
            Content settingInfo = (Content)settings.get(0);
            ProfileSettings profilesettings = new ProfileSettings();
            if (settingInfo.my.get("theme") != null) {
                profilesettings.theme = settingInfo.my.get("theme").toString();
            }
            if (settingInfo.my.get("istab") != null) {
                profilesettings.istab = settingInfo.my.get("istab").toString();
            }
            if(settingInfo.my.get("isdev") != null) {
                profilesettings.isDev = "1".equals(settingInfo.my.get("isdev"));
            }
            if (settingInfo.my.containsKey("pagelimit")) {
                profilesettings.pageLimit = Integer.parseInt(settingInfo.my.get("pagelimit").toString());
            } else {
                profilesettings.pageLimit = 20;
            }
            if (!Utils.isEmpty(settingInfo.my.get("menu"))){
                profilesettings.menu = settingInfo.my.get("menu").toString();
                if(!(!ContextUtils.isJar() && "lcdp".equals(profilesettings.menu) && profilesettings.isDev && (ProfileUtils.isAdmin() || ProfileUtils.isBoss() || ProfileUtils.isAssistant() || ProfileUtils.isSupplierAssistant()))){
                    if (ProfileUtils.isSupplier()) {
                        if (!BaseMenuConfig.isAuthorizeProgram(Arrays.asList("general", "supplier"), profilesettings.menu)) {
                            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                                profilesettings.menu = BaseMenuConfig.getProgram("supplier");
                            } else {
                                profilesettings.menu = BaseMenuConfig.getProgram("general");
                            }
                        }
                    } else if (ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                        if (!BaseMenuConfig.isAuthorizeProgram(Arrays.asList("general", "system"), profilesettings.menu)) {
                            profilesettings.menu = BaseMenuConfig.getProgram("general");
                        }
                    } else if(ProfileUtils.isManager()) {
                        if(!(BaseMenuConfig.isAuthorizeProgram(Collections.singletonList("general"), profilesettings.menu) || ((BaseMenuConfig.isAuthorizeProgram(Collections.singletonList("system"), profilesettings.menu) && !"settings".equals(profilesettings.menu))))){
                            profilesettings.menu = BaseMenuConfig.getProgram("general");
                        }
                    } else {
                        if (!BaseMenuConfig.isAuthorizeProgram(Collections.singletonList("general"), profilesettings.menu)) {
                            profilesettings.menu = BaseMenuConfig.getProgram("general");
                        }
                    }
                }
            } else {
                if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                    profilesettings.menu = BaseMenuConfig.getProgram("supplier");
                }else if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()){
                    profilesettings.menu = BaseMenuConfig.getProgram("system");
                }else {
                    profilesettings.menu = BaseMenuConfig.getProgram("general");
                }
            }
            if(!Utils.isEmpty(profilesettings.menu)) {
                cacheProfileSettings.put(getKey(profileid), profilesettings);
            }
            return profilesettings;
        }
    }
}
