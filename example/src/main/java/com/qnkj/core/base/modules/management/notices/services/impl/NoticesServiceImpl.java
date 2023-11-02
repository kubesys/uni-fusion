package com.qnkj.core.base.modules.management.notices.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Filter;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.home.BuiltinNotices.utils.MyNoticeUtils;
import com.qnkj.core.base.modules.management.notices.entitys.NoticeLevel;
import com.qnkj.core.base.modules.management.notices.entitys.NoticeType;
import com.qnkj.core.base.modules.management.notices.entitys.Notices;
import com.qnkj.core.base.modules.management.notices.services.INoticesService;
import com.qnkj.core.utils.ProfileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * create by Auto Generator
 * create date 2020-11-17
 * @author Generator
 */

@Slf4j
@Service
public class NoticesServiceImpl implements INoticesService {

    @Override
    public HashMap<String,Object> get(String profileid, int page, int limit) throws Exception {
        List<Notices> lists = new ArrayList<>();
        if (page <= 0) { page = 1; }
        XN_Query query = XN_Query.create ( "YearContent" ).tag("notices")
                .filter( "type", "eic", "notices" )
                .filter ("my.deleted", "=",'0')
                .filter ("my.profileid", "=",profileid)
                .order ("published","DESC")
                .alwaysReturnTotalCount()
                .begin((page-1)*limit)
                .end(page*limit);
        List<Object> notices = query.execute();
        long count = query.getTotalCount();

        if (!notices.isEmpty() ) {
            notices.forEach(item ->
                lists.add(new Notices(item))
            );
        }
        if (count == 0 ) {
            SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            String title = saasUtils.getPlatformName() + "欢迎你!";
            String body = "欢迎您使用" + saasUtils.getPlatformName() + "！";
            MyNoticeUtils.profileCreate(profileid,title,body);
        }
        HashMap<String,Object> infoMap = new HashMap<>(1);
        infoMap.put("pages",count);
        infoMap.put("data",lists);
        return infoMap;
    }

    @Override
    public HashMap<String,Object> getSupplierNotices(String supplierid,String profileid,int page, int limit) throws Exception {
        List<Notices> lists = new ArrayList<>();
        String filter1 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","supplier"), XN_Filter.filter("my.supplierid", "=",supplierid));
        String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
        XN_Query query = XN_Query.create ( "YearContent" ).tag("notices")
                .filter( "type", "eic", "notices" )
                .filter ("my.deleted", "=",'0')
                .filter(XN_Filter.any(filter1,filter2))
                .order ("published","DESC")
                .alwaysReturnTotalCount()
                .begin((page-1)*limit)
                .end(page*limit);
        List<Object> notices = query.execute();
        long count = query.getTotalCount();
        if (!notices.isEmpty() ) {
            notices.forEach(item ->
                lists.add(new Notices(item))
            );
        }
        HashMap<String,Object> info = new HashMap<>(1);
        info.put("pages",count);
        info.put("data",lists);
        return info;
    }

    @Override
    public HashMap<String,Object> getApprovalNotices(String profileid, int page, int limit) throws Exception {
        List<Notices> lists = new ArrayList<>();
        String filter1 = XN_Filter.filter("my.noticetype","=","approval");
        String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
        XN_Query query = XN_Query.create ( "YearContent" ).tag("notices")
                .filter( "type", "eic", "notices" )
                .filter ("my.deleted", "=",'0')
                .filter(XN_Filter.any(filter1,filter2))
                .order ("published","DESC")
                .alwaysReturnTotalCount()
                .begin((page-1)*limit)
                .end(page*limit);
        List<Object> notices = query.execute();
        long count = query.getTotalCount();
        if (!notices.isEmpty() ) {
            notices.forEach(item ->
                lists.add(new Notices(item))
            );
        }
        HashMap<String,Object> info = new HashMap<>(1);
        info.put("pages",count);
        info.put("data",lists);
        return info;
    }

    @Override
    public HashMap<String,Object> getProfileNotices(String profileid, int page, int limit) throws Exception {
        List<Notices> lists = new ArrayList<>();
        XN_Query query = XN_Query.create ( "YearContent" ).tag("notices")
                .filter( "type", "eic", "notices" )
                .filter ("my.deleted", "=",'0')
                .filter ("my.noticetype","=","profile")
                .filter ("my.profileid", "=",profileid)
                .order ("published","DESC")
                .alwaysReturnTotalCount()
                .begin((page-1)*limit)
                .end(page*limit);
        List<Object> notices = query.execute();
        long count = query.getTotalCount();
        if (!notices.isEmpty() ) {
            notices.forEach(item ->
                lists.add(new Notices(item))
            );
        }
        HashMap<String,Object> info = new HashMap<>(1);
        info.put("pages",count);
        info.put("data",lists);
        return info;
    }

    @Override
    public HashMap<String,Object> getSystemNotices(String profileid,int page, int limit) throws Exception {
        List<Notices> lists = new ArrayList<>();
        String filter1 = XN_Filter.filter("my.noticetype","=","system");
        String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
        XN_Query query = XN_Query.create ( "YearContent" ).tag("notices")
                .filter( "type", "eic", "notices" )
                .filter ("my.deleted", "=",'0')
                .filter(XN_Filter.any(filter1,filter2))
                .order ("published","DESC")
                .alwaysReturnTotalCount()
                .begin((page-1)*limit)
                .end(page*limit);
        List<Object> notices = query.execute();
        long count = query.getTotalCount();
        if (!notices.isEmpty() ) {
            notices.forEach(item ->
                lists.add(new Notices(item))
            );
        }
        HashMap<String,Object> info = new HashMap<>(1);
        info.put("pages",count);
        info.put("data",lists);
        return info;
    }


