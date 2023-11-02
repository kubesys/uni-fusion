package com.qnkj.core.base.modules.settings.loginlog.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.entitys.LoginLog;
import com.qnkj.core.base.modules.settings.loginlog.service.ILoginlogService;
import com.qnkj.core.base.modules.settings.loginlog.utils.AddressUtil;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.utils.IpUtil;
import com.qnkj.core.utils.ProfileUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * create by 徐雁
 */

@Service
public class LoginlogServiceImpl implements ILoginlogService {

    @Override
    public void checkLoginLog(HttpServletRequest request) {
        try {
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            String profileid = ProfileUtils.getCurrentProfileId();
            XN_Query query = XN_Query.create("YearMonthContent").tag("loginlogs")
                    .filter("type", "eic", "loginlogs")
                    .filter("published", ">=", startDateFormat.format(new Date()))
                    .filter("published", "<=", endDateFormat.format(new Date()))
                    .filter("my.profileid", "=", profileid)
                    .begin(0)
                    .end(1);
            List<Object> contentList = query.execute();
            if (contentList.isEmpty()) {
                LoginLog loginLog = new LoginLog();
                loginLog.profileid = profileid;
                loginLog.setSystemBrowserInfo(request);
                saveLoginLog(request, loginLog);
            }
        } catch (Exception ig) {}
    }

    @Override
    public void saveLoginLog(HttpServletRequest request, LoginLog loginLog){
        try {
            loginLog.loginTime = new Date();
            String ip = IpUtil.getIpAddr(request);
            String location = AddressUtil.getCityInfo(ip);
            loginLog.ip = ip;
            loginLog.location = "";
            if (!Utils.isEmpty(location)) {
                loginLog.location = location;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Content newcontent = XN_Content.create("loginlogs", "", loginLog.profileid, 9);
            newcontent.add("supplierid", SupplierUtils.getSupplierid());
            newcontent.add("deleted", "0");
            newcontent.add("profileid", loginLog.profileid);
            newcontent.add("logintime", dateFormat.format(loginLog.loginTime));
            newcontent.add("location", loginLog.location);
            newcontent.add("ip", loginLog.ip);
            newcontent.add("system", loginLog.system);
            newcontent.add("browser", loginLog.browser);
            newcontent.save("loginlogs");
        } catch (Exception e) {  }
    }

    @Override
    public Long findTotalVisitCount() throws Exception {
        try {
            XN_Query query = XN_Query.create("YearMonthContent_count").tag("loginlogs")
                    .filter("type", "eic", "loginlogs")
                    .begin(0)
                    .end(-1);
            query.execute();
            return query.getTotalCount();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Long findTodayVisitCount() throws Exception {
        try {
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            XN_Query query = XN_Query.create("YearMonthContent_count").tag("loginlogs")
                    .filter("type", "eic", "loginlogs")
                    .filter("published", ">=", startDateFormat.format(new Date()))
                    .filter("published", "<=", endDateFormat.format(new Date()))
                    .begin(0)
                    .end(-1);
            query.execute();
            return query.getTotalCount();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Long findTodayIp() throws Exception {
        try {
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            XN_Query query = XN_Query.create("YearMonthContent_count").tag("loginlogs")
                    .filter("type", "eic", "loginlogs")
                    .filter("published", ">=", startDateFormat.format(new Date()))
                    .filter("published", "<=", endDateFormat.format(new Date()))
                    .rollup()
                    .group("my.ip")
                    .begin(0)
                    .end(-1);
            List<Object> contentList = query.execute();
            return Long.valueOf(contentList.size());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> findLastTenDaysVisitCount(String profileid) throws Exception {
        try {
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -10);
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            XN_Query query = XN_Query.create("YearMonthContent_count").tag("loginlogs")
                    .filter("type", "eic", "loginlogs")
                    .filter("published", ">=", startDateFormat.format(calendar.getTime()))
                    .filter("published", "<=", endDateFormat.format(today))
                    .rollup()
                    .group("published@monthday")
                    .begin(0)
                    .end(-1);
            if (profileid != null && profileid.compareTo("") != 0) {
                query.filter("my.profileid", "=", profileid);
            }
            List<Object> contentList = query.execute();
            List<Map<String, Object>> result = new ArrayList<>();
            contentList.forEach(item -> {
                Map info = new HashMap();
                Content content = (Content) item;
                info.put("days", content.published);
                info.put("count", content.my.get("count"));
                result.add(info);
            });
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String getLastLoginTime(String profileid) throws Exception {
        try {
            XN_Query query = XN_Query.create("YearMonthContent").tag("loginlogs")
                    .filter("type", "eic", "loginlogs")
                    .filter("my.profileid", "=", profileid)
                    .order("published", "DESC")
                    .begin(0)
                    .end(2);
            List<Object> contentList = query.execute();
            if (contentList.size() == 2) {
                Content loginlogInfo = (Content) contentList.get(1);
                return loginlogInfo.published;
            }
            return "";
        } catch (Exception e) {
            throw e;
        }
    }
}
