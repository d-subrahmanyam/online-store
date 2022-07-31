package io.subbu.estore.web.listeners;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
public class LocalHttpSessionListener implements HttpSessionListener {
    /**
     * Notification that a session was created.
     * The default implementation is a NO-OP.
     *
     * @param httpSessionEvent the notification event
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSessionListener.super.sessionCreated(httpSessionEvent);
        log.info("Http Session Created with [id = {}]", httpSessionEvent.getSession().getId());
    }

    /**
     * Notification that a session is about to be invalidated.
     * The default implementation is a NO-OP.
     *
     * @param httpSessionEvent the notification event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSessionListener.super.sessionDestroyed(httpSessionEvent);
        log.info("Http Session Destroyed with [id = {}]", httpSessionEvent.getSession().getId());
    }
}
