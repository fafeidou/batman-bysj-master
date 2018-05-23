package com.batman.bysj.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author victor.qin
* @date 2018/5/23 11:12
*/
public abstract class Loggable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    protected boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    protected boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    protected boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    protected void $debug(String var1) {
        logger.debug(var1);
    }

    protected <T> void $debug(T var1) {
        logger.debug(String.valueOf(var1));
    }

    protected void $debug(String var1, Object var2) {
        logger.debug(var1, var2);
    }

    protected void $debug(String var1, Object var2, Object var3) {
        logger.debug(var1, var2, var3);
    }

    protected void $debug(String var1, Object... var2) {
        logger.debug(var1, var2);
    }

    protected void $debug(String var1, Throwable var2) {
        logger.debug(var1, var2);
    }

    public void $info(String var1) {
        logger.info(var1);
    }

    public <T> void $info(T var1) {
        logger.info(String.valueOf(var1));
    }

    public void $info(String var1, Object var2) {
        logger.info(var1, var2);
    }

    public void $info(String var1, Object var2, Object var3) {
        logger.info(var1, var2, var3);
    }

    public void $info(String var1, Object... var2) {
        logger.info(var1, var2);
    }

    public void $info(String var1, Throwable var2) {
        logger.info(var1, var2);
    }

    public void $warn(String var1) {
        logger.warn(var1);
    }

    public <T> void $warn(T var1) {
        logger.warn(String.valueOf(var1));
    }

    public void $warn(String var1, Object var2) {
        logger.warn(var1, var2);
    }

    public void $warn(String var1, Object var2, Object var3) {
        logger.warn(var1, var2, var3);
    }

    public void $warn(String var1, Object... var2) {
        logger.warn(var1, var2);
    }

    public void $warn(String var1, Throwable var2) {
        logger.warn(var1, var2);
    }

    public void $error(String var1) {
        logger.error(var1);
    }

    public <T> void $error(T var1) {
        logger.error(String.valueOf(var1));
    }

    public void $error(String var1, Object var2) {
        logger.error(var1, var2);
    }

    public void $error(String var1, Object var2, Object var3) {
        logger.error(var1, var2, var3);
    }

    public void $error(String var1, Object... var2) {
        logger.error(var1, var2);
    }

    public void $error(String var1, Throwable var2) {
        logger.error(var1, var2);
    }
}