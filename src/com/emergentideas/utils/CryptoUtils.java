package com.emergentideas.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



public class CryptoUtils 
{
	protected static final char[] caLower = new char[] { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'};
	protected static final char[] caUpper = new char[] { 'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Z' };
	protected static final char[] caNum = new char[] { '2', '3', '4', '5', '6', '7', '8', '9'};
	protected static final char[][] caCharTypes = new char[][] { caLower, caUpper, caNum };
	protected static Random ran = new Random(new Date().getTime());


	/**
	 * Returns an MD5 hash of the string as a string of hex digits
	 * @param s
	 * @return
	 */
	public static String generateMD5Hash(String s) throws Exception
	{
		java.security.MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		
		String sDigest = new java.math.BigInteger(1,m.digest()).toString(16);
		return(sDigest);
	}
	
	/**
	 * Returns an MD5 hash of the byte array as a string of hex digits
	 * @param s
	 * @return
	 */
	public static String generateMD5Hash(byte[] data) throws Exception 
	{
		java.security.MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(data, 0, data.length);
		
		String sDigest = new java.math.BigInteger(1, m.digest()).toString(16);
		return(sDigest);
	}
	
	public static KeyPair generateKeyPair() throws Exception
	{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		kpg.initialize(1024, random);
		KeyPair kp = kpg.generateKeyPair();
		
		return(kp);
	}
	
	public static byte[] signData(PrivateKey key, byte[] data) throws Exception
	{
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initSign(key);
		sig.update(data);
		return(sig.sign());
		
	}

	public static boolean verifySignature(PublicKey key, byte[] data, byte[] signature) throws Exception
	{
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(key);
		sig.update(data);
		return(sig.verify(signature));
		
	}
	
	public static SecretKey generateBlowfishKey() throws Exception
	{
		SecretKey seckey;
		KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
		kg.init(128);
		seckey = kg.generateKey();
		
		SecretKey seckeycomp = generateBlowfishKey(seckey.getEncoded());
		return(seckey);
	}
	
	public static SecretKey generateBlowfishKey(byte[] primer) throws Exception
	{
		SecretKey seckey;
		
		if(primer.length > (128 / 8))
		{
			byte[] smallerkey = new byte[(128/8)];
			System.arraycopy(primer, 0, smallerkey, 0, (128 / 8));
			primer = smallerkey;
		}
		
		seckey = new SecretKeySpec(primer, "Blowfish");
		return(seckey);
	}
	
	public static byte[] encryptWithSecretKey(SecretKey seckey, byte[] data) throws Exception
	{
		Cipher c = Cipher.getInstance(seckey.getAlgorithm());
		c.init(Cipher.ENCRYPT_MODE, seckey);
		
		return(c.doFinal(data));
	}
	
	public static byte[] encryptWithAsymmetricKey(Key key, byte[] data) throws Exception
	{
		ByteArrayOutputStream bab = new ByteArrayOutputStream();
		Cipher c = Cipher.getInstance(key.getAlgorithm());
		c.init(Cipher.ENCRYPT_MODE, key);
		
		byte[] tempBuffer;
		
		for(int iPlace = 0; iPlace < data.length; )
		{
			int iLength = data.length - iPlace;
			if(iLength > 117)
			{
				iLength = 117;
			}
			
			tempBuffer = new byte[iLength];
			System.arraycopy(data, iPlace, tempBuffer, 0, iLength);
			byte[] bResult = c.doFinal(tempBuffer);
			bab.write(bResult);
			iPlace += iLength;
		}
		return(bab.toByteArray());
	}
	
	public static byte[] decryptWithAsymmetricKey(Key key, byte[] data) throws Exception
	{
		ByteArrayOutputStream bab = new ByteArrayOutputStream();
		Cipher c = Cipher.getInstance(key.getAlgorithm());
		c.init(Cipher.DECRYPT_MODE, key);
		
		byte[] tempBuffer;
		
		for(int iPlace = 0; iPlace < data.length; )
		{
			int iLength = data.length - iPlace;
			if(iLength > 128)
			{
				iLength = 128;
			}
			
			tempBuffer = new byte[iLength];
			System.arraycopy(data, iPlace, tempBuffer, 0, iLength);
			bab.write(c.doFinal(tempBuffer));
			iPlace += iLength;
		}
		return(bab.toByteArray());
	}


	public static byte[] decryptWithSecretKey(SecretKey seckey, byte[] data) throws Exception
	{
		Cipher c = Cipher.getInstance(seckey.getAlgorithm());
		c.init(Cipher.DECRYPT_MODE, seckey);
		return(c.doFinal(data));
	}
	
	/**
	 * Creates a blowfish key by using a set of public keys.
	 * @param keys
	 * @return
	 * @throws Exception
	 */
	public static Key createAgreedBlowfishKey(Collection<Key> keys) throws Exception
	{
		KeyAgreement ka = KeyAgreement.getInstance("Blowfish");
		Iterator<Key> itKeys = keys.iterator();
		while(itKeys.hasNext())
		{
			Key key = itKeys.next();
			if(itKeys.hasNext() == true)
			{
				ka.doPhase(key, false);
			}
			else
			{
				return(ka.doPhase(key, true));
			}
		}
		return(null);
	}
	
	public static void outputBytes(byte[] data)
	{
		for(byte b : data)
		{
			
			System.out.print("" + ((int)b));
		}
		
		System.out.println();
		
	}
	
	/**
	 * Decodes an encoded byte stream to a UTF-8 string
	 * @param enc
	 * @return
	 */
	public static String base64decodeToString(byte[] enc) throws UnsupportedEncodingException
	{
		enc = base64decode(enc);
		return(new String(enc, "UTF-8"));
	}
	
	public static byte[] base64decode(byte[] enc)
	{
		enc = Base64.decodeBase64(enc);
		return(enc);
	}
	
	/**
	 * Decodes a to encrypted bytes assuming that the string is UTF-8
	 * @param enc
	 * @return
	 */
	public static byte[] base64decode(String enc) throws UnsupportedEncodingException
	{
		return(base64decode(enc.getBytes("UTF-8")));
	}

	
	public static byte[] base64encode(byte[] plain)
	{
		return(Base64.encodeBase64(plain));
	}
	
	/**
	 * Returns the encoded bytes of a string assuming it is in UTF-8
	 * @param plain
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] base64encode(String plain) throws UnsupportedEncodingException
	{
		return(base64encode(plain.getBytes("UTF-8")));
	}
	
	/**
	 * encodes a set of bytes to a UTF-8 string
	 * @param plain
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64encodeToString(byte[] plain) throws UnsupportedEncodingException
	{
		plain = base64encode(plain);
		return(new String(plain, "UTF-8"));
	}

	
	public static String genNewPassword(int length)
	{
		StringBuffer sb = new StringBuffer();
		
		while(length > 0)
		{
			int iCharTypes = caCharTypes.length;
			
			int iCharTypesChosen = ran.nextInt(iCharTypes);
			
			char[] caChosen = caCharTypes[iCharTypesChosen];
			
			int iChars = caChosen.length;
			
			int iCharChosen = ran.nextInt(iChars);
			
			char c = caChosen[iCharChosen];
			
			sb.append(c);
			
			length--;
		}
		
		return(sb.toString());
	}


}
