package com.qnkj.core.base.controller;

import com.github.restapi.models.WebException;
import com.qnkj.common.utils.ChinaAreaUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "框架：中国行政区划查询")
public class ChinaAreaController {

    @GetMapping("/chinaarea/{keyword}")
    @ApiOperation("查询")
    public Object search(@PathVariable String keyword) throws WebException {
        log.info("中国行政区划查询: " + keyword);
        return ChinaAreaUtils.search(keyword);
    }


}
