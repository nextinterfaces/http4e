package org.roussev.http4e.crypt;

import java.io.PrintStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

class TestCrypt { 

   /**
    * The following method is used for testing the String Encrypter class. This
    * method is responsible for encrypting and decrypting a sample String using
    * using a Pass Phrase.
    */
   private static void testUsingPassPhrase( PrintStream out){

      out.println();
      out.println("+----------------------------------------+");
      out.println("|  -- Test Using Pass Phrase Method --   |");
      out.println("+----------------------------------------+");
      out.println();

      String secretString = "Attack at dawn!";
      String passPhrase = "My Pass Phrase";

      // Create encrypter/decrypter class
      EncryptFacade desEncrypter = new EncryptFacade(passPhrase);

      // Encrypt the string
      String desEncrypted = desEncrypter.encrypt(secretString);

      // Decrypt the string
      String desDecrypted = desEncrypter.decrypt(desEncrypted);

      // Print out values
      out.println("PBEWithMD5AndDES Encryption algorithm");
      out.println("    Original String  : " + secretString);
      out.println("    Encrypted String : " + desEncrypted);
      out.println("    Decrypted String : " + desDecrypted);
      out.println();
   }


   /**
    * The following method is used for testing the String Encrypter class. This
    * method is responsible for encrypting and decrypting a sample String using
    * several symmetric temporary Secret Keys.
    */
   private static void testUsingSecretKey( PrintStream out){
      try {

         out.println();
         out.println("+----------------------------------------+");
         out.println("|  -- Test Using Secret Key Method --    |");
         out.println("+----------------------------------------+");
         out.println();

         String secretString = "Attack at dawn!";

         // Generate a temporary key for this example. In practice, you would
         // save this key somewhere. Keep in mind that you can also use a
         // Pass Phrase.
         SecretKey desKey = KeyGenerator.getInstance("DES").generateKey();
         SecretKey blowfishKey = KeyGenerator.getInstance("Blowfish").generateKey();
         SecretKey desedeKey = KeyGenerator.getInstance("DESede").generateKey();

         // Create encrypter/decrypter class
         EncryptFacade desEncrypter = new EncryptFacade(desKey);
         EncryptFacade blowfishEncrypter = new EncryptFacade(blowfishKey);
         EncryptFacade desedeEncrypter = new EncryptFacade(desedeKey);

         // Encrypt the string
         String desEncrypted = desEncrypter.encrypt(secretString);
         String blowfishEncrypted = blowfishEncrypter.encrypt(secretString);
         String desedeEncrypted = desedeEncrypter.encrypt(secretString);

         // Decrypt the string
         String desDecrypted = desEncrypter.decrypt(desEncrypted);
         String blowfishDecrypted = blowfishEncrypter.decrypt(blowfishEncrypted);
         String desedeDecrypted = desedeEncrypter.decrypt(desedeEncrypted);

         // Print out values
         out.println(desKey.getAlgorithm() + " Encryption algorithm");
         out.println("    Original String  : " + secretString);
         out.println("    Encrypted String : " + desEncrypted);
         out.println("    Decrypted String : " + desDecrypted);
         out.println();

         out.println(blowfishKey.getAlgorithm() + " Encryption algorithm");
         out.println("    Original String  : " + secretString);
         out.println("    Encrypted String : " + blowfishEncrypted);
         out.println("    Decrypted String : " + blowfishDecrypted);
         out.println();

         out.println(desedeKey.getAlgorithm() + " Encryption algorithm");
         out.println("    Original String  : " + secretString);
         out.println("    Encrypted String : " + desedeEncrypted);
         out.println("    Decrypted String : " + desedeDecrypted);
         out.println();

      } catch (NoSuchAlgorithmException e) {
      }
   }


   /**
    * Simple MAC validation tests. A good requst and a tempered requests are
    * performed.
    */
   private static void testMAC( PrintStream out) throws Exception{

      String text = "qwe";
      String pass = "pass";

      byte[] d1 = CryptUtils.computeMAC(text.getBytes("UTF8"), pass);
      out.println("----------Digest 1----------");
      out.println("MAC(in hex): " + HexUtils.bytesToHex(d1));
      System.out.println(HexUtils.prettyHex(d1));

      out.println("----------Digest 2----------");

      byte[] d2 = CryptUtils.computeMAC(text.getBytes("UTF8"), pass);
      out.println("MAC(in hex): " + HexUtils.bytesToHex(d2));
      System.out.println(HexUtils.prettyHex(d2));

      out.println("Digests equal ? :" + MessageDigest.isEqual(d1, d2) + "\n");

      // tampered attempt
      text = text + "1";

      out.println("----------Digest 3. Tampered Text----------");

      byte[] d3 = CryptUtils.computeMAC(text.getBytes("UTF8"), pass);
      out.println("MAC(in hex): " + HexUtils.bytesToHex(d3));
      System.out.println(HexUtils.prettyHex(d3));

      out.println("Digests equal ? :" + MessageDigest.isEqual(d1, d3) + "\n");
   }


   private static void testPublicPrivateKeys(){
      try {
         String keyAlgorithm = "RSA";
         Key[] keys = CryptUtils.generatePrivatePublicKeys(keyAlgorithm, 1024);
         Key kPriv = keys[0];
         Key kPub = keys[1];
         byte[] kPubBytes = kPub.getEncoded();
         byte[] kPrivBytes = kPriv.getEncoded();
         System.out.println("--------------PublicKey----------------");
         System.out.println(kPub);
         System.out.println(kPub.getAlgorithm());
         System.out.println(kPub.getEncoded());
         System.out.println("--------------PrivateKey----------------");
         System.out.println(kPriv);
         System.out.println(kPriv.getAlgorithm());
         System.out.println(kPriv.getEncoded());

         // The bytes can be converted back to public and private key objects
         KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
         EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(kPrivBytes);
         PrivateKey privateKey2 = keyFactory.generatePrivate(privateKeySpec);

         EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(kPubBytes);
         PublicKey publicKey2 = keyFactory.generatePublic(publicKeySpec);

         // The original and new keys are the same
         System.out.println("  Are both private keys equal? " + kPriv.equals(privateKey2));

         System.out.println("  Are both public keys equal? " + kPub.equals(publicKey2));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
