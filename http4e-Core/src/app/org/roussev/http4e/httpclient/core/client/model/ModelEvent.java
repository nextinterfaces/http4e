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

/**
 * This class contains all plug-in states event types
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ModelEvent {

   public final static int 
      UNKNOWN = -1, 
      FOLDER_FOCUS_GAINED = 96, 
      FOLDER_FOCUS_LOST = 97,     
      ITEM_ADD = 98,
      ITEM_CLOSE = 99,    
      ITEM_DISPOSE = 100,
      REQUEST_START = 101,
      REQUEST_APPENDED = 102,
      REQUEST_STOPPED = 103,
      REQUEST_ABORTED = 104,
      FOLDER_DISPOSE = 105,
      FOLDER_INIT = 106,
      HEADERS_FOCUS_GAINED = 107,
      HEADERS_FOCUS_LOST = 108,
      
      PARAMS_FOCUS_GAINED = 109,
      PARAMS_FOCUS_LOST = 110,
      URL_FOCUS_GAINED = 116,
      URL_FOCUS_LOST = 117,
      BODY_FOCUS_GAINED = 118,
      BODY_FOCUS_LOST = 119,
      HTTP_METHOD_CHANGE = 120,
      CONTENT_TYPE_CHANGE = 121,
      
      HEADERS_RESIZED = 111,
      PARAMS_RESIZED = 112,
      BODY_RESIZED = 113,
      REQUEST_RESIZED = 114,
      RESPONSE_RESIZED = 115,
      EXPORT = 122,
      IMPORT = 123,

      AUTH = 200,
      PROXY = 201,
      PARAMETERIZE_CHANGE = 202,
      PAYLOAD_FILES = 203
      ;
   
   
   private int       type;
   private Model     model;

   public ModelEvent( int eventType, Model model) {
      this.type = eventType;
      this.model = model;
   }

   public int getType(){
      return type;
   }

   public Model getModel(){
      return model;
   }

   public String toString(){
      return "{" + type + "," + model + "}";
   }

}
