package com.qnkj.clouds.services;

import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2023-06-27
 * @author Auto Generator
 */
public interface IKubeStackService {

    Boolean connectKubeStackClient(String zone,String config) throws Exception;

    void cleanKubeStackClientPools(String zone);

    Map<String,String> getVmNodes() throws Exception;

    List<Map<String,Object>> getVmPools() throws Exception;

    List<Map<String,Object>> getVmDisks() throws Exception;

    List<Map<String,Object>> getVmDiskImages() throws Exception;

    List<Map<String,Object>> getVmDiskSnapShots() throws Exception;

    List<Map<String,Object>> getVms() throws Exception;

    List<Map<String,Object>> getVmNetWorks() throws Exception;


    Boolean createVmPools(Map<String,Object> maps) throws Exception;
    Map<String,Object> getVmPoolByName(String zone,String name) throws Exception;
    Boolean deleteVmPools(String zone,String name,String vmPoolType,String eventid) throws Exception;

    Boolean createVmDisks(Map<String,Object> maps) throws Exception;
    Map<String,Object> getVmDiskByName(String zone,String name) throws Exception;
    Boolean deleteVmDisks(String zone,String name, String vmDiskType,String pool) throws Exception;

    Boolean createVmDiskImages(Map<String,Object> maps) throws Exception;
    Map<String,Object> getVmDiskImageByName(String zone,String name) throws Exception;
    Boolean deleteVmDiskImages(String zone,String name, String sourcePool) throws Exception;


    Boolean createVmInstances(Map<String,Object> maps) throws Exception;
    Map<String,Object> getVmInstanceByName(String zone,String name) throws Exception;
    Boolean deleteVmInstances(String zone,String name, String eventid) throws Exception;

    Boolean startVm(String zone,String name) throws Exception;
    Boolean restartVm(String zone,String name) throws Exception;
    Boolean stopVm(String zone,String name) throws Exception;
    Boolean forceStopVm(String zone,String name) throws Exception;


    Boolean createDiskImageFromDisk(Map<String,Object> maps) throws Exception;

    List<Map<String,Object>> getPhysicalMachiness() throws Exception;



    String getVmVncServiceIp(String zone,String name) throws Exception;


}
