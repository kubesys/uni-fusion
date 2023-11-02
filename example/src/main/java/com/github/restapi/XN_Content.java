package com.github.restapi;

import com.github.restapi.models.Content;
import com.github.restapi.models.FetchMsgException;
import com.github.restapi.models.FetchResult;
import com.github.restapi.utils.ColorUtils;
import com.github.restapi.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Oldhand
 **/

@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
@Slf4j
public class XN_Content {

    public static String transdatatype(int datatype) {
        if (datatype == 0) {
            return  "/content";
        } else if (datatype == 1) {
            return "/bigcontent";
        } else if (datatype == 2) {
            return "/mq";
        } else if (datatype == 4) {
            return "/maincontent";
        } else if (datatype == 5) {
            return "/schedule";
        } else if (datatype == 6) {
            return "/message";
        } else if (datatype == 7) {
            return "/yearcontent";
        } else if (datatype == 9) {
            return "/yearmonthcontent";
        }
        return "";
    }

    public static Content create(String xnType, String title) {
        return create(xnType,title,"",0);
    }
    public static Content create(String xnType,String title,String author) {
        return create(xnType,title,author,0);
    }

    /**
     * 创建一条新记录
     * @param xnType 表名
     * @param title 空
     * @param author 谁创建的
     * @param datatype 数据类型
     * @return
     */
    public static Content create(String xnType,String title,String author,int datatype){
        Content content = new Content(xnType,title,datatype);
        content.application = XN_Rest.getApplication();
        if (author == null || author.isEmpty()) {
            if (XN_Rest.getViewer() != null && !XN_Rest.getViewer().isEmpty()) {
                author = XN_Rest.getViewer();
            }
        }
        content.author = author;
        return content;
    }
    public static Content load(String id) throws Exception {
        return load(id,"",0);
    }

    /**
     * 去中台根据id,查找该条记录
     * @param id
     * @param tag
     * @return
     * @throws Exception
     */
    public static Content load(String id, String tag) throws Exception {
        return load(id,tag,0);
    }

