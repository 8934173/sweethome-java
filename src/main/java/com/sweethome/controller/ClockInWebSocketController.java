package com.sweethome.controller;

import com.sweethome.socket.BaseSocketServer;
import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/clockInMessage")
public class ClockInWebSocketController {

    @GetMapping("/send/{uid}")
    public R sentMessageByUid(@RequestParam("message") String message, @PathVariable("uid") String uid) {
        try {
            BaseSocketServer.sendInfo(message, uid);
            return R.ok();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return R.error();
        }
    }
}
