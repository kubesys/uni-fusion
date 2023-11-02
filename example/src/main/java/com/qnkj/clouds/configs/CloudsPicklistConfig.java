package com.qnkj.clouds.configs;

import com.qnkj.common.configs.BasePicklistConfig;
import com.qnkj.common.entitys.PickList;
import com.qnkj.common.entitys.PickListEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * create by 徐雁
 * create date 2021/8/20
 */

@Slf4j
@Component("CloudsPicklistConfig")
public class CloudsPicklistConfig {
    @PostConstruct
    public void init() {
        BasePicklistConfig.addBuiltinPicklists(
                Arrays.asList(
                        new PickList().picklistname("vmarchitecture").picklistlabel("CPU架构").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("x86_64").strval("x86_64").intval(1),
                                        new PickListEntity().label("aarch64").strval("aarch64").intval(2).sequence(1),
                                        new PickListEntity().label("mips64el").strval("mips64el").intval(3).sequence(2)
                                )
                        ),
                        new PickList().picklistname("vmbootmode").picklistlabel("BIOS模式").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("Legacy").strval("Legacy").intval(1),
                                        new PickListEntity().label("UEFI").strval("UEFI").intval(2).sequence(1)
                                )
                        ),
                        new PickList().picklistname("vmimageservertype").picklistlabel("类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("ftp").strval("ftp").intval(1)
                                )
                        ),
                        new PickList().picklistname("vmmediatype").picklistlabel("镜像格式").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("qcow2").strval("qcow2").intval(1),
                                        new PickListEntity().label("iso").strval("iso").intval(2).sequence(1)
                                )
                        ),
                        new PickList().picklistname("vmplatform").picklistlabel("平台").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("Linux").strval("Linux").intval(1),
                                        new PickListEntity().label("Windows").strval("Windows").intval(2).sequence(1),
                                        new PickListEntity().label("Other").strval("Other").intval(3).sequence(2)
                                )
                        ),
                        new PickList().picklistname("vmtype").picklistlabel("来源").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("localfs").strval("localfs").intval(1),
                                        new PickListEntity().label("nfs").strval("nfs").intval(2).sequence(1),
                                        new PickListEntity().label("uus").strval("uus").intval(3).sequence(2),
                                        new PickListEntity().label("glusterfs").strval("glusterfs").intval(4).sequence(3),
                                        new PickListEntity().label("vdiskfs").strval("vdiskfs").intval(5).sequence(4),
                                        new PickListEntity().label("dir").strval("dir").intval(6).sequence(5)
                                )
                        ),
                        new PickList().picklistname("vmcontent").picklistlabel("类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("vmd").strval("vmd").intval(1),
                                        new PickListEntity().label("iso").strval("iso").intval(2).sequence(1),
                                        new PickListEntity().label("vmdi").strval("vmdi").intval(3).sequence(2)
                                )
                        ),
                        new PickList().picklistname("vmstate").picklistlabel("就绪状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已关机").strval("Shutdown").intval(5).sequence(4),
                                        new PickListEntity().label("运行中").strval("Running").intval(6).sequence(5)
                                )
                        ),
                        new PickList().picklistname("vmimageserverstate").picklistlabel("镜像服务器状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已连接").strval("connected").intval(2).sequence(1),
                                        new PickListEntity().label("未连接").strval("notconnected").intval(3).sequence(2)
                                )
                        ),
                        new PickList().picklistname("vmimagestate").picklistlabel("镜像状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已就绪").strval("Ready").intval(2).sequence(1),
                                        new PickListEntity().label("未就绪").strval("NotReady").intval(3).sequence(2)
                                )
                        ),
                        new PickList().picklistname("vmdiskstate").picklistlabel("云盘状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已就绪").strval("Ready").intval(2).sequence(1),
                                        new PickListEntity().label("未就绪").strval("NotReady").intval(3).sequence(2)
                                )
                        ),
                        new PickList().picklistname("vmzonestate").picklistlabel("区域连接状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已就绪").strval("Ready").intval(2).sequence(1),
                                        new PickListEntity().label("未就绪").strval("NotReady").intval(3).sequence(2),
                                        new PickListEntity().label("连接中").strval("Connecting").intval(3).sequence(3)
                                )
                        ),
                        new PickList().picklistname("vmpoolstate").picklistlabel("主存储状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已就绪").strval("active").intval(2).sequence(1),
                                        new PickListEntity().label("未就绪").strval("notactive").intval(3).sequence(2)
                                )
                        ),

                        new PickList().picklistname("virtualization").picklistlabel("虚拟化技术").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("KVM").strval("kvm").intval(2).sequence(1)
                                )
                        ),
                        new PickList().picklistname("vmphysicalmachinestate").picklistlabel("物理机状态").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("已就绪").strval("Ready").intval(2).sequence(1),
                                        new PickListEntity().label("未就绪").strval("NotReady").intval(3).sequence(2)
                                )
                        ),


                        new PickList().picklistname("virttype").picklistlabel("虚拟化类型").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("kvm").strval("kvm").intval(1),
                                        new PickListEntity().label("xen").strval("xen").intval(2).sequence(1)
                                )
                        ),
                        new PickList().picklistname("vmosvariant").picklistlabel("操作系统").picklistentitys(
                                Arrays.asList(
                                        new PickListEntity().label("centos7.0").strval("centos7.0").intval(1),
                                        new PickListEntity().label("centos8.0").strval("centos8.0").intval(2).sequence(1),
                                        new PickListEntity().label("win7").strval("win7").intval(3).sequence(2),
                                        new PickListEntity().label("win10").strval("win10").intval(4).sequence(3)
                                )
                        )
                )
        );
    }
}
