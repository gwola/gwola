package org.goodfox.gwola.redis.event.receiver;


import org.goodfox.gwola.redis.bean.EventBean;
import org.goodfox.gwola.redis.bean.constant.EventStatusEnum;
import org.goodfox.gwola.redis.event.hanlder.EventHandler;
import org.goodfox.gwola.redis.event.hanlder.EventHandlerFactory;
import org.goodfox.gwola.redis.service.SysEventEOService;
import org.goodfox.gwola.util.utils.GsonUtil;
import org.goodfox.gwola.util.utils.RedisUtil;
import org.goodfox.gwola.redis.bean.EventBean;
import org.goodfox.gwola.redis.bean.constant.EventStatusEnum;
import org.goodfox.gwola.redis.event.hanlder.EventHandler;
import org.goodfox.gwola.redis.event.hanlder.EventHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

public class EventReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventReceiver.class);

    private CountDownLatch latch;

    @Autowired
    private SysEventEOService sysEventEOService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public EventReceiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String message) {
        EventBean eventBean = GsonUtil.fromJson(message, EventBean.class);
        LOGGER.info("Received <{}>", message);
        LOGGER.info("Received Event({}, {}, {})", eventBean.getEventId(), eventBean.getEventCode(), eventBean.getEventName());

        // 加锁防止多个线程同时执行
        try {
            EventHandler eventHandler = EventHandlerFactory.getEventHandler(eventBean.getEventCode());
            if (eventHandler == null) {
                LOGGER.error("Cannot find handler");
                return;
            }

            // 检查是否锁住了资源，如果没锁住，则锁住资源
            boolean isCanOperate = redisUtil.lock(eventBean.getEventId());
            if (isCanOperate) {
                eventHandler.handler(eventBean);

                // 解锁资源
                redisUtil.unlock(eventBean.getEventId());
                sysEventEOService.updateEventStatus(eventBean.getEventId(), EventStatusEnum.DONE.getValue(), "");
            } else {
                // 资源已经被锁住了
                LOGGER.info("Event [{}] is locked", eventBean.getEventId());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            sysEventEOService.updateEventStatus(eventBean.getEventId(), EventStatusEnum.ERROR.getValue(), e.getMessage());
            throw e;
        }

        latch.countDown();
    }

}