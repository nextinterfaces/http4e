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
package org.roussev.http4e.httpclient.core.client.model;

import java.io.Serializable;

import org.apache.commons.lang.text.StrTokenizer;

/**
 * <p>
 * Bean for holding proxy information. This version holds no authentication
 * information.
 * </p>
 * 
 * @author andreaszbschmidt
 */
public class ProxyItem implements Serializable {

   private static final String DELIMITER        = ",";
   /**
    * 
    */
   private static final long   serialVersionUID = 1L;
   String                      host;
   int                         port;
   String                      name;
   boolean                     isProxy;


   public ProxyItem() {
   }


   /**
    * @param host
    *           proxy host
    * @param port
    *           proxy port
    * @param name
    *           name for ui
    * @param isProxy
    *           <code>false</code> if it is no proxy
    */
   private ProxyItem( String host, int port, String name, boolean isProxy) {
      super();
      this.host = host;
      this.port = port;
      this.name = name;
      this.isProxy = isProxy;
   }


   /**
    * @param proxysettings
    *           String to convert
    * @return
    * @throws Exception
    */
   public static ProxyItem createFromString( String proxysettings) throws Exception{
      ProxyItem returnvalue = null;
      try {
         StrTokenizer tokenizer = new StrTokenizer(proxysettings, DELIMITER);
         String[] tokenArray = tokenizer.getTokenArray();
         String name = tokenArray[0];
         String host = tokenArray[1];
         int port = Integer.parseInt(tokenArray[2]);
         returnvalue = new ProxyItem(host, port, name, true);
      } catch (Exception e) {
         throw new Exception("Error while parsing proxysettings", e);
      }

      return returnvalue;
   }


   /**
    * @return ProxyItem with settings for direct Connection
    */
   public static ProxyItem createDirectConnectionProxy(){
      return new ProxyItem("", 0, "direct", false);
   }


   public String getHost(){
      return host;
   }


   public void setHost( String host){
      this.host = host;
   }


   public int getPort(){
      return port;
   }


   public void setPort( int port){
      this.port = port;
   }


   public String getName(){
      return name;
   }


   public void setName( String name){
      this.name = name;
   }


   public boolean isProxy(){
      return isProxy;
   }


   public void setProxy( boolean isProxy){
      this.isProxy = isProxy;
   }


   public String toConfigString(){

      return new StringBuilder().append(name).append(DELIMITER).append(host).append(DELIMITER).append(port).toString();
   }

   @Override
   public String toString(){
      return "ProxyItem{host=" + host + ",port=" + port + ", isProxy=" + isProxy + "}";
   }
}
