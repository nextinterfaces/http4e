package org.roussev.http4e.crypt;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Cryptography utilities including Encription/Decryption, Computing MAC
 * (message authentication code), Computing MessageDigest, etc.
 * 
 * @author Atanas Roussev
 */

public class CryptUtils {

   private CryptUtils() {
   }


   public static String encryptAndEncode( String key, String openText){
      String encrypted = null;
      String encoded = null;
      try {
         encrypted = encrypt(key, openText);
         encoded = URLEncoder.encode(encrypted, "UTF8");
         return encoded;

      } catch (UnsupportedEncodingException e) {
         // System.err.println("Failed to encode '" + encrypted + "' using
         // charset '" + CryptConstants.UTF8 + "'");
      }
      return null;
   }


   public static String decryptAndDecode( String key, String cipherText){
      String decrypted = null;
      String decoded = null;
      try {
         decoded = URLDecoder.decode(cipherText, "UTF8");
         decrypted = decrypt(key, decoded);
         return decrypted;

      } catch (UnsupportedEncodingException e) {
         // System.err.println("Failed to decode '" + cipherText + "' using
         // charset '" + CryptConstants.UTF8 + "'");
      }
      return null;
   }


   public static String encrypt( String key, String openText){
      try {
         EncryptFacade desEncrypter = new EncryptFacade(key);
         String desEncrypted = desEncrypter.encrypt(openText);
         return desEncrypted;

      } catch (Exception e) {
         // System.err.println("Failed to encrypt '" + openText + "' with key '"
         // + key + "'");
      }
      return null;
   }


   public static String decrypt( String key, String cipherText){
      try {
         EncryptFacade desEncrypter = new EncryptFacade(key);
         String desDecrypted = desEncrypter.decrypt(cipherText);
         return desDecrypted;

      } catch (Exception e) {
         // System.err.println("Failed to decrypt '" + cipherText + "' with key
         // '" + key + "'");
      }
      return null;
   }


