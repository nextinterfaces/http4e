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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.roussev.http4e.httpclient.core.CoreConstants;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class Item implements Serializable {
 
   private static final long serialVersionUID = -6572164765472818414L;
   
   int                              hashcode          = 0;
   public String                    name              = "";
   public String                    httpMethod        = CoreConstants.HTTP_GET;

   /* CoreConstants.PROTOCOL_HTTP; */
   public String                    url               = "";

   /* default is no proxy */
   public ProxyItem                 currentProxy      = ProxyItem.createDirectConnectionProxy();

   public List<ProxyItem>           availableProxies  = new ArrayList<ProxyItem>();
   public String                    availableKeystore = null;

   public Map<String, List<String>> headers           = new HashMap<String, List<String>>();
   public Map<String, List<String>> parameters        = new HashMap<String, List<String>>();

   public String                    body              = "";
   public String                           request           = "";
   public String                           response          = "";

   int[]                            hSashWeights      = new int[] { CoreConstants.H_SASH_EQ, CoreConstants.H_SASH_EQ, CoreConstants.H_SASH_EQ };
   int[]                            vSashWeights      = new int[] { CoreConstants.V_SASH_EQ, CoreConstants.V_SASH_EQ, CoreConstants.V_SASH_EQ };

   ProxyItem proxyItem = new ProxyItem();
   AuthItem authItem = new AuthItem();
   
   public Map<String, String> parameteredArgs        = new HashMap<String, String>(); //FIXME. This does not belong at Item level, but Folder global level.

   public boolean equals( Object obj){
      if (obj != null && obj instanceof Item) {
         Item newItem = (Item) obj;
         return newItem.hashCode() == this.hashCode();
      }
      return false;
   }


   public int hashCode(){
      return hashcode;
   }


   public String toString(){
      return "{" + hashCode() + ",name=" + name +
      ",httpMethod=" + httpMethod +
      ",url=" + url +
      ",headers=" + headers +
      ",parameters=" + parameters +
      ",body=" + body + "}";
   }

}
