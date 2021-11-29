package com.sweethome.utils;

import com.alibaba.fastjson.JSON;
import com.sweethome.entities.UserEntity;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt
 */
public class JwtUtil {
    /**
     * toke 过期时间 3 小时
     */
    public static final long DEFAULT_TOKEN_TIME_MS = 3 * 60* 60 * 1000L;

    /**
     * 签名密匙
     */
    public static final String SECRET = "sweethome";

    /**
     * 添加一个前缀
     */
    private static final String JWT_SEPARATOR = "sweethome#";

    /**
     * token生效时间(默认是从当前开始生效)
     * 默认：new Date(System.currentTimeMillis() + START_TIME)
     */
    private static final Long START_TIME = 0L;

    /**
     * token在什么时间之前是不可用的（默认从当前时间）
     * 默认：new Date(System.currentTimeMillis() + BEFORE_TIME)
     */
    private static final Long  BEFORE_TIME = 0L;

    /**
     * 加密类型 三个值可取 HS256  HS384  HS512
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;

    /**
     * 将将密码转换为字节数组
     * 根据指定的加密方式，生成密钥
     * @return 密匙
     */
    private static Key generateKey() {
//        byte[] bytes = Base64.decodeBase64(SECRET);
//        return new SecretKeySpec(bytes, JWT_ALG.getJcaName());
        byte[] bytes = DatatypeConverter.parseBase64Binary(SECRET);
        return new SecretKeySpec(bytes, JWT_ALG.getJcaName());
    }

    /**
     * 创建token
     * @param subject token所面向的用户
     * @param audience 接收token的一方
     * @param issuer token签发者
     * @param jti token的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @return 加密后的token字符串
     */
    public static String createToken(Object user, String subject, String audience, String issuer, String jti) {
        final JwtBuilder builder = Jwts.builder();

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("userDetails", user);
        String token = builder
                .setClaims(claims)
                .signWith(JWT_ALG, generateKey())
                .setSubject(subject)
                .setAudience(audience)
                .setId(jti)
                .setIssuer(issuer)
                .setNotBefore(new Date(System.currentTimeMillis() + BEFORE_TIME))
                .setIssuedAt(new Date(System.currentTimeMillis() + START_TIME))
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_TOKEN_TIME_MS))
                .compact();
        return token;
    }

    /**
     * 解析token
     * @param token token字符串
     * @return Jws
     */
    public static Jws<Claims> parseToken(String token) {
        //token = StringUtils.substringAfter(token, JWT_SEPARATOR);
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(token);
        //return Jwts.parser().setSigningKey(generateKey()).parseClaimsJws(token);
    }

    /**
     * 校验token,校验是否是本服务器的token
     * @param token token字符串
     * @return boolean
     */
    public static Boolean checkToken(String token) {
        return parseToken(token).getBody() != null;
    }

    /**
     *  根据sub判断token
     * @param token token字符串
     * @param sub 面向的用户
     * @return boolean
     */
    public static Boolean checkToken(String token,String sub) {
        return parseToken(token).getBody().getSubject().equals(sub);
    }

    /**
     * 验证jwt的有效期
     *
     * @param claims
     * @return
     */
    public static Boolean isTokenExpired(Claims claims) {
        return claims == null || claims.getExpiration().before(new Date());
    }

    /**
     * 获取用户
     * @param request
     * @return
     */
    public static UserEntity getUser(HttpServletRequest request) {
        String header = request.getHeader(Constant.TOKEN);
        String userDetails = (String) parseToken(header).getBody().get("userDetails");
        return JSON.parseObject(userDetails, UserEntity.class);
    }
}
