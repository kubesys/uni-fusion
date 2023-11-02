package com.qnkj.core.plugins.file.service.impl;


import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.plugins.file.entity.UploadFile;
import com.qnkj.core.plugins.file.entity.UploadResult;
import com.qnkj.core.plugins.file.service.IUploadFileService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Oldhand
 */
@Service
public class UploadFileServiceImpl implements IUploadFileService {

    @Override
    public UploadFile load(String uniquekey) throws Exception {
        XN_Query query = XN_Query.create ( "Content" ).tag("uploads")
                .filter( "type", "eic", "uploads" )
                .filter ("my.deleted", "=", 0)
                .filter ("my.uniquekey", "=",uniquekey)
                .begin(0)
                .end(1);
        List<Object> uploads = query.execute();
        if (!uploads.isEmpty() ) {
            return new UploadFile((Content)uploads.get(0));
        }
        return null;
    }

    @Override
    public void delete(String uniquekey) {
        try {
            XN_Query query = XN_Query.create ( "Content" ).tag("uploads")
                    .filter( "type", "eic", "uploads" )
                    .filter ("my.deleted", "=", 0)
                    .filter ("my.uniquekey", "=",uniquekey)
                    .begin(0)
                    .end(1);
            List<Object> uploads = query.execute();
            if(!uploads.isEmpty()) {
                XN_Content.delete(uploads, "uploads");
            }
        } catch(Exception ignored) {
        }
    }

    @Override
    public UploadFile loadByUrl(String url)  throws Exception {
        XN_Query query = XN_Query.create ( "Content" ).tag("uploads")
                .filter( "type", "eic", "uploads" )
                .filter ("my.deleted", "=", 0)
                .filter ("my.url", "=",url)
                .begin(0)
                .end(1);
        List<Object> uploads = query.execute();
        if (!uploads.isEmpty() ) {
            return new UploadFile((Content)uploads.get(0));
        }
        return null;
    }
    @Override
    public UploadFile create(UploadResult uploadResult) throws Exception {
        Content newcontent = XN_Content.create("uploads","","");
        newcontent.add("deleted","0");
        newcontent.add("uniquekey",uploadResult.uniquekey);
        newcontent.add("url",uploadResult.url);
        newcontent.add("filename",uploadResult.filename);
        newcontent.add("filesize",uploadResult.filesize);
        newcontent.add("filetype",uploadResult.filetype);
        newcontent.add("supplierid",SupplierUtils.getSupplierid());
        newcontent.save("uploads");
        return new UploadFile(newcontent);
    }


}
