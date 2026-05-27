package com.expedium.demo.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

	public static String encrypt(String data, String key) {
	    try {
	        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");//it converts string to 16bytes key
	        Cipher cipher = Cipher.getInstance("AES");//cipher initialization it initializes AES algorithm like encryption machine is started and this 
	        //used ECB mode by default so for same passwords same encrypted key will be generated

	        cipher.init(Cipher.ENCRYPT_MODE, keySpec);//it'll start encrypting now using key

	        byte[] encrypted = cipher.doFinal(data.getBytes());//here data.getBytes->converts password to bytes then doFinal converts data to unreadable binary

	        return Base64.getEncoder().encodeToString(encrypted);//DBexpects data in text format so Base64 does binary->readable text format

	    } catch (Exception e) { 
	        e.printStackTrace();
	    }
	    return null;
	}
}
