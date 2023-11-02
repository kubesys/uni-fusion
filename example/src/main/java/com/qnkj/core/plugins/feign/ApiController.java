package com.qnkj.core.plugins.feign;

import com.qnkj.core.plugins.feign.authorization.TokenAuthEntity;
import feign.Headers;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@Component
public interface ApiController {

    @PostMapping("/api/generator/{module}")
    @ApiOperation("生成API模块代码")
    Map<String, Object> generatorApiCode(@PathVariable("module")String module);

    @DeleteMapping("/api/generator/{module}")
    @ApiOperation("清除模块配置缓存信息")
    Map<String, Object> clearModuleCacheInfo(@PathVariable("module")String module);

    @DeleteMapping("/api/generator")
    @ApiOperation("清除所有模块配置缓存信息")
    Map<String, Object> clearAllModuleCacheInfo();

    @PostMapping("/auth/credential")
    @ApiOperation("Token认证")
    @Headers("Content-Type: application/json")
    Map<String, Object> authCredential(@RequestBody TokenAuthEntity auth);

}
