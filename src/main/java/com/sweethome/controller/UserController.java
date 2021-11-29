package com.sweethome.controller;

import com.sweethome.entities.UserEntity;
import com.sweethome.service.UserService;
import com.sweethome.service.impl.UserServiceImpl;
import com.sweethome.utils.R;
import com.sweethome.utils.SweetUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${baidu.http}")
    private String baidu;

    @Value("${baidu.ak}")
    private String ak;

    @GetMapping("/getAddress")
    public R getAddress(HttpServletRequest servletRequest, @Param("cr") String cr) {
        String url = baidu+ "?ak="+ak+"&coor="+cr;
        return restTemplate.getForObject(url, R.class);
    }

}
