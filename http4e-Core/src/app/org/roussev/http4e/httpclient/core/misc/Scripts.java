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

/**
 * This interface contains the JavaScript functions used across http4E.
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public interface Scripts {

   String POPUP_WINDOW = "function popup_window(url, width, height, options){  " +
       "var w = (width) ? width : 400;" +
       "var h = (height) ? height : 400;" +
       "var t = (screen.height) ? (screen.height - h) / 2 : 0;" +
       "var l = (screen.width) ? (screen.width - w) / 2 : 0;" +
       "var opt = (options) ? options : 'toolbar = 1, location = 1, directories = 1, status = 1, menubar = 1, scrollbars = 1, copyhistory = no, resizable = 1';" + 
       "var popped = window.open(url, 'popupwindow', 'top = '+t+', left = '+l+', width = '+w+', height = '+h+',' + opt);" +
       "popped.focus();" + 
       "};";

   String HEADER = "<html><head><title>Exported Call</title><style type='text/css'></style></head><body><code><pre>";
   String FOOTER = "</pre></code></body></html>";


}


