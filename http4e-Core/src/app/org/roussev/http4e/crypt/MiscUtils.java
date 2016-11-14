package org.roussev.http4e.crypt;

import java.io.IOException;
import java.io.InputStream;

public class MiscUtils {
 
   public static byte[] streamToBytes( InputStream in) throws IOException{
      byte[] bytes = new byte[in.available()];
      in.read(bytes);
      in.close();
      return bytes;
   }

}
