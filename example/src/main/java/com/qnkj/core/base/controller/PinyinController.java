package com.qnkj.core.base.controller;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * create by oldhnd
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("pinyin")
@Api(tags = "框架：拼音模块")
public class PinyinController {

    @GetMapping("get")
    @ApiOperation("获取拼音")
    @ResponseBody
    public WebResponse get(@RequestParam(name="str")  String str) throws WebException {
        return new WebResponse().code(200).message("ok").data(PinyinUtil.getPinyin(str,""));
    }

}
