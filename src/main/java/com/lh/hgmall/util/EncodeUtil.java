package com.lh.hgmall.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

public class EncodeUtil {
    public static Map<String,Object> encode(String pass)
    {
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName,pass,salt,times).toString();
        Map<String,Object> map = new HashMap<>();
        map.put("salt",salt);
        map.put("pass",encodedPassword);
        return map;
    }

    public static String recode(String pass,String salt){
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName,pass,salt,times).toString();
        return encodedPassword;
    }
}
