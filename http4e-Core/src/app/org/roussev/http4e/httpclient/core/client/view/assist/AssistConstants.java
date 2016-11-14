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
package org.roussev.http4e.httpclient.core.client.view.assist;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public interface AssistConstants {

   char EQUAL = '=';
   char SHARP = '#';

   String S_EQUAL = String.valueOf(EQUAL);
   String S_SHARP = String.valueOf(SHARP);

   String BRACKETS_COMPLETION = "=";

   String CONTENT_TYPE_MULTIPART    = "multipart/form-data";
   String CONTENT_TYPE_X_WWW_FORM   = "application/x-www-form-urlencoded";
   String HEADER_CONTENT_TYPE       = "content-type";
   String HEADER_ACCEPT_CHARSET     = "accept-charset";
   String HEADER_ACCEPT_LANGUAGE    = "accept-language";
   String HEADER_DATE               = "date";
   String HEADER_IF_MOD_SINCE       = "if-modified-since"; 
   String HEADER_RETRY_AFTER        = "Retry-After";
   String HEADER_LAST_MODIFIED      = "Last-Modified";
   String HEADER_IF_UNMOD_SINCE     = "If-Unmodified-Since";
   String HEADER_EXPIRES            = "Expires";
   
   
   char LINE_DELIM_NL = '\n';   //10
   char LINE_DELIM_CR = '\r';   //13
//   String CRLF = System.getProperty("line.separator");
   
   String S_NL = String.valueOf(LINE_DELIM_NL);
   String   PARAM_LINE_DELIM           = "\r\n&";
   String   PARAM_DELIM_EQ             = "=";   
   
}
