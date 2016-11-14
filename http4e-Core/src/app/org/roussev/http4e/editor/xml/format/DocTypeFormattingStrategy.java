/*
 * Created on Oct 11, 2004
 */
package org.roussev.http4e.editor.xml.format;

/**
 * 
 * @author Phil Zoio
 */
public class DocTypeFormattingStrategy extends DefaultFormattingStrategy {

   public String format( String content, boolean isLineStart, String indentation, int[] positions){
      return lineSeparator + content;
   }

}