package com.sweethome.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.sweethome.entities.SocketMessage;
import com.sweethome.entities.UserEntity;
import com.sweethome.service.StudentService;
import com.sweethome.service.UserService;
import com.sweethome.utils.DateUtilSweet;
import com.sweethome.utils.RedisT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/clock/{uid}", subprotocols = {"WebSocket"})
@Slf4j
public class BaseSocketServer {

    private static RedisT redisT;

    private static UserService userService;

    @Autowired
    public void setRedisT(RedisT redisT) {
        BaseSocketServer.redisT = redisT;
    }

    @Autowired
    public void setUserService(UserService userService) {
        BaseSocketServer.userService = userService;
    }

    /**
     * 记录连接数
     */
    private static int onLineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static final ConcurrentHashMap<String, BaseSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收用户id
     */
    private String uid = "";

    public static synchronized int getOnLineCount() {
        return onLineCount;
    }

    public static synchronized void addOnLineCount() {
        BaseSocketServer.onLineCount++;
    }

    public static synchronized void subOnlineCount() {
        BaseSocketServer.onLineCount--;
    }

    /**
     * 连接建立成功调用的方法
     * @param session 保存消息 session
     * @param uid 用户id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        this.uid = uid;
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
        } else {
            addOnLineCount();
        }
        webSocketMap.put(uid, this);
        log.info("用户连接: {}, 当前在线人数为 : {}", uid, getOnLineCount());
        try {
            if (redisT.hasKey(uid)) {
                String s = (String)redisT.get(uid);
                SocketMessage message = JSON.parseObject(s, SocketMessage.class);
                sendMessage(JSON.toJSONString(message));
            }
        } catch (IOException ioException) {
            log.error("用户连接异常");
        }
    }

    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            subOnlineCount();
        }
        log.info("用户 ： {} 退出, 当前在线人数为 : {}", uid, getOnLineCount());
    }

    /**
     * 收到客户端（前端消息后调用的方法）
     * @param message 消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户 : {}, 发送消息: {}", uid, message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            try {
                // 解析发送的消息
                SocketMessage socketMessage = JSONObject.parseObject(message, SocketMessage.class);
                socketMessage.setUid(uid);
                log.info(socketMessage.toString());
                redisT.set(socketMessage.getToUid(), JSON.toJSONString(socketMessage), DateUtilSweet.getNowTo24Second());
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId", this.uid);
                String toUid = jsonObject.getString("toUid");
                if (StringUtils.isNotBlank(toUid) && webSocketMap.containsKey(toUid)) {
                    webSocketMap.get(toUid).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的uid ： {}, 不在该服务器上", toUid);
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendInfo(String message, @PathParam("uid") String uid) throws IOException {
        log.info("用户 : {}, 发送消息: {}", uid, message);
        if (StringUtils.isNotBlank(uid) && webSocketMap.containsKey(uid)) {
            webSocketMap.get(uid).sendMessage(message);
        } else {
            log.error("用户 : {} 不在线", uid);
        }
    }
    /**
     * 服务器主动推送
     * @param message 消息
     * @throws IOException io异常
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.uid+",原因:"+error.getMessage());
        error.printStackTrace();
    }
}
