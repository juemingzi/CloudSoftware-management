package edu.xidian.enc;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

/**@author ZXQ
 * 序列化和反序列化处理
 */
public class SerializeUtil {
	
	public final static String CHARSET_ISO88591="iso-8859-1";
	
	/**@author ZXQ
	 * 将对象序列化
	 * @param original
	 * @return
	 * @throws IOException
	 */
	public static String serialize(Object original) throws IOException {
		if (null == original)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(original);
		byte[] str = baos.toByteArray();
		oos.close();
		baos.close();
		return new String(str, CHARSET_ISO88591);
	}
	
	/**@author ZXQ
	 * 反序列化
	 * @param serializedstr
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(String serializedstr)
			throws UnsupportedEncodingException, IOException,
			ClassNotFoundException {
		if (null == serializedstr)
			return null;
		BufferedInputStream bis = new BufferedInputStream(
				new ByteArrayInputStream(
						serializedstr.getBytes(CHARSET_ISO88591)));
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		ois.close();
		bis.close();
		return obj;
	}

}
