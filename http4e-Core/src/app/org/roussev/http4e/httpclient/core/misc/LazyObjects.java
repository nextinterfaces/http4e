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
package org.roussev.http4e.httpclient.core.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.client.view.assist.HAssistInfoMap;
import org.roussev.http4e.httpclient.core.client.view.assist.Tracker;
import org.roussev.http4e.httpclient.core.client.view.assist.Tracker.BlacklistStrategy;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class LazyObjects {
   
   private final static Collection EMPTY_LIST = new ArrayList();
   
   public static Tracker getHeaderTracker(){
      CoreContext ctx = CoreContext.getContext();
      Tracker headerTracker = (Tracker)ctx.getObject(CoreObjects.HEADER_TYPE_TRACKER);
      if(headerTracker == null){
         headerTracker = new Tracker(Tracker.MASTER_ID, CoreConstants.MAX_TRACKS_HEADER_KEYS);
         ctx.putObject(CoreObjects.HEADER_TYPE_TRACKER, headerTracker);
         Tracker.BlacklistStrategy blacklistStrategy = new BlacklistStrategy(){
            public boolean isBlacklisted(String word){
               return getHttpHeaders().contains(word);
            }
         };
         headerTracker.setBlacklistStrategy(blacklistStrategy);
      }
      return headerTracker;
   }
   
   
   public static List getHttpHeaders(){
      CoreContext ctx = CoreContext.getContext();
      List httpHeaders = (List)ctx.getObject(CoreObjects.HTTP_HEADERS);
      
      if (httpHeaders == null) {
         httpHeaders = BaseUtils.loadList( ResourceUtils.getBundleResourceStream(CoreConstants.PLUGIN_CORE, CoreImages.FILE_HEADERS));
         ctx.putObject(CoreObjects.HTTP_HEADERS, httpHeaders);
      }
      return httpHeaders;
   }
   
   
   public static Map getHeaderValues(){
      CoreContext ctx = CoreContext.getContext();
      Map headerValuesMap = (Map)ctx.getObject(CoreObjects.HTTP_HEADER_VALUES);
      if (headerValuesMap == null) {
         headerValuesMap = ResourceUtils.getBundleProperties(CoreConstants.PLUGIN_CORE, CoreImages.FILE_HEADERS_VALUES);
         ctx.putObject(CoreObjects.HTTP_HEADER_VALUES, headerValuesMap);
      }
      return headerValuesMap;
   }
   
   
   public static Collection getValuesForHeader(String header){
      CoreContext ctx = CoreContext.getContext();
      Collection valsForHeader = (Collection)ctx.getObject(CoreObjects.HTTP_HEADER_VALUES + header);
      if(valsForHeader == null){
         String linekeyVal = BaseUtils.noNull((String)LazyObjects.getHeaderValues().get(header));
         if(linekeyVal.length() > 0){
            StringTokenizer st = new StringTokenizer(linekeyVal, ",");
            valsForHeader = new ArrayList();
            while(st.hasMoreTokens()){
               String val = st.nextToken();
               valsForHeader.add(val);
            }
            ctx.putObject(CoreObjects.HTTP_HEADER_VALUES + header, valsForHeader);
         }
      }      
      if(valsForHeader == null) {
         valsForHeader = EMPTY_LIST;
      }
      return valsForHeader;
   }
   
   
   public static HAssistInfoMap getInfoMap(String headerKey){
      String prefix = "assist-info-";
      CoreContext ctx = CoreContext.getContext();
      HAssistInfoMap infoMap = (HAssistInfoMap)ctx.getObject(prefix+headerKey);
      if (infoMap == null) {
         infoMap = new HAssistInfoMap(CoreImages.FILE_HEADERS_ROOT + "Info-" + headerKey + ".properties");
         ctx.putObject(prefix+headerKey, infoMap);
      }
      return infoMap;
   }
   
   
   
}
