package edu.xidian.enc;

import java.security.MessageDigest;

/**@author ZXQ
 * 加解密算法
 * @author ZXQ
 *
 */

public class MD5Util {
	/**@author ZXQ
	 * 生成32位md5码
	 * @param obj
	 * @return
	 */
//	public static String string2MD5(Object  obj) {
//		MessageDigest md5 = null;
//		try {
//			md5 = MessageDigest.getInstance("MD5");
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			e.printStackTrace();
//			return "";
//		}
//		char[] charArray = obj.toString().toCharArray();
//		byte[] byteArray = new byte[charArray.length];
//
//		for (int i = 0; i < charArray.length; i++)
//			byteArray[i] = (byte) charArray[i];
//		byte[] md5Bytes = md5.digest(byteArray);
//		StringBuffer hexValue = new StringBuffer();
//		for (int i = 0; i < md5Bytes.length; i++) {
//			int val = ((int) md5Bytes[i]) & 0xff;
//			if (val < 16)
//				hexValue.append("0");
//			hexValue.append(Integer.toHexString(val));
//		}
//		return hexValue.toString();
//
//	}
//	
//	/**@author ZXQ
//	 * 加密解密算法 (执行一次加密，执行两次解密)
//	 * @param inStr
//	 * @return
//	 */
//	public static String convertMD5(String inStr) {
//
//		char[] a = inStr.toCharArray();
//		for (int i = 0; i < a.length; i++) {
//			a[i] = (char) (a[i] ^ 't');
//		}
//		String s = new String(a);
//		return s;
//	}

}
