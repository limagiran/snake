package com.limagiran.snake.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Vinicius Silva
 */
public class Security {

    /**
     * Retorna o hash SHA256 da string passada por parâmetro
     *
     * @param value string a ser codificada
     * @return hash SHA256 hexadecimal
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getSHA256(String value) 
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(value.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        return hexString.toString();
    }

    /**
     * Retorna o password com 16 dígitos
     *
     * @param key password
     * @return password com 16 dígitos
     */
    public static String getPass16(String key) {
        key = (((key == null) || (key.isEmpty())) ? "A" : key);
        try {
            return getSHA256(key).substring(0, 16);
        } catch (Exception e) {
            while (key.length() < 16) {
                key += key;
            }
            return key.substring(0, 16);
        }
    }
}
