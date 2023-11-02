package com.qnkj.ftpserver.ftp;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class MyFtpClient {
    private FTPClient ftpClient = null; //FTP 客户端代理
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm"); //时间格式化

    /**
     * 测试连接到服务器
     * @param ip 服务器地址IP地址
     * @param port 端口
     * @param userName 登录用户名
     * @param password 登录密码
     * @return true 连接服务器成功，false 连接服务器失败
     */
    public static boolean testConnectServer(String ip,int port,String userName,String password) {
        MyFtpClient client = new MyFtpClient();
        if (client.connectServer(ip,port,userName,password)) {
            client.closeConnect();
            return true;
        }
        client.closeConnect();
        return false;
    }

    /**
     * 连接到服务器
     * @param ip 服务器地址IP地址
     * @param port 端口
     * @param userName 登录用户名
     * @param password 登录密码
     * @return true 连接服务器成功，false 连接服务器失败
     */
    public boolean connectServer(String ip,int port,String userName,String password) {
        if (ftpClient != null) {
            this.closeConnect();
        }
        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("UTF8");
            ftpClient.connect(ip,port);
            ftpClient.login(userName, password);
            int reply = ftpClient.getReplyCode();
            ftpClient.setDataTimeout(120000);
            if (FTPReply.isPositiveCompletion(reply)) {
                log.info("ftp server connection successful.");
                 return true;
            }
            ftpClient.disconnect();
            log.error("ftp server refused connection.");
        } catch (SocketException e) {
            e.printStackTrace();
            log.error("Login to ftp server (" + ip + ") failed,Connection timed out!");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Login to ftp server (" + ip + ") failed,The server cannot connect!");
        }
        return false;
    }

    /**
     * 上传文件
     * @param remoteFile 远程文件路径,支持多级目录嵌套
     * @param localFile 本地文件名称，绝对路径
     */
    public boolean uploadFile(String remoteFile, File localFile)  throws IOException {
        boolean isSuccessed = false;
        InputStream in = new FileInputStream(localFile);
        String remote = new String(remoteFile.getBytes("GBK"),"iso-8859-1");
        if(ftpClient.storeFile(remote, in)){
            isSuccessed = true;
            log.info(localFile.getAbsolutePath()+" upload successful.");
        }else{
            log.error(localFile.getAbsolutePath()+" upload failed.");
        }
        in.close();
        return isSuccessed;
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param local--本地文件路径
     * @param remote--新的文件名,可以命名为空""
     * @return true 上传成功，false 上传失败
     * @throws IOException
     */
    public boolean uploadFile(String local, String remote) throws IOException {
        boolean isSuccessed = true;
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            // 创建服务器远程目录结构，创建失败直接返回
            if (!CreateDirecroty(remote)) {
                return false;
            }
        }
        FTPFile[] files = ftpClient.listFiles(new String(remoteFileName));
        File f = new File(local);
        if(!uploadFile(remoteFileName, f)){
            isSuccessed = false;
        }
        return isSuccessed;
    }

    /**
     * 上传文件夹内的所有文件
     * @param filename 本地文件夹绝对路径
     * @param uploadpath 上传到FTP的路径,形式为/或/dir1/dir2/../
     * @return true 上传成功，false 上传失败
     * @throws IOException
     */
    public List uploadManyFile(String filename, String uploadpath) {
        boolean flag = true;
        List l = new ArrayList();
        StringBuffer strBuf = new StringBuffer();
        int n = 0; //上传失败的文件个数
        int m = 0; //上传成功的文件个数
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.changeWorkingDirectory("/");
            File file = new File(filename);
            File fileList[] = file.listFiles();
            for (File upfile : fileList) {
                if (upfile.isDirectory()) {
                    uploadManyFile(upfile.getAbsoluteFile().toString(),uploadpath);
                } else {
                    String local = upfile.getCanonicalPath().replaceAll("\\\\","/");
                    String remote = uploadpath.replaceAll("\\\\","/") + local.substring(local.indexOf("/") + 1);
                    flag = uploadFile(local, remote);
                    ftpClient.changeWorkingDirectory("/");
                }
                if (!flag) {
                    n++;
                    strBuf.append(upfile.getName() + ",");
                    log.error(upfile.getName() + " upload failed.");
                } else{
                    m++;
                }
            }
            l.add(0, n);
            l.add(1, m);
            l.add(2, strBuf.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.error("Upload failed: {}", e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Upload failed: {}", e);
        }
        return l;
    }

    /**
     * 下载文件
     * @param remoteFileName  --服务器上的文件名
     * @param localFileName   --本地文件名
     * @return true 下载成功，false 下载失败
     */
    public boolean downloadFile(String remoteFileName, String localFileName) {
        boolean flag = true;
        BufferedOutputStream buffOut = null;
        try {
            buffOut = new BufferedOutputStream(new FileOutputStream(localFileName));
            flag = ftpClient.retrieveFile(remoteFileName, buffOut);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Download failed: {}", e);
        } finally {
            try {
                if (buffOut != null) {
                    buffOut.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 删除一个文件
     */
    public boolean deleteFile(String filename) {
        try {
            if (ftpClient.deleteFile(filename)) {
                log.info(filename + " delete successful.");
                return true;
            } else {
                log.error(filename + "delete failed.");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    /**
     * 删除目录
     */
    public void deleteDirectory(String pathname) {
        try {
            File file = new File(pathname);
            if (file.isDirectory()) {
                File file2[] = file.listFiles();
            } else {
                deleteFile(pathname);
            }
            ftpClient.removeDirectory(pathname);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 删除空目录
     */
    public void deleteEmptyDirectory(String pathname) {
        try {
            ftpClient.removeDirectory(pathname);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 列出服务器上文件和目录
     *
     * @param regStr --匹配的正则表达式
     */
    public List<String> listRemoteFiles(String regStr) {
        try {
            String files[] = ftpClient.listNames(regStr);
            if (files != null && files.length > 0) {
                return Arrays.asList(files);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 列出Ftp服务器上的所有文件和目录
     */
    public List<String> listRemoteAllFiles() {
        try {
            String[] names = ftpClient.listNames();
            if (names != null && names.length > 0) {
                return Arrays.asList(names);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 关闭连接
     */
    public void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置传输文件的类型[文本文件或者二进制文件]
     * @param fileType--BINARY_FILE_TYPE、ASCII_FILE_TYPE
     */
    public void setFileType(int fileType) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入到服务器的某个目录下
     * @param directory
     */
    public boolean changeWorkingDirectory(String directory) {
        try {
            if (ftpClient.changeWorkingDirectory(directory)) {
                log.debug("cd "+ directory + " ok.");
                return true;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    /**
     * 返回到上一层目录
     */
    public void changeToParentDirectory() {
        try {
            ftpClient.changeToParentDirectory();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 重命名文件
     * @param oldFileName --原文件名
     * @param newFileName --新文件名
     */
    public void renameFile(String oldFileName, String newFileName) {
        try {
            ftpClient.rename(oldFileName, newFileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 设置FTP客服端的配置--一般可以不设置
     * @return ftpConfig
     */
    private FTPClientConfig getFtpConfig() {
        FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);
        return ftpConfig;
    }

    /**
     * 转码[ISO-8859-1 -> GBK] 不同的平台需要不同的转码
     * @param obj
     * @return ""
     */
    private String iso8859togbk(Object obj) {
        try {
            if (obj == null) {
                return "";
            }
            return new String(obj.toString().getBytes("iso-8859-1"), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 在服务器上创建一个文件夹
     * @param dir 文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>...
     */
    public boolean makeDirectory(String dir) {
        try {
            if (ftpClient.makeDirectory(dir)) {
                log.debug("mkdir "+ dir + " ok.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查路径是否存在
     * @return true 连接服务器成功，false 连接服务器失败
     */
    public boolean existFile(String path) throws IOException {
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if(ftpFileArr.length > 0){
            return true;
        }
        return false;
    }

    /**
     * 递归创建远程服务器目录
     * @param remote 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/")&& !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"),"iso-8859-1");
                if (changeWorkingDirectory(subDirectory)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        log.error("mkdir "+subDirectory+" failed.");
                        success = false;
                        return success;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }
}
