package com.qnkj.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * @author 徐雁
 * create date 2022/2/13
 * create time 5:03 下午
 */
public class NetWorkUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetWorkUtils.class);
    /**
     * 正确的IP拿法，即优先拿site-local地址
     * @return InetAddress
     */
    private static InetAddress getLocalHostLanAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    public static String getLocalIp() {
        try {
            InetAddress address = getLocalHostLanAddress();
            return address.getHostAddress();
        }catch (Exception e) {
            logger.error("<getLocalIp> Error: {}", e.getMessage());
        }
        return "";
    }

    public static Boolean isNacosStart(String NacosServiceAddr) {
        String host = "localhost";
        int port = 8848;
        if(Utils.isNotEmpty(NacosServiceAddr)) {
            String[] info = NacosServiceAddr.split(":");
            if(info.length > 0){
                host = info[0];
                if(info.length > 1){
                    port = Integer.parseInt(info[1]);
                }
            }
        }
        return isHostConnectable(host,port);
    }
    public static Boolean isRabbitMqStart(String serviceAddr) {
        String host = "localhost";
        int port = 5672;
        if(Utils.isNotEmpty(serviceAddr)) {
            String[] info = serviceAddr.split(":");
            if(info.length > 0){
                host = info[0];
                if(info.length > 1){
                    port = Integer.parseInt(info[1]);
                }
            }
        }
        return isHostConnectable(host,port);
    }

    protected static Boolean isHostConnectable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
