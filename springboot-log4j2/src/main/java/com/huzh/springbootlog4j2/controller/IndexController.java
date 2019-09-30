package com.huzh.springbootlog4j2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Date 2019/9/30 11:10
 * @Author huzh
 * @Version 1.0
 */
@RestController
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/index")
    public String index() {
        logger.debug("记录debug日志");
        logger.info("访问了index方法");
        logger.error("记录了error错误日志");
        return "index";
    }


}
