package org.roussev.http4e.crypt;

public interface CryptConstants {
    
    String UTF8 = "UTF8"; 
    
    // 8-bytes Salt
    byte[] SALT = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
    };

    int ITERATION_COUNT = 19;
    
    String SECRET_KEY_ALGORITHM = "PBEWithMD5AndDES";

    String HMAC_ALGORITHM = "HmacSHA1";
    
    String MESSAGE_DIGEST_ALGORITHM = "SHA1";    

    String PUBLIC_KEY_ALGORITHM = "RSA";  //"DSA", "DH", "RSA"

}