    /**
     * 根据id获取这条记录
     * @param id
     * @param tag 缓存标签
     * @param datatype 数据类型
     * @return
     * @throws Exception
     */
    public static Content load(String id, String tag, int datatype) throws Exception {
        log.info(ColorUtils.blue() + "XN_Content.load" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](id:'" + id + "',tag:'" + tag + "',datatype:'" + datatype + "')");
        try {
            if (id.isEmpty()) {
                throw new Exception("XN_Content.load record id cannot be empty");
            }
            String url = transdatatype(datatype);
            if (url.isEmpty()) {
                throw new Exception("XN_Content.load datatype parameter error");
            }
            url += "(id="+id+")";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map<?,?> cipher;
            try {
                cipher = XN_Fetch.get(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.get(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            }
            FetchResult entry = Content.mapToContents(accessToken,cipher,datatype);
            if (!entry.entery.isEmpty()) {
                return (Content)entry.entery.get(0);
            }
            throw new Exception("no Content");
        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }

    public static List loadMany(List<String> ids, String tag) throws Exception {
        return loadMany(ids, tag,0);
    }
    public static List loadMany(List<String> ids, String tag, int datatype) throws Exception {
        log.info(ColorUtils.blue() + "XN_Content.loadMany" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](ids:" + ids.toString() + ",tag:'" + tag + "',datatype:'" + datatype + "',)");
        try {
            if (ids.isEmpty()) {
                throw new Exception("XN_Content.loadMany ids cannot be empty");
            }
            String url = transdatatype(datatype);
            if (url.isEmpty()) {
                throw new Exception("XN_Content.loadMany datatype parameter error");
            }
            url += "(id in ["+ StringUtils.join(ids,",") +"])";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            Map<?,?> cipher;
            try {
                cipher = XN_Fetch.get(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher = XN_Fetch.get(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            }
            FetchResult entry = Content.mapToContents(accessToken,cipher,datatype);
            return entry.entery;

        } catch(Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }

    public static void delete(String id, String tag) throws Exception {
        delete(id,tag,0);
    }
    public static void delete(String id, String tag, int datatype) throws Exception {
        if(id != null && !id.isEmpty()) {
            selfDelete(Collections.singletonList(id),tag,datatype);
        }
    }

    public static void delete(List<Object> contents, String tag) throws Exception {
        delete(contents,tag,0);
    }
    public static void delete(List<Object> contents, String tag, int datatype) throws Exception {
        if(contents == null || contents.size() <= 0) {
            return;
        }
        if(contents.size() > 100){
            int page = 0, index = 0, limit = 0;
            do{
                if((page+1)*100 > contents.size()) {
                    limit = page*100 + contents.size() - index;
                } else {
                    limit = (page+1)*100;
                }
                List<Object> sub = contents.subList(page*100,limit);
                List<String> ids = new ArrayList<>();
                sub.forEach( item -> {
                    if(item instanceof String){
                        ids.add(item.toString());
                    }else if(item instanceof Content){
                        ids.add(((Content) item).id);
                    }
                });
                selfDelete(ids,tag,datatype);
                index += sub.size();
                page++;
                sub.clear();
                ids.clear();
            }
            while (index < contents.size());
        } else {
            List<String> ids = new ArrayList<>();
            contents.forEach( item -> {
                if(item instanceof String){
                    ids.add(item.toString());
                }else if(item instanceof Content){
                    ids.add(((Content) item).id);
                }
            });
            selfDelete(ids,tag,datatype);
            ids.clear();
        }
    }
    private static void selfDelete(List<String> ids, String tag, int datatype) throws Exception{
        try {
            log.info(ColorUtils.blue() + "XN_Content.delete" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](ids:" + ids.toString() + "',tag:'" + tag + "',datatype:'" + datatype + "')");
            String url = XN_Content.transdatatype(datatype);
            if (url.isEmpty()) {
                throw new Exception("XN_Content.delete datatype parameter error");
            }
            if (ids.isEmpty()) {
                throw new Exception("XN_Content.delete id parameter error");
            }
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "(id in ["+ String.join(",", ids) +"])";
            url += "?xn_out=json";

            String accessToken = XN_Credential.get();
            Map<?,?> cipher;
            try {
                cipher =  XN_Fetch.delete(url,accessToken,headers);
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        cipher =  XN_Fetch.delete(url,accessToken,headers);
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            }
            if (cipher.containsKey("cipher") && cipher.get("cipher").toString().compareTo("") != 0) {
                String ciphertext = cipher.get("cipher").toString();
                String publickey = "";
                if (StringUtils.isNotEmpty(accessToken) && accessToken.compareTo("closed") != 0) {
                    publickey = XN_Credential.getPublicKey();
                }
                Map<?,?> decryptbody =  CryptoUtils.restDecrypt(ciphertext, publickey);
                if (decryptbody.containsKey("code") && decryptbody.get("code").toString().compareTo("0") == 0) {
                    Map<?,?> body = (Map<?,?>)decryptbody.get("body");
                    if (body.containsKey("code") && Integer.parseInt(body.get("code").toString()) == 0) {
                        return;
                    }
                }
            } else if (accessToken.compareTo("closed") == 0) {
                if (cipher.containsKey("code") && Integer.parseInt(cipher.get("code").toString()) == 0) {
                    return;
                }
            }
            throw new Exception("delete failure");
        } catch (Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
    /**
     * 分割数组
     * @param array 原数组
     * @param Size  分割后每个数组的最大长度
     * @return
     */
    private static  List<List> splitArray(List array, int Size) {
        List<List> lists = new ArrayList<>();
        int i = (array.size()) % Size == 0 ? (array.size()) / Size : (array.size()) / Size + 1;
        for (int j = 0; j < i; j++) {
            List<Object> list1 = new ArrayList<Object>();
            for (int k = 0; k < Size; k++) {
                if ((j * Size + k) >= array.size()) {
                    break;
                } else {
                    list1.add(array.get(j * Size + k));
                }
            }
            lists.add(list1);
        }
        return lists;
    }

    public static List batchsave(List<?> contents, String tag) throws Exception{
        return batchsave(contents,tag,0);
    }

    public static List batchsave(List<?> contents, String tag, int datatype) throws Exception{
        if (contents.size() <= 500) {
            return batch_save(contents,tag,datatype);
        } else {
            List<List> lists = splitArray(contents,500);
            List<Object> results = new ArrayList<>();
            for(List<?> items : lists) {
                results.addAll(batch_save(items,tag,datatype));
            }
            return results;
        }
    }

    private static List batch_save(List<?> contents, String tag, int datatype) throws Exception{
        try {
            log.info(ColorUtils.blue() + "XN_Content.save" +  ColorUtils.white() + "["+ColorUtils.yellow() + XN_Rest.getApplication() + ColorUtils.white() + "](tag:'" + tag + "',datatype:'" + datatype + "')");
            String url = XN_Content.transdatatype(datatype);
            if (url.isEmpty()) {
                throw new Exception("XN_Content.batchsave Data type parameter error");
            }
            Map<String, String> headers = new HashMap<>(1);
            headers.put("domain",XN_Rest.getApplication());
            if (StringUtils.isNotEmpty(tag)) {
                headers.put("tag",tag);
            }
            url += "?xn_out=json";
            String accessToken = XN_Credential.get();
            List<Map<String, Object>> entrys = new ArrayList<>();
            boolean isModify = false;
            for (Object content : contents) {
                Content item = (Content) content;
                entrys.add(item.toJson());
                if (item.id.isEmpty()) {
                    isModify = true;
                } else {
                    if (isModify) {
                        throw new Exception("XN_Content.batchsave operation type must be unified");
                    }
                }
            }
            Map<String, Object> sendbody = new HashMap<>(1);
            sendbody.put("size",contents.size());
            sendbody.put("entry", entrys);
            Map<?,?> cipher;
            try {
                if (isModify) {
                    cipher =  XN_Fetch.post(url,accessToken,headers,sendbody);
                } else {
                    cipher =  XN_Fetch.put(url,accessToken,headers,sendbody);
                }
            } catch (FetchMsgException fme) {
                if (fme.isNeedFlush(accessToken)) {
                    try {
                        accessToken = XN_Credential.flush();
                        if (isModify) {
                            cipher =  XN_Fetch.post(url,accessToken,headers,sendbody);
                        } else {
                            cipher =  XN_Fetch.put(url,accessToken,headers,sendbody);
                        }
                    } catch (FetchMsgException fe) {
                        throw new Exception(fe.errormsg);
                    }
                } else {
                    throw new Exception(fme.errormsg);
                }
            }
            FetchResult entry = Content.mapToContents(accessToken,cipher,datatype);
            return entry.entery;
        } catch (Exception e) {
            if (e.getMessage().contains("Input length not multiple of 8 bytes")) {
                XN_Credential.clear();
            }
            throw e;
        }
    }
}
