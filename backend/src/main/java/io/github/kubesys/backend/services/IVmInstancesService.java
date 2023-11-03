package io.github.kubesys.backend.services;

import java.util.Map;

/**
 * @author bingshuai@nj.iscas.ac.cn
 * @since 11.01
 */
public interface IVmInstancesService  {

    Integer startWebsockifyServer() throws Exception;

    void startVm(String record) throws Exception;
    void restartVm(String record) throws Exception;
    void stopVm(String record) throws Exception;
    void forceStopVm(String record) throws Exception;

    void createDiskImageFromDisk(Map<String, Object> Request) throws Exception;

}
