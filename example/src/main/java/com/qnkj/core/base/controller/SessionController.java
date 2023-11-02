package com.qnkj.core.base.controller;

import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.entitys.ActiveUser;
import com.qnkj.core.base.services.ISessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
@RestController
@RequestMapping("session")
@RequiredArgsConstructor
@Api(tags = "框架：登录管理")
public class SessionController {

    private final ISessionService sessionService;

    @GetMapping("list")
    @RequiresPermissions("online:view")
    @ApiOperation("登录列表")
    public WebResponse list(String username) {
        List<ActiveUser> list = sessionService.list();
        Map<String, Object> data = new HashMap<>(2);
        data.put("rows", list);
        data.put("total", CollectionUtils.size(list));
        return new WebResponse().success().data(data);
    }

    @GetMapping("delete/{id}")
    @RequiresPermissions("user:kickout")
    @ApiOperation("踢出")
    public WebResponse forceLogout(@PathVariable(value="id") String id) {
        sessionService.forceLogout(id);
        return new WebResponse().success();
    }
}
