package com.qnkj.core.plugins.file.entity;

import lombok.Data;

@Data
public class UploadResult {
    public String url;
    public String filename;
    public String filetype;//扩展名
    public String uniquekey;
    public int filesize;

    public UploadResult(String url,String filename, String filetype, String uniquekey, int filesize) {
        this.url = url;
        this.filename = filename;
        this.filetype = filetype;
        this.uniquekey = uniquekey;
        this.filesize = filesize;
    }
}
