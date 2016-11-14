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
package org.roussev.http4e.editor.xml.xmlattribs;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLValidationError {

   private String errorMessage;
   private int    lineNumber;
   private int    columnNumber;


   public String getErrorMessage(){
      return errorMessage;
   }


   public void setErrorMessage( String errorMessage){
      this.errorMessage = errorMessage;
   }


   public int getLineNumber(){
      return lineNumber;
   }


   public void setLineNumber( int lineNumber){
      this.lineNumber = lineNumber;
   }


   public int getColumnNumber(){
      return columnNumber;
   }


   public void setColumnNumber( int columnNumber){
      this.columnNumber = columnNumber;
   }


   public String toString(){
      StringBuilder buf = new StringBuilder();
      buf.append("Error on ").append(" line ").append(lineNumber).append(", column ").append(columnNumber).append(": ").append(errorMessage);
      return buf.toString();
   }
}
