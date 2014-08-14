package util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;


public class SCry {
	//public final static String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEmgEmQcgLd0mvWqL6AKmhzj" + "JfZoAmZC0PUmG8K9CB1Ml68P00S3eU+TSL5aG8Mg3Tipvs02gC2veC10knRi7r" + "EsUwL8+h22EsjnpKZ/7K5YV9cefryTMnS0x4QGZbSkdPz/rLh0uGwk8Zu0cEKb" + "xQyvd3+pSmqZ5/ZQGaFjm9TQIDAQAB";
	
	/*
	public final static String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEmgEmQcgLd0mvWqL6AKmhzj"
	+ "JfZoAmZC0PUmG8K9CB1Ml68P00S3eU+TSL5aG8Mg3Tipvs02gC2veC10knRi7r"
	+ "EsUwL8+h22EsjnpKZ/7K5YV9cefryTMnS0x4QGZbSkdPz/rLh0uGwk8Zu0cEKb"
	+ "xQyvd3+pSmqZ5/ZQGaFjm9TQIDAQAB";
	*/
	
	public final static String serverPublicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEmgEmQcgLd0mvWqL6AKmhzj" + "JfZoAmZC0PUmG8K9CB1Ml68P00S3eU+TSL5aG8Mg3Tipvs02gC2veC10knRi7r" + "EsUwL8+h22EsjnpKZ/7K5YV9cefryTMnS0x4QGZbSkdPz/rLh0uGwk8Zu0cEKb" + "xQyvd3+pSmqZ5/ZQGaFjm9TQIDAQAB\n";
	
	private static PublicKey key;

	static {
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(serverPublicKey));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			key = kf.generatePublic(spec);
			//System.out.println(DatatypeConverter.parseBase64Binary(serverPublicKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String string) {
		byte[] buf = string.getBytes(Charset.forName("UTF-8"));

		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//System.out.println("testing");
			
			//RC4 c = new RC4("138058324507555049234246734000441140752323800141033219108446290424209651149061550256257123074054375779322028941830252756690799794787415386877876745524597141573809959460210645347060554401228507084399841512197648738006400503980279149209427806825118624928692857963634531857449103468249102954313978230908185591117");
			
			//byte[] b=c.rc4(buf);
			
			/*
			System.out.println(b.length);
			for(int i=0;i<b.length;i+=1)
			{
				//System.out.println(b[i]);	
			}
			*/
			
			buf = cipher.doFinal(buf);
			//System.out.println("testing2");
			//System.out.println(buf.length);
			
			return DatatypeConverter.printBase64Binary(buf);
		} catch (Exception e) {
			key = null;
			e.printStackTrace();
			return null;
		}
	}

	public static String createGuestGUID() {
		long timestamp = System.currentTimeMillis();
		double random = Math.random() * Double.MAX_VALUE;
		System.out.println(Double.MAX_VALUE);
		System.out.println( Math.random());
		System.out.println( random );
		String caps = caps();
		return sha1string(timestamp + "" + random + caps + 1).toUpperCase();
	}

	private static String sha1string(String string) {
		return step8(string);
	}

	private static String step8(String string) {
		return hexString(sha1(stringToBytes(string)));
	}

	private static String hexString(byte[] buf) {
		char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] hexChars = new char[buf.length * 2];
		int v;
		for (int j = 0; j < buf.length; j++) {
			v = buf[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private static byte[] sha1(byte[] buf) {
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			sha1.digest(buf);
			return buf;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] stringToBytes(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String caps() {
		return "";
	}
}