   /**
    * Generates secret key by provided password phrase
    */
   public static Key getSecretKey( String passPhrase) throws Exception{
      if (passPhrase == null) {
         return null;
      }
      // KeyGenerator kg = KeyGenerator.getInstance("DES");
      // kg.init(56); // 56 is the keysize. Fixed for DES
      // SecretKey key = kg.generateKey();

      KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), CryptConstants.SALT, CryptConstants.ITERATION_COUNT);
      SecretKey key = SecretKeyFactory.getInstance(CryptConstants.SECRET_KEY_ALGORITHM).generateSecret(keySpec);

      return key;
   }


   /**
    * Computes the message authentication code from the provided stream and
    * password
    */
   public static byte[] computeMAC( InputStream is, String pass) throws Exception{
      if (is == null || pass == null) {
         return null;
      }

      Mac mac = Mac.getInstance(CryptConstants.HMAC_ALGORITHM);
      mac.init(getSecretKey(pass));

      byte[] dataBytes = new byte[1024];
      int nread = is.read(dataBytes);
      while (nread > 0) {
         mac.update(dataBytes, 0, nread);
         nread = is.read(dataBytes);
      }
      byte[] macbytes = mac.doFinal();

      return macbytes;
   }


   /**
    * Computes the message authentication code from the byte array and password
    */
   public static byte[] computeMAC( byte[] dataBytes, String pass) throws Exception{
      if (dataBytes == null)
         throw new RuntimeException("dataBytes are null.");
      if (pass == null)
         throw new RuntimeException("pass is null .");

      Mac mac = Mac.getInstance(CryptConstants.HMAC_ALGORITHM);
      mac.init(getSecretKey(pass));

      mac.update(dataBytes);

      byte[] macbytes = mac.doFinal();

      return macbytes;
   }


   public static byte[] computeMD( InputStream is) throws Exception{
      if (is == null)
         throw new RuntimeException("InputStream is null.");

      MessageDigest md = MessageDigest.getInstance(CryptConstants.MESSAGE_DIGEST_ALGORITHM);

      byte[] dataBytes = new byte[1024];
      int nread = is.read(dataBytes);
      while (nread > 0) {
         md.update(dataBytes, 0, nread);
         nread = is.read(dataBytes);
      }
      byte[] digest = md.digest();

      return digest;
   }


   public static byte[] computeMD( byte[] dataBytes) throws Exception{
      if (dataBytes == null)
         throw new RuntimeException("dataBytes is null.");

      MessageDigest md = MessageDigest.getInstance(CryptConstants.MESSAGE_DIGEST_ALGORITHM);

      md.update(dataBytes);
      byte[] digest = md.digest();

      return digest;
   }


   public static String encode( byte[] data){
      if (data == null)
         throw new RuntimeException("data is null.");

      String res = null;
      try {
         res = new String(Base64.encodeBase64(data), "UTF8");  // new sun.misc.BASE64Encoder().encode(data);
         res = URLEncoder.encode(res, "UTF8");
      } catch (Exception e) {
         // System.err.println("Failed to encode '" + data + "' using charset '"
         // + CryptConstants.UTF8 + "'");
         throw new RuntimeException(e);
      }
      return res;
   }


   public static byte[] decode( String data){
      if (data == null)
         throw new RuntimeException("data is null.");
      byte[] digestBytes;
      try {
         data = URLDecoder.decode(data, "UTF8");
         digestBytes = Base64.decodeBase64(data.getBytes("UTF8"));// new
                                                            // sun.misc.BASE64Decoder().decodeBuffer(data);
      } catch (Exception e) {
         // System.err.println("Failed to decode '" + data + "' using charset '"
         // + CryptConstants.UTF8 + "'");
         throw new RuntimeException(e);
      }
      return digestBytes;
   }


   /**
    * The method generates the public and private keys.
    * 
    * @throws NoSuchAlgorithmException
    */
   public static Key[] generatePrivatePublicKeys( String algorithm, int numBits) throws Exception{

      // Get the public/private key pair
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
      // SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
      // keyGen.initialize(numBits, random);
      keyGen.initialize(numBits);
      KeyPair keyPair = keyGen.genKeyPair();
      return new Key[] { keyPair.getPrivate(), keyPair.getPublic() };
   }


   /**
    * Method convertes the bytes arrays back to private and public key objects
    */
   public static Key[] bytesToPrivatePublicKeys( String algorithm, byte[] privKeyBytes, byte[] pubKeyBytes) throws Exception{
      PrivateKey privKey = null;
      PublicKey pubKey = null;
      KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

      if (privKeyBytes != null) {
         EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
         privKey = keyFactory.generatePrivate(privateKeySpec);
      }

      if (pubKeyBytes != null) {
         EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pubKeyBytes);
         pubKey = keyFactory.generatePublic(publicKeySpec);
      }

      return new Key[] { privKey, pubKey };
   }


   /**
    * Use to dump JDK service types.
    */
   private static void viewServiceTypes( PrintStream out){
      out.println("|----------Service Types----------|");
      for (Iterator it = getServiceTypes().iterator(); it.hasNext();) {
         String servType = (String) it.next();
         out.println("\t---" + servType + "---");
         out.println(getCryptoImpls(servType));
      }
   }


   /**
    * returns all available services types
    */
   private static Set getServiceTypes(){
      Set result = new HashSet();
      // All all providers
      Provider[] providers = Security.getProviders();
      for (int i = 0; i < providers.length; i++) {
         // Get services provided by each provider
         Set keys = providers[i].keySet();
         for (Iterator it = keys.iterator(); it.hasNext();) {
            String key = (String) it.next();
            key = key.split(" ")[0];

            if (key.startsWith("Alg.Alias.")) {
               // Strip the alias
               key = key.substring(10);
            }
            int ix = key.indexOf('.');
            result.add(key.substring(0, ix));
         }
      }
      return result;
   }


   /**
    * returns the available implementations for a service type
    */
   private static Set getCryptoImpls( String serviceType){
      Set result = new HashSet();

      // All all providers
      Provider[] providers = Security.getProviders();
      for (int i = 0; i < providers.length; i++) {
         // Get services provided by each provider
         Set keys = providers[i].keySet();
         for (Iterator it = keys.iterator(); it.hasNext();) {
            String key = (String) it.next();
            key = key.split(" ")[0];

            if (key.startsWith(serviceType + ".")) {
               result.add(key.substring(serviceType.length() + 1));
            } else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
               // This is an alias
               result.add(key.substring(serviceType.length() + 11));
            }
         }
      }
      return result;
   }


   public static byte[] mergeBytes( byte[] bytesA, byte[] bytesB){

      int aLen = bytesA.length;
      int bLen = bytesB.length;

      byte[] merged = new byte[aLen + bLen];
      for (int i = 0; i < aLen; i++) {
         merged[i] = bytesA[i];
      }
      for (int i = 0; i < bLen; i++) {
         merged[aLen + i] = bytesB[i];
      }
      return merged;
   }

   // public static void main( String[] args){
   // String data = "MS4xLjJ8MTI0MDUxMTAwMjY2MXx8fA==";
   // byte[] bytes = decode(data);
   // System.out.println(new String(bytes));
   // }

}
