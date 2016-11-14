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
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class CoreException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public final static String GENERAL = "general.failure";
   public final static String FILE_NOT_FOUND = "file.nf";
   public final static String GIF_ANIMATION_FAILURE = "gif.animation";
   public final static String IO_EXCEPTION = "io.exception";
   public final static String UNSUPPORTED_ENCODING = "unsupported.encoding";
   public final static String HTTP_FAILURE = "http.error";
   public final static String HTTP_METHOD_NOT_IMPLEMENTED = "method.not.implemented";
   public final static String INVALID_URI = "invalid.uri";
   public final static String SSL = "ssl";

   private String code;
   private String message;
   private Throwable cause;
   
   
   public CoreException( String code) {
      super(code);
      this.code = code;
   }
   
   public CoreException( String code, String message) {
      this.code = code;
      this.message = message;
   }
   
   public CoreException( String code, String message, Throwable cause) {
      this.code = code;
      this.message = message;
      this.cause = cause;
   }
   
   public CoreException( String code, Throwable cause) {
      this.code = code;
      this.cause = cause;
   }
   
   
   public static CoreException getInstance(String code, String message, Exception e){
      if (e instanceof CoreException) {
         return (CoreException)e;
      }
      return new CoreException(code, message, e);
   }
   
   public static CoreException getInstance(String code, Exception e){
      if (e instanceof CoreException) {
         return (CoreException)e;  
      }
      return new CoreException(code, e);
   }   
   
   public String getCode(){
      return code;
   }
   
   public String getMessage() {
       return "Code[" + getCode() + "] " + message + (message==null?"":" "+message);
   }

   
   public Throwable getCause(){
      return (cause!=null)? cause : super.getCause();
   }
   
   

   
}
