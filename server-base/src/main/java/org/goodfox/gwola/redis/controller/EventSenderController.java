package org.goodfox.gwola.redis.controller;

import org.goodfox.gwola.redis.bean.EventBean;
import org.goodfox.gwola.redis.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.goodfox.gwola.redis.bean.EventBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${restPath}/event")
@Api(description = "CMS文章")
public class EventSenderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventSenderController.class);

    @Autowired
    private EventService eventService;

    @ApiOperation(value = "发送事件")
    @RequestMapping("/send")
    public void sendEvent(@RequestBody EventBean eventBean) {
        eventService.createEvent(eventBean);
    }

}
