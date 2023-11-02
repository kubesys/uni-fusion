package com.qnkj.core.plugins.file.service;


import com.qnkj.core.plugins.file.entity.UploadFile;
import com.qnkj.core.plugins.file.entity.UploadResult;

/**
 * @author Oldhand
 */
public interface IUploadFileService {

    /**
     * load 文件信息
     *
     * @param uniquekey
     * @return 文件信息
     */
    UploadFile load(String uniquekey) throws Exception;

    /**
     * load 文件信息
     *
     * @param url
     * @return 文件信息
     */
    UploadFile loadByUrl(String url) throws Exception;

    /**
     * 保存文件信息
     *
     * @param uploadResult
     * @return 文件信息
     */
    UploadFile create(UploadResult uploadResult) throws Exception;

    void delete(String uniquekey);
}
