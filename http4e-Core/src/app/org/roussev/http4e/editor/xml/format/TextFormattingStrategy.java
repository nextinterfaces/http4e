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
package org.roussev.http4e.editor.xml.format;

/**
 * 
 * @author Phil Zoio
 */
public class TextFormattingStrategy extends DefaultFormattingStrategy {

   private static final String lineSeparator = System.getProperty("line.separator");


   public TextFormattingStrategy() {
      super();
   }


   public String format( String content, boolean isLineStart, String indentation, int[] positions){
      if (indentation.length() == 0)
         return content;
      return lineSeparator + content.trim() + lineSeparator + indentation;
   }

}