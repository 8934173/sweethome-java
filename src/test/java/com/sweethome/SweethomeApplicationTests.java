package com.sweethome;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sweethome.entities.ClockInEntity;
import com.sweethome.entities.SysAuthor;
import com.sweethome.entities.UserEntity;
import com.sweethome.service.ClockInService;
import com.sweethome.service.SysAuthorService;
import com.sweethome.service.SysRoleService;
import com.sweethome.service.UserService;
import com.sweethome.utils.DateUtilSweet;
import com.sweethome.utils.RedisT;
import com.sweethome.utils.SnowflakeIdWorker;
import com.sweethome.utils.SweetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SweethomeApplicationTests {

    @Autowired
    private ClockInService clockInService;

    @Autowired
    private SysAuthorService sysAuthorService;

    @Autowired
    private RedisT redisT;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() throws ParseException {
        long now = System.currentTimeMillis();
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
        long overTime = (now - (sdfOne.parse(sdfOne.format(now)).getTime()))/1000;
        //当前毫秒数
        System.out.println(now);
        //当前时间  距离当天凌晨  秒数 也就是今天过了多少秒
        System.out.println(overTime);
        //当前时间  距离当天晚上23:59:59  秒数 也就是今天还剩多少秒
        long TimeNext = 24*60*60 - overTime;
        System.out.println(TimeNext);
        //当天凌晨毫秒数
        System.out.println(sdfOne.parse(sdfOne.format(now)).getTime());
        //当天凌晨日期
        SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.print(sdfTwo.format(sdfOne.parse(sdfOne.format(now)).getTime()));
    }

    @Test
    void testPassword () {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String s = passwordEncoder.encode("kk8934173");
        System.out.println(s);

        //是否匹配
        boolean matches = passwordEncoder.matches("kk1743684221", "$2a$10$nlerESvub4lvXYiJWZyPjOCMxC9LNwjuOD.wGrgUCCKWOy34kaCeG");
        System.out.println(matches);
        // passwordEncoder.matches("")

        String encode = passwordEncoder.encode("$2a$10$nlerESvub4lvXYiJWZyPjOCMxC9LNwjuOD.wGrgUCCKWOy34kaCeG");
        System.out.println(encode);

    }

    @Test
    void testDate() {
        String date = DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_SECOND);
        System.out.println(date);

        String simpleUUID = IdUtil.simpleUUID();
        String fastSimpleUUID = IdUtil.fastSimpleUUID();
        System.out.println(simpleUUID);
        System.out.println(simpleUUID);
        System.out.println(fastSimpleUUID);
    }

    @Test
    void testDelete() {
//        ClockInEntity clock = clockInService.getOneClockInByTime();
//        System.out.println(clock);
    }

    @Test
    void testDay15() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -15);
        int l = 0 / 15;
        float i = ((float)9 / 15);
        System.out.println(i);
        System.out.println("当前的时间是 " + DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_DAY));
        System.out.println("15天前的时间是 " + DateUtilSweet.dateToString(calendar.getTime(), DateUtilSweet.DATE_FORMAT_DAY));
    }

    @Test
    void testUser() {
        UserEntity userEntity = userService.queryByUserName("18807064255");
        System.out.println(userEntity);
        System.out.println(userEntity.getAuthority());
        List<String> list = JSON.parseArray(userEntity.getAuthority(), String.class);

        List<SysAuthor> authorByIds = sysAuthorService.getAuthorByIds(list);
        List<String> collect = authorByIds.stream().map(SysAuthor::getAuName).collect(Collectors.toList());
        System.out.println(ArrayUtils.toString(collect));
        StringBuffer buffer = new StringBuffer();
        authorByIds.forEach((it) -> {
            buffer.append(it.getAuName()).append(",");
        });
        System.out.println(buffer.substring(0, buffer.length() - 1));

    }

    @Test
    void testRedis() {
        System.out.println(redisT.hasKey("112345"));
    }
}
