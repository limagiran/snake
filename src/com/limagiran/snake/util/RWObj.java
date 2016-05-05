package com.limagiran.snake.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Vinicius
 */
public class RWObj {

    private static final String IV = "AAAAAAAAAAAAAAAA";

    /**
     * Salva um objeto no caminho passado por parâmetro
     *
     * @param object objeto a ser salvo
     * @param file local onde será salvo
     * @param password senha para criptografia
     * @return true para salvo ou false para erro
     */
    public static boolean gravarObjeto(Object object, File file, String password) {
        try (FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(encrypt(fos,
                        password));) {
            oos.writeObject(object);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Carrega um objeto salvo em disco
     *
     * @param <T>
     * @param file local do objeto salvo
     * @param classe classe a ser convertida
     * @param password senha para descriptografia
     * @return objeto lido ou null para erro;
     */
    public static <T> T lerObjeto(File file, Class<T> classe, String password) {
        try (FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(decrypt(fis,
                        password));) {
            Object retorno = ois.readObject();
            return classe.isInstance(retorno) ? (T) retorno : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Cria um OutputStream para encriptação
     *
     * @param os OutputStream utilizado
     * @param encKey chave de encriptação
     * @return CipherOutputStream
     * @throws Exception erro ao gerar CipherOutputStream
     */
    private static CipherOutputStream encrypt(OutputStream os,
            String encKey) throws Exception {
        encKey = Security.getPass16(encKey);
        Cipher cip = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encKey.getBytes("UTF-8"), "AES");
        cip.init(ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return new CipherOutputStream(os, cip);
    }

    /**
     * Cria um InputStream para decriptação
     *
     * @param is InputStream utilizado
     * @param encKey chave de decriptação
     * @return CipherInputStream
     * @throws Exception erro ao gerar CipherInputStream
     */
    private static CipherInputStream decrypt(InputStream is,
            String encKey) throws Exception {
        encKey = Security.getPass16(encKey);
        Cipher cip = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encKey.getBytes("UTF-8"), "AES");
        cip.init(DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return new CipherInputStream(is, cip);
    }

}
