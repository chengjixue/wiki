package edu.xuecj.wiki.utils;

import java.io.Serializable;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 11:06
 */
public class RequestContext implements Serializable {
    /*
    * 线程本地变量
    * 作用：随时保存当前线程的上下文信息
    *
    * */
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();
    public static String getRemoteAddr(){
        return remoteAddr.get();
    }
    public static void setRemoteAddr(String remoteAddr){
        RequestContext.remoteAddr.set(remoteAddr);
    }
}
