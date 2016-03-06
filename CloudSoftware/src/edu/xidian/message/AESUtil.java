package edu.xidian.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;

import edu.xidian.message.Message;
import edu.xidian.message.MsgType;

public class AESUtil {
//	private final static String filePath = "C:/key.txt";
////	private final static String filePath = "/home/key.txt";
	private static String filePath = null;
	/*
	 * 获取系统类型
	 */
	private static int os = -1;
	
	static{
		Properties prop = System.getProperties();
		String osName = prop.getProperty("os.name");
		if (osName.toLowerCase().contains("windows")) {
			filePath = "C:/key.txt";
			os = 0;
		} else {
			os = 1;
			filePath = "/home/key.txt";
		}
	}
	
	
	
	public AESUtil(){
		Properties prop = System.getProperties();
		String osName = prop.getProperty("os.name");
		if (osName.toLowerCase().contains("windows")) {
			filePath = "C:/key.txt";
			os = 0;
		} else {
			os = 1;
			filePath = "/home/key.txt";
		}
	}
	/**
	 * @author WZY
	 * @throws SQLException
	 * @content 要加密的内容
	 * @password 加密共享秘钥 show加解密算法
	 * 
	 */
	public static byte[] encrypt(String content) throws SQLException {
		try {
			WRFile wr = new WRFile();
			String pwd = wr.readKey(filePath);
			System.out.println(pwd+"**********"+pwd.length());
//			String pwd = "12345678";
			//String password = null;
			// 得到明文秘钥
			if(pwd.equals("12345678")){
				System.out.println("the same name===============");
			}
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(128, new SecureRandom(pwd.getBytes()));
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
            secureRandom.setSeed(pwd.getBytes());  
            kgen.init(128,secureRandom); 
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("iso-8859-1");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author WZY
	 * @content 要解密的内容
	 * @password 加密共享秘钥，在这个地方先写死了 show加解密算法
	 * 
	 */
	public static byte[] decrypt(byte[] content) {
		try {
			WRFile wr = new WRFile();
			String password = wr.readKey(filePath);
//			String password = "12345678";
			System.out.println("password "+password+"-------"+password.length());
			if(password.equals("12345678")){
				System.out.println("the same name===============");
			}
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(128, new SecureRandom(password.getBytes()));
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
            secureRandom.setSeed(password.getBytes());  
            kgen.init(128,secureRandom); 
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 解密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "NoSuchAlgorithmException".getBytes();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return "NoSuchPaddingException".getBytes();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return "InvalidKeyException".getBytes();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return "IllegalBlockSizeException".getBytes();
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return "BadPaddingException".getBytes();
		}
	}

	/**
	 * @Title: isErrorKey
	 * @Description: 判断是否为错误密钥加密的消息
	 * @param str
	 * @return: boolean
	 */
	public static boolean isErrorKey(String str) {
		if (str.equals("BadPaddingException")) {
			return true;
		} else if (str.equals("IllegalBlockSizeException")) {
			return true;
		} else if (str.equals("InvalidKeyException")) {
			return true;
		} else if (str.equals("NoSuchPaddingException")) {
			return true;
		} else if (str.equals("NoSuchAlgorithmException")) {
			return true;
		} else
			return false;

	}

	 /**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	 public static void main(String[] args) throws Exception {
	/*
	 Message msg = new Message(MsgType.setupTomcat, "12","12345678");
	 String content = SerializeUtil.serialize(msg);
	 //String password = "12345678";
	 String hostIP = "127.0.0.1";
	 //加密
	 System.out.println("加密前：" + content);
	 byte[] encryptResult = encrypt(content);
	 String b = new String(encryptResult,"ISO-8859-1");
	 System.out.println("加密后："+b);
	 //解密
	 byte[] bb = b.getBytes("ISO-8859-1");
	 byte[] decryptResult = decrypt(bb);
	 System.out.println("解密后：" + new String(decryptResult,"ISO-8859-1"));
	 msg = (Message)SerializeUtil.deserialize( new
	 String(decryptResult,"ISO-8859-1"));
	 System.out.println(msg.getType().name());*/
		/* String pwd ="12345678";
		 String password = new String(new BASE64Decoder().encodeBuffer(pwd));*/
//		 encrypt("a");
		 decrypt("a".getBytes());
	 }

}
