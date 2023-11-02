package com.qnkj.core.base.modules.settings.customviews.service;

import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.TabField;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICustomviewsService {

    List<Object> getModuleViews(HttpServletRequest request);

    HashMap<String,Object> getListViewHeader(HttpServletRequest request);

    CustomView getCustomView(String viewid);

    List<TabField> getListFields(Map<String, Object> httpRequest);

    String saveCustomView(HttpServletRequest request) throws Exception;

    String delCustomviews(HttpServletRequest request) throws Exception;
}
