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

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public interface CoreMessages {

   String VERSION             = "5.0.12";
   String PLUGIN_NAME         = "HTTP4e Client";
   String PLUGIN_NAME_SHORT   = "http4e";
   String PLUGIN_ABOUT        = "Version: " + VERSION;

   String HEADER_DEFAULTS     = "";                                                                  // "header=value";
   String PARAM_DEFAULTS      = "";                                                                  // "parameter=value";
   String EMPTY_TITLE         = "New Tab";
   String EMPTY_TITLE_NAME    = "New Tab";

   String CONNECTION_REFUSED  = "\n\tUnable to connect: Can't establish a connection to server \n\n\t";
   String CONNECTION_DROPPED  = "\n\tIO error: The server dropped connection on us \n\n\t";
   String CONNECTION_IO_ERR   = "\n\tIO error: Can't establish a connection to server \n\n\t";
   String CONNECTION_ILLEAGAL = "\n\tWrong argumens: HTTP4e can't establish a connection to server \n\n\t";
   String CONNECTION_UNKNOWN  = "\n\tHTTP4e can't establish a connection to server \n\n\t";
   String ABORTED             = "";

   String LICENSE_TITLE       = "HTTP4e License";
}
