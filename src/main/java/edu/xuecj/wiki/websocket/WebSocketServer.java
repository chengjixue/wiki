package edu.xuecj.wiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 16:29
 */
@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG= LoggerFactory.getLogger(WebSocketServer.class);
    /*
    * 每个客户端一个token
    * */
    private String token="";
    private static HashMap<String, Session> map=new HashMap<>();

    /*
    * 链接成功
    * */
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token){
        map.put(token,session);
        this.token=token;
        LOG.info("链接成功:token:{},session_id{},当前连接数{}",token,session.getId(),map.size());
    }
    /*
    * 连接关闭
    * */
    @OnClose
    public void onClose(Session session){
        map.remove(this.token);
        LOG.info("断开连接:token:{},session_id:{},当前连接数{}",this.token,session.getId(),map.size());
    }
    /*
    * 收到消息
    * */
    @OnMessage
    public void onMessage(String message,Session session){
        LOG.info("收到消息:{},内容：{}",token,message);
    }
    /*
    * 链接错误
    * */
    @OnError
    public void onError(Session session,Throwable error){
        LOG.error("错误:{}",error.getMessage());
    }
    /*
    * 群发消息
    * */
    public void sendInfo(String message){
        for(String token:map.keySet()){
            Session session=map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("推送消息失败:{}，内容{}",token,message);
            }
            LOG.info("推送消息成功:{}，内容{}",token,message);
        }
}

}
