package com.sweethome.entities;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SocketMessage {

    /**
     * 消息发送者id
     */
    private String uid;
    /**
     * 消息接受者id或群聊id
     */
    private String toUid;
    /**
     * 消息内容
     */
    private String message;

    /**
     * 发送时间
     */
    private String time;

}
