package com.kuyuner.common.security;

import com.kuyuner.common.codec.DigestUtils;
import com.kuyuner.common.codec.EncodeUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 */
public class PasswordUtils {

    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
    public static final String MD5 = "MD5";

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 MD5
     *
     * @param plainPassword 明文密码
     */
    public static String entryptPassword(String plainPassword) {
        byte[] salt = DigestUtils.genSalt(SALT_SIZE);
        byte[] hashPassword;
        try {
            hashPassword = DigestUtils.digest(plainPassword.getBytes("UTF-8"), MD5, salt, HASH_INTERATIONS);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return EncodeUtils.encodeHex(salt) + EncodeUtils.encodeHex(hashPassword);
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = EncodeUtils.decodeHex(password.substring(0, 16));
        byte[] hashPassword;
        try {
            hashPassword = DigestUtils.digest(plainPassword.getBytes("UTF-8"), MD5, salt, HASH_INTERATIONS);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return password.equals(EncodeUtils.encodeHex(salt) + EncodeUtils.encodeHex(hashPassword));
    }

}