    @Override
    public long getSupplierUnReadCount(String supplierid, String profileid) throws Exception {
        String thisday = DateTimeUtils.getDatetime("yyyy-MM-dd");
        String amonthago = DateTimeUtils.getDatetime(DateTimeUtils.addMonths(-1),"yyyy-MM-dd");
        String filter1 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","supplier"), XN_Filter.filter("my.supplierid", "=",supplierid));
        String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
        XN_Query query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter(XN_Filter.any(filter1,filter2))
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long allnotices = query.getTotalCount();
        query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter ("my.alreadyreads", "=", profileid)
                .filter(XN_Filter.any(filter1,filter2))
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long reads = query.getTotalCount();
        return allnotices - reads;
    }

    @Override
    public long getApprovalUnReadCount(String profileid) throws Exception {
        String thisday = DateTimeUtils.getDatetime("yyyy-MM-dd");
        String amonthago = DateTimeUtils.getDatetime(DateTimeUtils.addMonths(-1),"yyyy-MM-dd");
        String filter1 = XN_Filter.filter("my.noticetype","=","approval");
        String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
        XN_Query query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter(XN_Filter.any(filter1,filter2))
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long allnotices = query.getTotalCount();
        query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter ("my.alreadyreads", "=", profileid)
                .filter(XN_Filter.any(filter1,filter2))
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long reads = query.getTotalCount();
        return allnotices - reads;
    }

    @Override
    public long getProfileUnReadCount(String profileid) throws Exception {
        String thisday = DateTimeUtils.getDatetime("yyyy-MM-dd");
        String amonthago = DateTimeUtils.getDatetime(DateTimeUtils.addMonths(-1),"yyyy-MM-dd");
        XN_Query query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter ("my.noticetype","=","profile")
                .filter ("my.profileid", "=",profileid)
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long allnotices = query.getTotalCount();
        query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter ("my.noticetype","=","profile")
                .filter ("my.profileid", "=",profileid)
                .filter ("my.alreadyreads", "=", profileid)
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long reads = query.getTotalCount();
        return allnotices - reads;
    }

    @Override
    public long getSystemUnReadCount(String profileid) throws Exception {
        String thisday = DateTimeUtils.getDatetime("yyyy-MM-dd");
        String amonthago = DateTimeUtils.getDatetime(DateTimeUtils.addMonths(-1),"yyyy-MM-dd");
        String filter1 = XN_Filter.filter("my.noticetype","=","system");
        String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
        XN_Query query = XN_Query.create("YearContent_count").tag("notices")
            .filter("type", "eic", "notices")
            .filter("published", ">=", amonthago + " 00:00:00")
            .filter("published", "<=", thisday + " 23:59:59")
            .filter ("my.deleted", "=",'0')
            .filter(XN_Filter.any(filter1,filter2))
            .rollup()
            .begin(0)
            .end(-1);
        query.execute();
        long allnotices = query.getTotalCount();
        query = XN_Query.create("YearContent_count").tag("notices")
                .filter("type", "eic", "notices")
                .filter("published", ">=", amonthago + " 00:00:00")
                .filter("published", "<=", thisday + " 23:59:59")
                .filter ("my.deleted", "=",'0')
                .filter ("my.alreadyreads", "=", profileid)
                .filter(XN_Filter.any(filter1,filter2))
                .rollup()
                .begin(0)
                .end(-1);
        query.execute();
        long reads = query.getTotalCount();
        return allnotices - reads;
    }

    @Override
    public void update(String noticeid) throws Exception {
        Content noticeInfo = XN_Content.load(noticeid,"notices",7);
        Object alreadyreads = noticeInfo.my.get("alreadyreads");
        if(Utils.isEmpty(alreadyreads)) {
            alreadyreads = new ArrayList<>();
        }else if(alreadyreads instanceof String){
            alreadyreads = new ArrayList<>(Collections.singletonList(alreadyreads.toString()));
        }
        String profileid = ProfileUtils.getCurrentProfileId();
        if(alreadyreads instanceof List) {
            if (!((List<?>)alreadyreads).contains(profileid)) {
                ((List)alreadyreads).add(ProfileUtils.getCurrentProfileId());
                noticeInfo.my.put("alreadyreads", alreadyreads);
                noticeInfo.save("notices");
            }
        }
    }

    @Override
    public void create(String supplierid, String profileid, String publisher, NoticeType noticetype, NoticeLevel noticelevel, String title, String body, String md5) throws Exception {
        Content newcontent = XN_Content.create("notices","", ProfileUtils.getCurrentProfileId(),7);
        newcontent.add("deleted","0");
        newcontent.add("md5",md5);
        newcontent.add("alreadyreads",new ArrayList<>());
        newcontent.add("profileid", profileid);
        newcontent.add("supplierid", supplierid);
        newcontent.add("title",title);
        newcontent.add("body",body);
        newcontent.add("publisher", publisher);
        newcontent.add("noticetype",noticetype.getStrval());
        newcontent.add("noticelevel",noticelevel.getStrval());
        newcontent.save("notices");
    }

    @Override
    public Boolean exist(String md5) throws Exception {
        XN_Query query = XN_Query.create ( "YearContent" ).tag("notices")
                .filter( "type", "eic", "notices" )
                .filter ("my.deleted", "=",'0')
                .filter ("my.md5", "=", md5)
                .begin(0)
                .end(1);
        List<Object> lists = query.execute();
        return !lists.isEmpty();
    }



}
