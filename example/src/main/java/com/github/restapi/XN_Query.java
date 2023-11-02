package com.github.restapi;

import com.github.restapi.models.Content;
import com.github.restapi.models.FetchMsgException;
import com.github.restapi.models.FetchResult;
import com.github.restapi.models.Profile;
import com.github.restapi.utils.ColorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author oldhand
 */

@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
@Slf4j
public class XN_Query {
    /**表的存储方式*/
    private String xnSubject;
    /**缓存标签*/
    private String xnTag;
    /**获取记录的起始位置*/
    private int    xnBegin;
    /**获取记录的结束位置*/
    private int    xnEnd;
    /**表的存储方式(数值)*/
    private int    xnDatatype;
    /**是不是统计模式，统计的话，返回对应字段的总和*/
    private boolean   xnIsrollup;
    /**是不是返回数据表里面的记录数*/
    private boolean   xnAlwaysReturnTotalCount;
    /**排序*/
    private List<String> xnOrders;
    /**条件*/
    private List<Object>  xnFilters;
    /**统计字段*/
    private List<String>  xnRollups;
    /**分组字段*/
    private List<Object>  xnGroups;
    /**返回查询的记录数*/
    long    xnSize;

    public long getTotalCount() {
        return xnSize;
    }
    XN_Query(String subject) {
        xnSubject = subject;
        xnTag  = "";
        xnBegin  = 0;
        xnEnd    = 100;
        xnDatatype = 0;
        xnIsrollup = xnSubject.toLowerCase().contains("count");

        xnAlwaysReturnTotalCount = false;
        xnOrders = new ArrayList<>();
        xnFilters  = new ArrayList<>();
        xnRollups  = new ArrayList<>();
        xnGroups   = new ArrayList<>();
        xnSize  = 0;
    }
    public static XN_Query create(String subject) {
        return new XN_Query(subject);
    }
    public static XN_Query contentQuery() {
        return new XN_Query("Content");
    }
    public XN_Query notDelete() {
        String filter = XN_Filter.filter("my.deleted", "=", "0");
        xnFilters.add(filter);
        return this;
    }
    public XN_Query isDelete() {
        String filter = XN_Filter.filter("my.deleted", "!=", "0");
        xnFilters.add(filter);
        return this;
    }
    public XN_Query alwaysReturnTotalCount() {
        xnAlwaysReturnTotalCount = true;
        return this;
    }
    public XN_Query begin(int value) {
        xnBegin = value;
        return this;
    }
    public XN_Query end(int value) {
        xnEnd = value;
        return this;
    }
    public XN_Query tag(String value) {
        xnTag = value;
        return this;
    }
    public XN_Query order(String field, String type) {
        if (StringUtils.isNotEmpty(field)) {
            switch(type) {
                case "desc":
                case "DESC":
                case "d":
                case "D":
                    xnOrders.add("order=" + field.toLowerCase() + "@D");
                    break;
                case "asc":
                case "ASC":
                case "a":
                case "A":
                    xnOrders.add("order=" + field.toLowerCase() + "@A");
                    break;
                case "desc_number":
                case "DESC_NUMBER":
                case "d_n":
                case "D_N":
                    xnOrders.add("order=" + field.toLowerCase() + "@D_N");
                    break;
                case "asc_number":
                case "ASC_NUMBER":
                case "a_n":
                case "A_N":
                    xnOrders.add("order=" + field.toLowerCase() + "@A_N");
                    break;
                default:
                    break;
            }
        }
        return this;
    }
    public Boolean isOrder(){
        return !xnOrders.isEmpty();
    }
    public XN_Query rollup() {
        xnIsrollup = true;
        if (!xnRollups.isEmpty()) {
            if (!xnRollups.contains("field = 'count'")) {
                xnRollups.add("field = 'count'");
            }
        }
        return this;
    }
    public XN_Query rollup(String field) {
        if (StringUtils.isNotEmpty(field)) {
            if (xnIsrollup) {
                if (!xnRollups.contains("field = 'count'")) {
                    xnRollups.add("field = 'count'");
                }
            }
            if(Arrays.asList("id", "my.id").contains(field)) {
                String rollup = "field = 'count'";
                if (!xnRollups.contains(rollup)) {
                    xnRollups.add(rollup);
                }
            } else if(Arrays.asList("title", "author", "published", "updated", "application").contains(field) || field.startsWith("my.")) {
                String rollup = "field = '" + field.toLowerCase() + "'";
                if (!xnRollups.contains(rollup)) {
                    xnRollups.add(rollup);
                }

            } else {
                String rollup = "field = 'my." + field.toLowerCase() + "'";
                if (!xnRollups.contains(rollup)) {
                    xnRollups.add(rollup);
                }
            }
            xnIsrollup = true;
        } else {
            xnRollups.clear();
            xnIsrollup = true;
        }
        return this;
    }
    public XN_Query rollup(List<String> fields) {
        if(!fields.isEmpty()){
            for(String field : fields){
                if(!field.isEmpty()) {
                    if(Arrays.asList("id", "title", "author", "published", "updated", "application").contains(field) || field.startsWith("my.")) {
                        xnRollups.add("field = '" + field.toLowerCase() + "'");
                    }else {
                        xnRollups.add("field = 'my." + field.toLowerCase() + "'");
                    }
                }
            }
        } else {
            xnRollups.clear();
        }
        xnIsrollup = true;
        return this;
    }
    public XN_Query group(String field) {
        if (StringUtils.isNotEmpty(field)) {
            if(Arrays.asList("id", "title", "author", "published", "updated", "application").contains(field)
                    || field.startsWith("my.")
                    || field.startsWith("updated")
                    || field.startsWith("published")) {
                xnGroups.add("field = '" + field.toLowerCase() + "'");
            }else {
                xnGroups.add("field = 'my." + field.toLowerCase() + "'");
            }
            xnIsrollup = true;
        }
        return this;
    }
    public XN_Query filter(String filter){
        if (StringUtils.isNotEmpty(filter)) {
            xnFilters.add(filter);
        }
        return this;
    }
    public XN_Query filter(String field, String operator, Object value){
        if (StringUtils.isNotEmpty(operator) && StringUtils.isNotEmpty(field)) {
            String filter = XN_Filter.filter(field,operator,value);
            xnFilters.add(filter);
        }
        return this;
    }
    public XN_Query removeFilter(String filter){
        if (StringUtils.isNotEmpty(filter)) {
            xnFilters = xnFilters.stream().filter( v -> {
                if (v.toString().startsWith(filter)) {
                  return false;
                }
                return true;
            }).collect(Collectors.toList());
        }
        return this;
    }

