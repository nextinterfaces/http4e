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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.Item;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.view.Utils;
import org.roussev.http4e.httpclient.core.util.shared.Exported;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class Translator {

   public static Item httppacketToItem( String packet){
      String LN = System.getProperty("line.separator"); // "\r\n"

      String str;

      packet = packet.trim();
      String line = "";
      StringBuilder headers = new StringBuilder();
      StringBuilder body = new StringBuilder();
      BufferedReader reader = new BufferedReader(new StringReader(packet));
      try {

         int cnt = 0;
         boolean isbody = false;
         while ((str = reader.readLine()) != null) {
            if (str.length() == 0) {
               isbody = true;
            }
            if (cnt == 0) {
               line = str;
            } else if (!isbody) {
               headers.append(str);
               headers.append(LN);

            } else {
               body.append(str);
               body.append(LN);
            }
            cnt++;
         }

      } catch (IOException e) {
         ExceptionHandler.handle(e);
      }

      Properties headersMap = new Properties();
      try {
         headersMap.load(new ByteArrayInputStream(headers.toString().getBytes("UTF8")));

      } catch (Exception e) {
         e.printStackTrace();
      }

      // /////////////////////////////////////////////////
      Item item = new Item();
      item.headers = new HashMap<String, List<String>>();

      // Convert properties to HashMap
      Enumeration<?> en = headersMap.keys();
      while (en.hasMoreElements()) {
         String key = en.nextElement().toString();
         String value = headersMap.getProperty(key);
         List<String> valueList = new ArrayList<String>();
         valueList.add(value);
         item.headers.put(key, valueList);

      }
      try {
         item.name = Utils.trimProtocol(item.url);
      } catch (Exception ignore) {
      }

      StringTokenizer lineST = new StringTokenizer(line, " ");

      String method = "";
      String uri = "";
      try {
         method = BaseUtils.noNull(lineST.nextToken());
         uri = BaseUtils.noNull(lineST.nextToken());
      } catch (Exception e) {
         e.printStackTrace();
      }
      item.httpMethod = method.toUpperCase();
      item.url = "http://" + getHeader("Host", item.headers) + uri;

      String contentType = getHeader("Content-type", item.headers);
      boolean isXwwwFormType = "application/x-www-form-urlencoded".equalsIgnoreCase(contentType);

      if (isXwwwFormType) {
         StringTokenizer st = new StringTokenizer(body.toString().trim(), "&");
         while (st.hasMoreTokens()) {
            String param = "";
            try {
               param = st.nextToken();
               param = URLDecoder.decode(param, "UTF8");
            } catch (UnsupportedEncodingException ignore) {
            }

            StringTokenizer st2 = new StringTokenizer(param, "=");
            String p1 = "";
            String v1 = "";
            try {
               p1 = st2.nextToken();
               v1 = st2.nextToken();
            } catch (Exception ignore) {
            }
            if (!BaseUtils.isEmpty(p1)) {
               ArrayList<String> parameters = new ArrayList<String>();
               parameters.add(v1);
               item.parameters.put(p1, parameters);
            }
         }
      } else {
         item.body = BaseUtils.noNull(body.toString().trim());
      }

      return item;
   }


   public static String foldertabToJavaSource( ItemModel model){

      try {
         InputStream srcIN = ResourceUtils.getBundleResourceStream2(CoreConstants.PLUGIN_CORE, "resources/http-runner-src.txt");
         Exported exported = new Exported(srcIN, model.getHttpMethod(), model.getUrl(), model.getBody(), model.getHeaders(), model.getParameters());

         return exported.getSource();

      } catch (IOException e) {
         e.printStackTrace();
      }

      return "";
   }


   public static void main( String[] args) throws Exception{
      String packet = "GET /authentication HTTP/1.1\r\n" + "Host: 10.10.78.56:8099\r\n" + "User-Agent: ColdFusion\r\n" + "Content-type: application/x-www-form-urlencoded\r\n" + "Content-length: 61\r\n" + "\r\n" + "username=qwe+qwe&password=rty&sjh slk\r\n";
      // System.out.println(packet);

      httppacketToItem(packet);

      // System.out.println("------line------");
      // String line = packet.substring(0, packet.indexOf("\n"));
      // System.out.println(line);
      //
      // System.out.println("------headers------");
      // String headers = packet.substring(packet.indexOf("\n")+1,
      // packet.indexOf("\n\n"));
      // System.out.println(headers);
      // Properties headersMap = new Properties();
      // try {
      // headersMap.load(new ByteArrayInputStream(headers.getBytes("UTF8")));
      // System.out.println(headersMap);
      // } catch (Exception e) {
      // e.printStackTrace();
      // }
      //
      // System.out.println("------body------");
      // String body = packet.substring(packet.indexOf("\n\n")+2,
      // packet.length()-1);
      // System.out.println(body);
      //
      // ///////////////////////////////////////////////////
      // Item item = new Item();
      // item.headers = headersMap;
      //
      // StringTokenizer lineST = new StringTokenizer(line, " ");
      //
      // String method = "";
      // String uri = "";
      // try {
      // method = BaseUtils.noNull(lineST.nextToken());
      // uri = BaseUtils.noNull(lineST.nextToken());
      // } catch (Exception e) {
      // e.printStackTrace();
      // }
      // item.httpMethod = method.toUpperCase();
      // item.url = "http://" + getHeader("Host", item.headers) + uri;
      //
      // String contentType = getHeader("Content-type", item.headers);
      // boolean isXwwwFormType =
      // "application/x-www-form-urlencoded".equalsIgnoreCase(contentType);
      //
      // if(isXwwwFormType){
      // StringTokenizer st = new StringTokenizer(body, "&");
      // while (st.hasMoreTokens()) {
      // String param = URLDecoder.decode(st.nextToken(), "UTF8");
      // StringTokenizer st2 = new StringTokenizer(param, "=");
      // String p1 = "";
      // String v1 = "";
      // try {
      // p1 = st2.nextToken();
      // v1 = st2.nextToken();
      // } catch (Exception ignore) {}
      // if(!BaseUtils.isEmpty(p1)){
      // item.parameters.put(p1, v1);
      // }
      // }
      // } else {
      // item.body = BaseUtils.noNull(body);
      // }
      //
      // System.out.println(item);
   }


   private static String getHeader( String header, Map<String, List<String>> headersMap){
      String returnvalue = "";
      if (headersMap.containsKey(header) && headersMap.get(header) != null && headersMap.get(header).size() > 0) {
         // if available deliver first item
         returnvalue = headersMap.get(header).get(0);
      }
      return returnvalue;
   }

}
