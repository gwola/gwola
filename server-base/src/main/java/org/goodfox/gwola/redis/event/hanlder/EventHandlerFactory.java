package org.goodfox.gwola.redis.event.hanlder;

import org.goodfox.gwola.util.exception.GwolaBaseException;
import org.goodfox.gwola.util.utils.SpringContextHolder;
import org.goodfox.gwola.util.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件处理器工厂
 */
public class EventHandlerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandlerFactory.class);

    private static Class<EventHandler> getEventHandlerClass(String eventCode) {
        Class clazz = EventHandlerRegistry.getHandlerClass(eventCode);
        if (clazz == null) {
            throw new GwolaBaseException("获取事件[" + eventCode + "]的处理器失败");
        }
        LOGGER.info("Handler class of [{}] is [{}]", eventCode, clazz);
        return clazz;
    }

    public static EventHandler getEventHandler(String eventCode) {
        EventHandler eventHandler = null;
        Class<EventHandler> clazz = getEventHandlerClass(eventCode);
        if (clazz != null) {
            eventHandler = SpringContextHolder.getApplicationContext().getBean(clazz);
        }
        return eventHandler;
    }
}
