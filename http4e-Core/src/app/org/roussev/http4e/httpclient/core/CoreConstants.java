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
package org.roussev.http4e.httpclient.core;

import java.util.ArrayList;
import java.util.List;

import org.roussev.http4e.httpclient.core.client.model.Model;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public interface CoreConstants {

   String   HTTP_GET          = "GET";
   String   HTTP_POST         = "POST";
   String   HTTP_PUT          = "PUT";
   String   HTTP_DELETE       = "DELETE";
   String   HTTP_HEAD         = "HEAD";
   String   HTTP_TRACE        = "TRACE";
   String   HTTP_CONNECT      = "CONNECT";
   String   HTTP_OPTIONS      = "OPTIONS";
   String[] HTTP11_METHODS    = new String[] { HTTP_GET, HTTP_POST, HTTP_PUT, HTTP_DELETE, HTTP_HEAD, HTTP_OPTIONS, HTTP_TRACE };

   Model    NULL_MODEL        = null;
   String   EMPTY_TEXT        = "";
   String   CROSS_TEXT        = "X";

//   String   SERIALIZED_PATH   = System.getProperty("java.io.tmpdir") + File.separator + "http4e-" + CoreMessages.VERSION + ".ser";

   int      H_SASH_EQ         = 33;
   int      H_SASH_MIN        = 8;
   int      H_SASH_MAX        = 82;
   int      V_SASH_EQ         = 33;
   int      V_SASH_MIN        = 1;
   int      V_SASH_MAX        = 65;

   String   WWW               = "www";
   String   PROTOCOL_HTTP     = "http://";
   String   PROTOCOL_HTTPS    = "https://";
   String   PROTOCOL_TCP      = "tcp://";


   String   HEADER_USER_AGENT = "User-Agent";
   String   CONTENT_TYPE_X_WWW_FORM = "Content-Type=application/x-www-form-urlencoded";

   /**
    * Misc
    */
   int      MAX_TAB_NAME_SIZE           = 15;
   int      MIN_TAB_NAME_SIZE           = 5;
   
   int      MAX_TRACKS_HEADER_KEYS      = 20; // TODO Headers and Parameters share same track words. Separate probably ?
   int      MAX_TRACKS_VALUES           = 20;

   char     _AND              = '&';
   char     _Q                = '?';
   char     _CR               = '\r';
   char     _LF               = '\n';
   char     _COL              = ':';
   char     _SEMICOL          = ';';
   char     _EQ               = '=';
   char     _OR               = '|';
   char     _SLASH            = '/';
   char     _SPACE            = ' ';
   String   CRLF              = _CR + "" + _LF;
   String   UTF8              = "UTF8";
   String   FILE_PREFIX       = "@";
   
   int      ASCII_MIN         = 32;
   int      ASCII_MAX         = 126;
   int      ASCII_EXT_MIN     = 128;
   int      ASCII_EXT_MAX     = 253;
   
   String   PLUGIN_CORE       = "org.roussev.http4e.core";
   String   PLUGIN_UI         = "org.roussev.http4e.ui";   
   
   int      IMAGES_HEIGHT     = 16;
   
   String[] FILE_FILTER_EXT   = {"*.*", "*.http4e", "*.xml", "*.txt", "*.json"};
      
   String   TITLE_HEADERS     = "Headers";
   String   TITLE_PARAMETERS  = "Params";
   String   TITLE_BODY        = "Body";
   String   TITLE_PARAMETERS_X_WWW  = "Params *x-www-form-urlencoded";
   String   TITLE_PARAMETERS_MULTIPART  = "Params *multipart/form-data";
   String   TITLE_BODY_X_WWW        = "Body *x-www-form-urlencoded";
   String   TITLE_REQUEST     = "HTTP Request";
   String   TITLE_RESPONSE    = "HTTP Response";
   String   TITLE_SPACE       = " ";
   
   List<String>     BLANK_LIST = new ArrayList<String>();

}
