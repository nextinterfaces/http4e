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
package org.roussev.http4e.httpclient.core.client.view;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class ItemState {

   public final static int POST_NO_UPDATE = 0;
   public final static int POST_ENABLED   = 1;
   public final static int POST_DISABLED  = 2;

   public final static int HTTP_STARTED  = 3;
   public final static int HTTP_STOPPED  = 4;
   public final static int HTTP_ABORTED  = 5;

   private int             state          = POST_NO_UPDATE;
   private boolean         xwwwEnabled    = true;

   public int getState(){
      return state;
   }

   public void setState( int state){
      this.state = state;
   }
   
   public boolean isXwwwTypeEnabled(){
      return xwwwEnabled;
   }
   
   public void setXwwwTypeEnabled( boolean xwwwEnabled){
      this.xwwwEnabled = xwwwEnabled;
   }

   public String toString(){
      return "ItemState{state=" + getState() + ",xwwwEnabled=" + isXwwwTypeEnabled() + "}";
   }

}
