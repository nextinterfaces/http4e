/*
 *  Copyright 2017 Eclipse HttpClient (http4e) http://nextinterfaces.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.roussev.http4e.httpclient.core.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Serializer;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.swt.custom.StyledText;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.roussev.http4e.crypt.HexUtils;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.misc.ApacheHttpListener;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.view.assist.AssistConstants;
import org.roussev.http4e.httpclient.core.misc.CoreException;

/**
 * A class with misc utils. 
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class JunkUtils {
 
   private static String XML_LINE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   
   private final static ApacheHttpListener httpListener = new ApacheHttpListener(){
      public void write( byte[] data){ 
         // blank
      }

      public void close(){
         // blank
      }                 
   };
   
   
   private final static ResponseReader responseReader = new ResponseReader() {

      public void read(HttpMethod httpMethod) {
//          HttpUtils.dumpResponse(httpMethod, System.out);
      }
   };
  

   public static boolean isXwwwFormType(ItemModel model){ 
      List<String> headers = model.getHeaderValuesIgnoreCase(AssistConstants.HEADER_CONTENT_TYPE);
      if(headers == null){
         return false;
      }
      try {
         String lastContType = (String)headers.get(headers.size()-1);
         return AssistConstants.CONTENT_TYPE_X_WWW_FORM.equalsIgnoreCase(lastContType);
         
      } catch (Exception ignore) {}
      
      return false;
   }
  

   public static boolean isMultiartFormType(ItemModel model){ 
      List<String> headers = model.getHeaderValuesIgnoreCase(AssistConstants.HEADER_CONTENT_TYPE);
      if(headers == null){
         return false;
      }
      try {
         String lastContType = (String)headers.get(headers.size()-1);
         return AssistConstants.CONTENT_TYPE_MULTIPART.equalsIgnoreCase(lastContType);
         
      } catch (Exception ignore) {}
      
      return false;
   }
   
   
   public static void hexText(StyledText styledText, String text){
      try {
         styledText.setText( HexUtils.toHex(text.getBytes(CoreConstants.UTF8)));
      } catch (UnsupportedEncodingException e) {
         throw CoreException.getInstance(CoreException.UNSUPPORTED_ENCODING, e);
      }
   }
   
   public static String prettyText(String text){
      
         int inx = text.indexOf(CoreConstants.CRLF+CoreConstants.CRLF);
         if(inx > 5){
            text = text.substring(inx+4, text.length());         
         }
         
         text = text.trim();
         
         if(text.startsWith("<?xml")){
            return prettyXml(text, null);
            
         } else if(text.startsWith("<")){
            return prettyXml(text, XML_LINE);
            
         } else if(text.startsWith("[") || text.startsWith("{")){
            return jsonText(text, true);
            
         }  else {
            return text;
         }      
   } 

   public static String jsonText( String txt, boolean bypassXML){

      try {
         boolean isGWTok = txt.startsWith("//OK");
         boolean isGWTerr = txt.startsWith("//EX");
         if (isGWTok || isGWTerr) {
            txt = txt.substring(4, txt.length());
         }
         return toJSON(txt, isGWTok, isGWTerr);
         
      } catch (JSONException e) {
//         ExceptionHandler.handle(e); 
         if(bypassXML){
            return txt;
         } else {
            String line = txt.startsWith("<?xml")? null:XML_LINE;
            return prettyXml(txt, line);            
         }

      } catch (Exception e) {
         ExceptionHandler.handle(e);
         if(bypassXML){
            return txt;
         } else {
            String line = txt.startsWith("<?xml")? null:XML_LINE;
            return prettyXml(txt, line);            
         }
      }

   }
   
   private static String toJSON(String txt, boolean isGWTok, boolean isGWTerr) throws JSONException{
      
      String res = null;
      
      if(txt.startsWith("[")){
         JSONArray arr = new JSONArray(txt);
         res = arr.toString(4);         
      } else {
         JSONObject obj = new JSONObject(txt);
         res = obj.toString(4);         
      }
      if (isGWTok) {
         return "//OK" + res;
      } else if (isGWTerr) {
         return "//EX" + res;
      } else {
         return res;
      }
   }
   

   public static String prettyXml(String xml, String firstLine){
      try {
         
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         Serializer serializer = new Serializer(out);
         serializer.setIndent(2);
         if(firstLine != null){
            serializer.write(new Builder().build(firstLine + xml, ""));            
         } else {
            serializer.write(new Builder().build(xml, ""));
         }
         String ret =  out.toString("UTF-8");
         if(firstLine != null){
            return ret.substring(firstLine.length() , ret.length()).trim();
         } else {
            return ret;            
         }
      } catch (Exception e) {
//         ExceptionHandler.handle(e);
         return xml;
      }
   }
   
   public static String getHdToken(String url, String md){
      final HttpClient client = new HttpClient();      
      PostMethod post = new PostMethod(url);
      post.setRequestHeader("content-type", "application/x-www-form-urlencoded");
      post.setParameter("v", md);
      
      post.setApacheHttpListener(httpListener);
      try {
         HttpUtils.execute(client, post, responseReader);
         Header header = post.getResponseHeader("hd-token");
         if(header != null){
//            System.out.println("hd-token:" + header.getValue() + "'");
            return header.getValue();
         }
      } catch (Exception ignore) {
         ExceptionHandler.handle(ignore);
      } finally {
         if(post != null) post.releaseConnection();
      }
      return null;
   }
   
}
