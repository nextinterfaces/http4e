package org.roussev.http4e.httpclient.core.util;

public class JsonUtil {

   public static String hexToChar( String i, String j, String k, String l){
      return Character.toString((char) Integer.parseInt("" + i + j + k + l, 16));
   }


   public static String render( String value, boolean hubav, String indent){
      final StringBuilder sb = new StringBuilder();
      if (hubav) {
         sb.append(indent);
      }
      
      sb.append("\"");
      int len = value.length();
      for (int i = 0; i < len; i++) {
         final char lChar = value.charAt(i);
         if (lChar == '\n') {
            sb.append("\\n");
         } else if (lChar == '\r') {
            sb.append("\\r");
         } else if (lChar == '\f') {
            sb.append("\\f");
         } else if (lChar == '\t') {
            sb.append("\\t");
         } else if (lChar == '\b') {
            sb.append("\\b");
            // else if(lChar == '/') lBuf.append("\\/");
         } else if (lChar == '\"') {
            sb.append("\\\"");
         } else if (lChar == '\\') {
            sb.append("\\\\");
         } else {
            sb.append(lChar);
         }
      }

      return sb.append("\"").toString();
   }
   
   public static void main( String[] args){
      System.out.println(render(
            "{\"JSON\":\"Hello, World!\"}"//"[50,0,0,49,[\"hhhh\"],0,0,1,288,]"
            , false, "   "));
      
   }
   
}
