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
package org.roussev.http4e.httpclient.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A PrintWriter that maintains a String as its backing store.
 * 
 * Usage: StringPrintWriter out = new StringPrintWriter();
 * printTo(out); System.out.println( out.getString() );
 */
class StringPrintWriter extends PrintWriter {

   public StringPrintWriter() {
      super(new StringWriter());
   }


   public StringPrintWriter( int initialSize) {
      super(new StringWriter(initialSize));
   }


   /**
    * &ltp&gtSince toString() returns information *about* this object, we want a
    * separate method to extract just the contents of the internal buffer as a
    * String.
    * </p>
    * 
    * @return the contents of the internal string buffer
    */
   public String getString(){
      flush();
      return ((StringWriter) out).toString();
   }

}