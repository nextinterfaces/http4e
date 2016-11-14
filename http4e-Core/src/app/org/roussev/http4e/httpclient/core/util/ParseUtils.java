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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreMessages;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.view.assist.AssistConstants;
import org.roussev.http4e.httpclient.core.client.view.assist.DocumentUtils;

/**
 * Class with parsing utilities.
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ParseUtils {

   private final static String[]           shortExt     = new String[] { "com", "net", "org", "biz", "info", "co", "uk", "ca", "de", "eu", "name", "xxx", "us", };
   private final static String[]           longExt      = new String[] { "bg", "be", "ru", "ie", "fr", "es", "cn", "au", "at", "qc", "bc", "jp", "cz", "dk", "edu", "gov", "hk", "il", "fi", "it", "kr", "nz", "se", "nl", "no" };

   private final static Collection<String> shortExtList = Arrays.asList(shortExt);
   private final static Collection<String> longExtList  = Arrays.asList(longExt);


   public static String appendParamsToUrl( String url, String params){
      int q = url.indexOf(CoreConstants._Q);
      if (q < 0) {
         url = url + CoreConstants._Q + params;
      } else {
         url = url.substring(0, q + 1) + params;
      }
      if (BaseUtils.isEmpty(params)) {
         url = url.substring(0, url.length() - 1);
      }
      return url;
   }


   public static String doUrlToText( String url, boolean urlDecode){
      Map mparams = urlToMap(url, urlDecode);
      String textData = mapToText(mparams);
      return textData;
   }


   public static String doBodyToParam( String body){
      Map map = linesToMap(body);
      return mapToText(map);
   }


   static Map urlToMap( String url, boolean urlDecode){
      int q = url.indexOf(CoreConstants._Q);
      String params = "";
      if (q > 0) {
         params = url.substring(q + 1, url.length());
         if (urlDecode)
            params = urlDecode(params);
         return linesToMap(params);
      }
      return new HashMap();
   }


   static String mapToText( Map map){
      final StringBuilder sb = new StringBuilder();
      for (Iterator it = map.keySet().iterator(); it.hasNext();) {
         String key = (String) it.next();
         for (Iterator it2 = ((ParseBean) map.get(key)).getValues().iterator(); it2.hasNext();) {
            String val = (String) it2.next();
            sb.append(key);
            sb.append(AssistConstants.BRACKETS_COMPLETION);
            sb.append(val);
            sb.append(CoreConstants._LF);
         }
      }
      if (sb.length() > 0) {
         return sb.substring(0, sb.length() - 1);
      } else {
         return CoreConstants.EMPTY_TEXT;
      }
   }


   public static Map linesToMap( String params){
      StringTokenizer st = new StringTokenizer(params, AssistConstants.PARAM_LINE_DELIM);
      Map paramsMap = new LinkedHashMap();
      String p = null;
      while (st.hasMoreTokens()) {
         p = st.nextToken().trim();
         if (!BaseUtils.isEmpty(p)) {
            lineToMap(p, paramsMap);
         }
      }
      return paramsMap;
   }


   public static Map linesToMap2( String params){
      Properties map = new Properties();
      try {
         map.load(new ByteArrayInputStream(params.toString().getBytes("UTF8")));

      } catch (Exception e) {
         e.printStackTrace();
      }

      return map;
   }


   private static void lineToMap( String pline, Map pmap){
      StringTokenizer st = new StringTokenizer(pline, AssistConstants.PARAM_DELIM_EQ);
      String kk = st.nextToken();
      if (BaseUtils.isEmpty(kk) || DocumentUtils.isComment(kk)) {
         return;
      }
      String vv = "";
      try {
         vv = pline.substring(kk.length() + 1, pline.length());
      } catch (StringIndexOutOfBoundsException e) {
      }
      ParseBean pa = (ParseBean) pmap.get(kk);
      if (pa == null) {
         pa = new ParseBean(kk);
      }
      pa.addValue(BaseUtils.noNull(vv));
      pmap.put(kk, pa);
   }


   public static String toUrlParams( String text, boolean urlEncode){

      final Map paramMap = linesToMap(text);
      final StringBuilder sb = new StringBuilder();
      for (Iterator it = paramMap.keySet().iterator(); it.hasNext();) {
         String key = (String) it.next();
         final ParseBean p = (ParseBean) paramMap.get(key);
         for (Iterator it2 = p.getValues().iterator(); it2.hasNext();) {
            String val = (String) it2.next();
            sb.append(key);
            if (val != null) {
               sb.append("=");
               val = val.trim();
               sb.append(urlEncode ? urlEncode(val) : val);
            }
            sb.append(CoreConstants._AND);
         }
      }
      if (sb.length() > 0) {
         return sb.substring(0, sb.length() - 1);
      } else {
         return CoreConstants.EMPTY_TEXT;
      }
   }


   public static String toTitle( String url){

      if (BaseUtils.isEmpty(url)) {
         return CoreMessages.EMPTY_TITLE;
      }

      int q = url.indexOf(CoreConstants._Q);
      if (q > 0) {
         url = url.substring(0, q);
      }
      if (url.startsWith((CoreConstants.PROTOCOL_HTTP))) {
         url = url.substring(CoreConstants.PROTOCOL_HTTP.length(), url.length());
      } else if (url.startsWith((CoreConstants.PROTOCOL_HTTPS))) {
         url = url.substring(CoreConstants.PROTOCOL_HTTPS.length(), url.length());
      }
      if (url.startsWith((CoreConstants.WWW + "."))) {
         url = url.substring(3 + 1, url.length());
      }

      // Transformin roussev.com/aa/bb/cc to 'rous/aa/bb/cc'
      try {
         int inx = url.indexOf('/');
         if (url.length() > CoreConstants.MIN_TAB_NAME_SIZE && inx > 0) {
            String domain = url.substring(0, inx);
            String path = url.substring(inx, url.length());
            String nameWithoutExtension = domain;
            String[] split = domain.split("\\.");
            for (String name : split) {
               if (!shortExtList.contains(name) && !longExtList.contains(name)) {
                  if (!shortExtList.contains(name)) {
                     nameWithoutExtension = name;
                  }
               }
            }

            if (nameWithoutExtension.length() > 5) {
               nameWithoutExtension = nameWithoutExtension.substring(0, 4);
            }

            url = nameWithoutExtension + "" + path;
         }
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }

      if (url.length() <= CoreConstants.MAX_TAB_NAME_SIZE) {
         return url;
      }

      int minSize = url.indexOf(CoreConstants._SLASH);
      if (CoreConstants.MAX_TAB_NAME_SIZE > minSize) {
         url = url.substring(0, CoreConstants.MAX_TAB_NAME_SIZE);
      } else {
         url = url.substring(0, minSize);
      }

      return url;
   }


   /**
    * Translates paramText to: List[0] urlText, List[1] bodyText.
    * 
    * @return
    */
   public static List paramToUrlAndBody( String paramText){
      List res = new ArrayList();
      Map<String, String> parameterizedArgs = (Map<String, String>) CoreContext.getContext().getObject(CoreObjects.PARAMETERIZE_ARGS);

      final Map paramMap = ParseUtils.linesToMap(paramText);
      StringBuilder sBody = new StringBuilder();
      StringBuilder sUrl = new StringBuilder();

      for (Iterator it = paramMap.keySet().iterator(); it.hasNext();) {
         String key = (String) it.next();
         ParseBean pp = (ParseBean) paramMap.get(key);
         if (pp.getValues().size() == 0) {
            sBody.append(key);
            sBody.append(CoreConstants._AND);
            sUrl.append(urlEncode(key /* parameterizedVal */)); // TODO. What's
            // the meaning
            // of ParseBean.
            // Seams dead
            // class?
            sUrl.append(CoreConstants._AND);
         } else {
            for (Iterator it2 = pp.getValues().iterator(); it2.hasNext();) {
               String val = (String) it2.next();
               String parameterizedVal = ParseUtils.getParametizedArg(val, parameterizedArgs);
               // -- body
               sBody.append(key);
               sBody.append(CoreConstants._EQ);
               sBody.append(/* val */parameterizedVal);
               sBody.append(CoreConstants._AND);
               // -- url
               sUrl.append(key);
               sUrl.append(CoreConstants._EQ);
               sUrl.append(urlEncode(/* val */parameterizedVal));
               sUrl.append(CoreConstants._AND);
            }
         }
      }
      res.add(sUrl.toString());
      res.add(sBody.toString());
      // System.out.println("sBody[" + sBody + "]");
      // System.out.println("sUrl [" + sUrl + "]");

      return res;
   }


   public static String getParametizedArg( String argValue, Map<String, String> parameterizedMap){

      String parameterizedVal = null;
      if (parameterizedMap!= null && argValue != null && argValue.startsWith("@")) {
         String parameterizedKey = argValue.substring(0, argValue.length());
         parameterizedVal = parameterizedMap.get(parameterizedKey);
      }

      if (parameterizedVal != null) {
         return parameterizedVal;

      } else {
         return argValue;
      }
   }


   public static String urlEncode( String sUrl){
      if (sUrl == null) {
         return "";
      }
      try {
         return URLEncoder.encode(sUrl, CoreConstants.UTF8);
      } catch (UnsupportedEncodingException e) {
         ExceptionHandler.handle(e);
         return sUrl;
      }
   }


   public static String urlDecode( String sUrl){
      if (sUrl == null) {
         return "";
      }
      try {
         return URLDecoder.decode(sUrl, CoreConstants.UTF8);
      } catch (UnsupportedEncodingException e) {
         ExceptionHandler.handle(e);
         return sUrl;
      }
   }


   public static String doUrlToParam2( String url, Map<String, String> existingParams, boolean urlDecode){
      int q = url.indexOf(CoreConstants._Q);
      String params = "";
      if (q > 0) {
         params = url.substring(q + 1, url.length());
         if (urlDecode)
            params = urlDecode(params);
      }
      StringTokenizer st = new StringTokenizer(params, "" + CoreConstants._AND);
      StringBuilder sb = new StringBuilder();
      while (st.hasMoreTokens()) {
         String line = st.nextToken();

         // existing param. Check if its being parameterized
         String parKey = line.substring(0, line.indexOf("="));
         String tokVal = existingParams.get(parKey);
         if (tokVal != null && tokVal.startsWith("@")) {
            line = parKey + "=" + tokVal;
         }
         sb.append(line);
         sb.append(CoreConstants._LF);
      }
      return sb.toString();
   }


   public static String bodyToParam2( String body){
      StringTokenizer st = new StringTokenizer(body, "" + CoreConstants._AND);
      StringBuilder sb = new StringBuilder();
      while (st.hasMoreTokens()) {
         String line = st.nextToken();
         sb.append(line);
         sb.append(CoreConstants._LF);
      }
      return sb.toString();
   }


   public static void main( String[] args) throws Exception{
      // System.out.println(decode("qwe", "ISO-8859-1"));
      // System.out.println(decode("qwe", "UTF8"));
      // System.out.println(encode("qwe", "ISO-8859-1"));
      // System.out.println(encode("qwe", "UTF8"));
      // System.out.println( paramsToURL( "http://qwe.com?asd", "dd=1"));
      // System.out.println( paramsToURL( "?asd", "dd=1"));
      // System.out.println( paramsToURL( "asd", "dd=1"));
      // System.out.println(doUrlToText("asd=1&dgf=2 7&i"));

      // System.out.println( doTextToUrl("http://qwe.com", toUrlParams("dd:
      // =1")));
      // System.out.println( toUrlParams("dd: 6 \n \r x=9"));
      // String params = "aa:1 \r\n bb=2";
      // System.out.println(appendParamsToUrl("http://qwe.com", "555555"));
      // String orig = "k1 1\n   \rk1   2\nk2=a";
      // String url = "http://asd.com?" + paramToUrlAndBody(orig).get(0);
      // System.out.println( url);
      // System.out.println( doUrlToText( url, true));
      // String body = (String)paramToUrlAndBody(orig).get(1);
      // StringTokenizer st = new StringTokenizer(body, ""+CoreConstants._AND);
      // StringBuilder sb = new StringBuilder();
      // while(st.hasMoreTokens()){
      // String line = st.nextToken();
      // sb.append(line);
      // sb.append(CoreConstants._LF);
      // }
      // System.out.println(sb);

      // System.out.println( doUrlToText2( "q?k1=++2&k1=1&k2=a&", true));

   }

}