    private String getUrl() throws Exception{
        String url = "";
        if (xnSubject.equalsIgnoreCase("content")) {
            xnDatatype = 0;
            url =  "/content(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("content_count") == 0) {
            xnDatatype = -1;
            url =  "/content(" + StringUtils.join(xnFilters,"&") + ")";
            if (xnIsrollup) {
                url +=  "/rollup(" + StringUtils.join(xnRollups,"&") + ")";
                url +=  "/group(" + StringUtils.join(xnGroups,"&") + ")";
            }
        } else if (xnSubject.toLowerCase().compareTo("bigcontent") == 0) {
            xnDatatype = 1;
            url =  "/bigcontent(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("mq") == 0) {
            xnDatatype = 2;
            url =  "/mq(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("maincontent") == 0) {
            xnDatatype = 4;
            url =  "/maincontent(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("maincontent_count") == 0) {
            xnDatatype = -1;
            url =  "/maincontent(" + StringUtils.join(xnFilters,"&") + ")";
            if (xnIsrollup) {
                url +=  "/rollup(" + StringUtils.join(xnRollups,"&") + ")";
                url +=  "/group(" + StringUtils.join(xnGroups,"&") + ")";
            }
        } else if (xnSubject.toLowerCase().compareTo("schedule") == 0) {
            xnDatatype = 5;
            url =  "/schedule(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("message_count") == 0) {
            xnDatatype = -1;
            url =  "/message(" + StringUtils.join(xnFilters,"&") + ")";
            if (xnIsrollup) {
                url +=  "/rollup(" + StringUtils.join(xnRollups,"&") + ")";
                url +=  "/group(" + StringUtils.join(xnGroups,"&") + ")";
            }
        } else if (xnSubject.toLowerCase().compareTo("message") == 0) {
            xnDatatype = 6;
            url =  "/message(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("yearcontent") == 0) {
            xnDatatype = 7;
            url =  "/yearcontent(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("yearcontent_count") == 0) {
            xnDatatype = -1;
            url =  "/yearcontent(" + StringUtils.join(xnFilters,"&") + ")";
            if (xnIsrollup) {
                url +=  "/rollup(" + StringUtils.join(xnRollups,"&") + ")";
                url +=  "/group(" + StringUtils.join(xnGroups,"&") + ")";
            }
        }  else if (xnSubject.toLowerCase().compareTo("yearmonthcontent") == 0) {
            xnDatatype = 9;
            url =  "/yearmonthcontent(" + StringUtils.join(xnFilters,"&") + ")";
        }  else if (xnSubject.toLowerCase().compareTo("yearmonthcontent_count") == 0) {
            xnDatatype = -1;
            url =  "/yearmonthcontent(" + StringUtils.join(xnFilters,"&") + ")";
            if (xnIsrollup) {
                url +=  "/rollup(" + StringUtils.join(xnRollups,"&") + ")";
                url +=  "/group(" + StringUtils.join(xnGroups,"&") + ")";
            }
        } else if (xnSubject.toLowerCase().compareTo("profile") == 0) {
            xnDatatype = -1;
            url =  "/profile(" + StringUtils.join(xnFilters,"&") + ")";
        } else if (xnSubject.toLowerCase().compareTo("profile_count") == 0) {
            xnDatatype = -1;
            url =  "/profile(" + StringUtils.join(xnFilters,"&") + ")";
            if (xnIsrollup) {
                url +=  "/rollup(" + StringUtils.join(xnRollups,"&") + ")";
                url +=  "/group(" + StringUtils.join(xnGroups,"&") + ")";
            }
        } else {
            throw new Exception("XN_Query.execute datatype parameter error");
        }
        return url;
    }
    public List execute() throws Exception{
        try {
            String url = getUrl();

            url +=  "?from=" + xnBegin + "&to=" + xnEnd;
            if (!xnOrders.isEmpty()) {
                url +=  "&" + StringUtils.join(xnOrders,"&");
            }
            if (xnAlwaysReturnTotalCount) {
                url += "&count=true";
            } else {
                url += "&count=false";
            }
            url += "&xn_out=json";

            log.info(ColorUtils.blue() + "XN_Query.execute" +  ColorUtils.white() + "["+ColorUtils.yellow() + "{}" + ColorUtils.white() +"]{}",XN_Rest.getApplication(),url);

            String accessToken = XN_Credential.get();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (!xnTag.isEmpty()) {
                headers.put("tag",xnTag);
            }
            Map cipher;
            try {
                cipher = XN_Fetch.get(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.get(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            } catch (Exception e) {
                throw e;
            }

            if (xnSubject.toLowerCase().compareTo("profile") == 0) {
                FetchResult fetchresult = Profile.mapToProfiles(accessToken,cipher);
                xnSize = fetchresult.size;
                return fetchresult.entery;
            } else {
                FetchResult fetchresult = Content.mapToContents(accessToken,cipher,xnDatatype);
                xnSize = fetchresult.size;
                return fetchresult.entery;
            }

        } catch (Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
}
