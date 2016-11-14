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
 * Central repository handling all exception types.
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ExceptionHandler {

   private final static boolean WARN_ENABLED = false;


   public static void handle( Throwable e){
      e.printStackTrace();
      if (e.getCause() != null) {
         handle(e.getCause());
      }
   }


   public static void print( String msg){
      System.out.println(">>>>" + msg);
   }


   public static void warn( String msg){
      if (WARN_ENABLED) {
         System.err.println(msg);
      }
   }


   public static void warn( Exception e){
      if (WARN_ENABLED) {
         System.err.println(e);
      }
   }

}
