package com.gromoks.movieland.service.impl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryption {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncryption.class);

    public static String encryptPassword(String password) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid message digest algorithm");
            throw new RuntimeException("Invalid message digest algorithm");
        }

        md.update(password.getBytes());
        byte byteData[] = md.digest();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            stringBuilder.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }
}
