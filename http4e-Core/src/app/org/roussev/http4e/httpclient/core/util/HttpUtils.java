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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 * This is an utility class for executing HTTP calls using Appache HttpClient
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HttpUtils {

   public static void execute( HttpClient client, HttpMethod httpmethod, ResponseReader reader) throws Exception{

      try {
         client.executeMethod(httpmethod);

         if (reader != null) {
            reader.read(httpmethod);
         }

      } catch (Exception e) {
         throw e;

      } finally {
         httpmethod.releaseConnection();
      }
   }

   public static void execute( HttpMethod httpmethod, ResponseReader reader) throws Exception{
      HttpClient client = new HttpClient();
      execute(client, httpmethod, reader);
   }

   public static void dumpResponse( HttpMethod httpmethod, PrintStream out){

      try {
         out.println("------------");
         out.println(httpmethod.getStatusLine().toString());

         Header[] h = httpmethod.getResponseHeaders();
         for (int i = 0; i < h.length; i++) {
            out.println(h[i].getName() + ": " + h[i].getValue());
         }

         InputStreamReader inR = new InputStreamReader(httpmethod.getResponseBodyAsStream());
         BufferedReader buf = new BufferedReader(inR);
         String line;
         while ((line = buf.readLine()) != null) {
            out.println(line);
         }
         out.println("------------");

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static void dumpResponse( HttpMethod httpmethod, StringBuilder httpResponsePacket, StringBuilder respBody){
      try {
         httpResponsePacket.append('\n');
         httpResponsePacket.append(httpmethod.getStatusLine().toString());
         httpResponsePacket.append('\n');

         Header[] h = httpmethod.getResponseHeaders();
         for (int i = 0; i < h.length; i++) {
            httpResponsePacket.append(h[i].getName() + ": " + h[i].getValue());
            httpResponsePacket.append('\n');
         }

         InputStreamReader inR = new InputStreamReader(httpmethod.getResponseBodyAsStream());
         BufferedReader buf = new BufferedReader(inR);
         String line;
         while ((line = buf.readLine()) != null) {
            httpResponsePacket.append(line);
            httpResponsePacket.append('\n');
            respBody.append(line);
            respBody.append('\n');
         }
         httpResponsePacket.append('\n');

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }



   // public static String getRequestPacket( HttpServletRequest req){
   //
   // StringBuilder sb = new StringBuilder();
   //
   // if(req == null){
   // return null;
   // }
   //
   // sb.append("\r\n");
   // sb.append("_______________________________________________Http Request
   // START________\r\n");
   // sb.append(req.getMethod());
   // sb.append(" ");
   // sb.append(req.getRequestURI());
   // String qStr = req.getQueryString();
   // sb.append(qStr!=null? "?" + qStr : "");
   // sb.append(" ");
   // sb.append(req.getProtocol());
   // sb.append("[\\r][\\n]\r\n");
   // for (Enumeration e = req.getHeaderNames(); e.hasMoreElements();) {
   // String name = (String) e.nextElement();
   // Enumeration headers = req.getHeaders(name);
   // if(headers.hasMoreElements()){
   // while (headers.hasMoreElements()) {
   // sb.append(name + ": " + headers.nextElement()).append("[\\r][\\n]\r\n");
   // }
   // } else {
   // sb.append(name + ": " + req.getHeader(name)).append("[\\r][\\n]\r\n");
   // }
   // }
   // sb.append("");
   // sb.append("[\\r][\\n]\r\n");
   // sb.append("[\\r][\\n]\r\n");
   //        
   // byte[] contentData = null;
   // String contentType = (req.getContentType() != null)?
   // req.getContentType().toLowerCase() : "";
   // if( PlatformConstants.HTTP_METHOD_POST.equals(req.getMethod()) &&
   // PlatformConstants.HTTP_CONTENT_TYPE_X_WWW_FORM.equals(contentType)){
   // contentData = getRequestContent_FormUrlEncoded(req);
   // } else {
   // contentData = getRequestContent(req);
   // }
   // try {
   // sb.append( new String(contentData, CryptConstants.CHARSET_UTF8));
   // } catch (UnsupportedEncodingException e) {
   // throw new RuntimeException(e);
   // }
   //        
   // sb.append("\r\n");
   // sb.append("_______________________________________________Http Request
   // END__________");
   //        
   // if(contentData != null && contentData.length > 0) {
   // sb.append("\r\nRequestBody(Hex):");
   // sb.append( HexUtils.prettyHex(contentData));
   // }
   //
   // return sb.toString();
   // }

   // private static byte[] getRequestContent_FormUrlEncoded(HttpServletRequest
   // req){
   //
   // byte[] contentData = null;
   // StringBuilder contentBuff = new StringBuilder();
   // for (Enumeration en = req.getParameterNames(); en.hasMoreElements();) {
   // String pName = (String)en.nextElement();
   // String[] values = req.getParameterValues(pName);
   // if( values!= null && values.length > 0){
   // for (String val : values) {
   // contentBuff.append(pName).append("=").append(val).append("&");
   // }
   // } else {
   // contentBuff.append(pName).append("&");
   // }
   // }
   // try {
   // contentData =
   // contentBuff.toString().getBytes(CryptConstants.CHARSET_UTF8);
   // } catch (UnsupportedEncodingException e) {
   // throw new RuntimeException(e);
   // }
   //        
   // if(contentData == null){
   // return new byte[]{};
   // }
   //        
   // return contentData;
   // }

   // public static byte[] getRequestContent(HttpServletRequest req){
   // if(req.getContentLength() < 1){
   // return new byte[]{};
   // }
   // byte[] contentBytes = new byte[req.getContentLength()];
   // InputStream in;
   // try {
   // in = req.getInputStream();
   // in.read(contentBytes);
   // return contentBytes;
   //
   // } catch (Exception e) {
   // throw new RuntimeException(e);
   // }
   // }
   
}
