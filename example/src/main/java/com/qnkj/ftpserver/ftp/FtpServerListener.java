package com.qnkj.ftpserver.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.*;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FtpServerListener extends DefaultFtplet {

    /**
     * 开始连接
     */
    @Override
    public FtpletResult onConnect(FtpSession session) throws FtpException,IOException {
        UUID sessionId = session.getSessionId();
        if(sessionId!=null) {
            log.info("{} is logging in to the FTP server", session.getClientAddress().getAddress().toString());
        }
        return super.onConnect(session);
    }

}
