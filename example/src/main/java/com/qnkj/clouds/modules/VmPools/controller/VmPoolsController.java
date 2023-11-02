package com.qnkj.clouds.modules.VmPools.controller;

import com.qnkj.clouds.modules.VmPools.services.IVmPoolsService;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.webconfigs.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2023-06-26
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("VmPools")
@RequiredArgsConstructor
@Api(tags = "低代码演示：VmPools")
@Scope("prototype")
@RequestMapping("VmPools")
public class VmPoolsController {
    private final IVmPoolsService vmpoolsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }


    /**
     * 启动按钮
     **/
    @ApiOperation(value = "重连请求接口")
    @PostMapping("/startLink")
    @ResponseBody
    @Log("启动按钮请求")
    public Object startLink(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            if (httpRequest.containsKey("record") && Utils.isNotEmpty(httpRequest.get("record"))) {
                vmpoolsService.startLinkVmPool(httpRequest.get("record").toString());
                return new WebResponse().success("发起重连成功").refresh();
            }
            throw new Exception("无效的参数");
        } catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }

}
