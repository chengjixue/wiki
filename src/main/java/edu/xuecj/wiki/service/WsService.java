package edu.xuecj.wiki.service;

import edu.xuecj.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 17:35
 */
@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message,String logId){
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }
}
