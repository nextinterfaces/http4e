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
public interface CoreObjects {

   String RESOURCE_CACHE      = "resource.cache";
   String ROOT_PATH_CORE      = "root.core";
   String ROOT_PATH_UI        = "root.ui";

   String HTTP_HEADERS        = "http.headers";
   String HTTP_HEADER_VALUES  = "http.header.values";
   String HEADER_TYPE_TRACKER = "header.type.tracker";
   String PARAM_TYPE_TRACKER  = "param.type.tracker";
   String IS_STANDALONE       = "is.standalone";
   String AUTH_ITEM           = "auth.bean";
   String PROXY_ITEM          = "proxy.bean";
   String PARAMETERIZE_ARGS   = "parameterize.args";
   String RESPONSE_VIEW_SIZE  = "resp.size";

}
