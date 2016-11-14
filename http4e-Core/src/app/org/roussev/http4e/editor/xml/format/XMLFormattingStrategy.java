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
public class XMLFormattingStrategy extends DefaultFormattingStrategy {

   private String initialIndentation;
   boolean        lastTagWasOpening;


   public XMLFormattingStrategy() {
      super();
   }


   public void formatterStarts( String initialIndentation){
      this.initialIndentation = initialIndentation;
   }


   public String format( String content, boolean isLineStart, String indentation, int[] positions){

      if (isLineStart)
         indentation = initialIndentation;

      content = formatContent(content);

      // if the partition does not contain the start tag then just do
      // indentation
      if (content.indexOf("<") == -1) {
         // just check to see whether we need to indent the next tag
         if (content.indexOf("/>") != -1) {
            lastTagWasOpening = false;
         } else {
            lastTagWasOpening = true;
         }
         return content;
      }

      // start and end tag
      if (content.indexOf("/>") != -1) {

         if (lastTagWasOpening) {
            indentation = indentation + "\t";
         }
         lastTagWasOpening = false;
         return lineSeparator + indentation + content;

      }

      // end tag
      if (content.indexOf("</") != -1) {

         boolean lastOpening = lastTagWasOpening;
         lastTagWasOpening = false;

         // if the last tag was a closing tag we need to reduce the indentation
         if (!lastOpening) {
            if (indentation.length() > 1)
               indentation = indentation.substring(0, indentation.length() - 1);

            // add new line after content
            return lineSeparator + indentation + content;
         } else {
            return content;
         }

      }

      // start tag
      if (content.indexOf("<") != -1) {

         // if the last tag was an opening tag we need to reduce the indentation
         if (lastTagWasOpening) {
            indentation = indentation + "\t";
         }

         lastTagWasOpening = true;

         // add new line after content
         return lineSeparator + indentation + content;
      }
      return content;

   }


   protected String formatContent( String content){

      String[] contentParts = content.split("\\s+|\r|\n");
      StringBuilder buffer = new StringBuilder();
      for (int i = 0; i < contentParts.length; i++) {
         buffer.append(contentParts[i].trim()).append(" ");
      }
      buffer.delete(buffer.length() - 1, buffer.length());
      return buffer.toString();
   }


   public void formatterStops(){
   }

}