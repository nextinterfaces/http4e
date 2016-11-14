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
package org.roussev.http4e.httpclient.core.client.view.assist;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.roussev.http4e.editor.xml.XMLPartitioner;
import org.roussev.http4e.editor.xml.scanners.XMLPartitionScanner;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.util.BaseUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class DocumentUtils {

   public static IDocument createDocument1(){
      IDocument doc = new Document(){
         public String getDefaultLineDelimiter(){
            return String.valueOf(AssistConstants.LINE_DELIM_NL) /*super.getDefaultLineDelimiter()*/;
         }
      };
      IDocumentPartitioner partitioner = new DefaultPartitioner(
            new HPartitionScanner(), 
            new String[] {
                HPartitionScanner.COMMENT,
                HPartitionScanner.PROPERTY_VALUE});
      partitioner.connect(doc);
      doc.setDocumentPartitioner(partitioner);
      
      return doc;  
   }
    

   public static IDocument createDocument2() {
       IDocument document = new Document();
       if( document != null) {
           IDocumentPartitioner partitioner = new XMLPartitioner(
                   new XMLPartitionScanner(), new String[] {
                           XMLPartitionScanner.XML_START_TAG,
                           XMLPartitionScanner.XML_PI,
                           XMLPartitionScanner.XML_DOCTYPE,
                           XMLPartitionScanner.XML_END_TAG,
                           XMLPartitionScanner.XML_TEXT,
                           XMLPartitionScanner.XML_CDATA,
                           XMLPartitionScanner.XML_COMMENT });
           partitioner.connect(document);
           document.setDocumentPartitioner(partitioner);
       }
       return document;
   }
   
   
   
   public static String getValueFromLine( String lineText){
      if (lineText == null) {
         return "";
      }
      lineText = lineText.trim();
      int inx = lineText.indexOf(AssistConstants.PARAM_DELIM_EQ);
      if(inx < 1) {         
         return "";
      }
      try {
         String val = lineText.substring(inx+1, lineText.length());
         return val.trim();
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
      return "";
   }
   

   public static String getKeyFromLine( String lineText){     
      
      if (lineText == null) {
         return "";
      }
      lineText = lineText.trim();
      int inx = lineText.indexOf(AssistConstants.PARAM_DELIM_EQ);
      if(inx < 1) {
         return lineText;
      }
      try {
         String val = lineText.substring(0, inx);
         return val.trim();
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
      return "";        
   }   
   
   public static String nl_to_cursor( IDocument doc, int offset){      
      try {
         for (int n = offset - 1; n >= 0; n--) {
            char c = doc.getChar(n);
            if ( !BaseUtils.isHttp4eIdentifier(c) || c == AssistConstants.EQUAL) {
               String res1 = doc.get(n + 1, offset - n - 1);
               return res1;
            } else if (n == 0) {
               String res2 = doc.get(n, offset);
               return res2;
            }
         }
      } catch (BadLocationException ignore) {
         ExceptionHandler.warn("lastWord: " + ignore);
      }
      return "";
   }   

   public static String delim_to_cursor( IDocument doc, int offsetDelim, int offset){      
      String valToCursor = "";
      try {
         valToCursor = doc.get(offsetDelim, offset - offsetDelim);
      } catch (BadLocationException ignore) {
         ExceptionHandler.warn("lastWord: " + ignore);
      }
      return valToCursor;
   }
   
   
   public static String getLineText( IDocument doc, int offset) throws BadLocationException{
      IRegion reg2 = doc.getLineInformationOfOffset(offset);
      return doc.get(reg2.getOffset(), reg2.getLength());
   }
   
   
   public static IRegion getDelimRegion( IDocument doc, int offset) throws BadLocationException{
      IRegion regLine = doc.getLineInformationOfOffset(offset);
      FindReplaceDocumentAdapter finder = new FindReplaceDocumentAdapter(doc);
      int lineOff = regLine.getOffset();
      IRegion regDelim = null;
      try {
         regDelim = finder.find(lineOff, AssistConstants.S_EQUAL, true, true, false, false);
     } catch (Exception ignore) {}

      return regDelim;
   }
   

   public static int getDelimOffset( IDocument doc, int offset) throws BadLocationException{
      int offDelim = -1;
      IRegion regDelim = getDelimRegion(doc,offset);
      if(regDelim != null) 
         offDelim = regDelim.getOffset();
      return offDelim;
      
   }
   
   public static void dumpOffset(int offset, int delimOffset, IDocument doc) throws BadLocationException{      
//      String nl_to_cursor = DocumentUtils.nl_to_cursor(doc, offset);
//      String lineText = DocumentUtils.getLineText(doc, offset);
//      System.out.println("key=" + getKeyFromLine(lineText) + "', val=" + getValueFromLine(lineText) + "'");
//      System.out.println("\n\n nl_to_cursor='" + nl_to_cursor + "', lineText='" + lineText + "'");
//      System.out.println("offset=" + offset);
//      System.out.println("SelectedRange=" + viewer.getSelectedRange());
//      System.out.println("SelectionProvider().getSelection.isEmpty=" + viewer.getSelectionProvider().getSelection().isEmpty());
//      System.out.println("partitionType.getType()=" + doc.getPartition(offset).getType());

//      for (int i = 0; i < doc.getLength(); i++) {
//         System.out.print("'" + doc.getChar(i) /*+ "-" + (int)doc.getChar(i)*/ + "' ");
//      }
//      System.out.println( "isInValueType=" + isInValueType(offset, delimOffset));
   }
   

   
   public static boolean isInValueType(int offset, int delimOffset){
      
//      FindReplaceDocumentAdapter finder = new FindReplaceDocumentAdapter(doc);
//      IRegion reg1 = doc.getLineInformationOfOffset(offset);
//      int lineOff = reg1.getOffset();
//      IRegion reg2 = null;
////      IRegion reg3 = null;
////      IRegion reg4 = null;      
//      try {
//         reg2 = finder.find(lineOff, AssistConstants.S_EQUAL, true, true, false, false);
//         System.out.println("reg2.getLength()" + reg2.getLength() + ", reg2.getOffset()" + reg2.getOffset());
////         reg3 = finder.find(lineOff, AssistConstants.S_L_BRACKET, true, true, false, false);
////         reg4 = finder.find(lineOff, AssistConstants.S_R_BRACKET, true, true, false, false);
//      } catch (BadLocationException ignore) {
//      } catch (NullPointerException ignore) {}

//      int oLeft = -1;
//      if(reg2 != null && reg3 == null){
//         oLeft = reg2.getOffset();
//      } else if(reg2 == null && reg3 != null){
//         oLeft = reg3.getOffset();         
//      } else if(reg2 != null && reg3 != null){
//         oLeft = reg2.getOffset() < reg3.getOffset() ? reg2.getOffset() : reg3.getOffset();         
//      }
//      
//      boolean insideInValue = false;
//      if(reg4 != null){
//         insideInValue = (offset <= reg4.getOffset());
//         if(oLeft != -1){
//            insideInValue = insideInValue && (offset > oLeft);
//         }
//      } else {
//         if(oLeft != -1){
//            insideInValue = (offset > oLeft);
//         }
//      }

//      int offDelim = -1;
//      IRegion regDelim = getDelimRegion(doc,offset);
//      if(regDelim != null) offDelim = regDelim.getOffset();
      
      boolean insideInValue = false;
      if(delimOffset != -1){
         insideInValue = (offset > delimOffset);
      }
      return insideInValue;
   }
   

   

   
   public static String findMostRecentWord( int startSearchOffset, IDocument doc){
      int currOffset = startSearchOffset;
      char currChar;
      String word = "";
      try {
         while (currOffset >= 0 && !isNL(currChar = doc.getChar(currOffset))) {
            word = currChar + word;
            currOffset--;
         }
         return word;
      } catch (BadLocationException e) {
//         ExceptionHandler.handle(e);
         ExceptionHandler.warn("findMostRecentWord: " + e);
         return null;
      
      }
   }
   
   
   public static boolean isWhitespaceString( String string){
      StringTokenizer tokenizer = new StringTokenizer(string);
      // if there is at least 1 token, this string is not whitespace
      return !tokenizer.hasMoreTokens();
   }
      
   
   public static boolean isNL( String str){
      if(str == null || str.equals("")){
         return false;
      }
      return (str.startsWith("\n") || str.startsWith("\r"));
   }
   
   public static boolean isComment( String str){
      if(str != null && str.trim().startsWith(AssistConstants.S_SHARP)){
         return true;
      }
      return false;
   }
   
   
   public static boolean isNL(char c){
      boolean res = false;
      if(c == AssistConstants.LINE_DELIM_CR || c == AssistConstants.LINE_DELIM_NL){
         return true;
      }
      return res;
   }
   
   
   public static void dumpChars( char[] ch){
      StringBuilder sb = new StringBuilder(ch.length);
      for (int i = 0; i < ch.length; i++) {
         sb.append((int)ch[i]);
         sb.append(",");
      }
      System.out.println(sb);
   }
   
   private static List getValueListByKey(String key, Map masterCache){
      List list = (List)masterCache.get(key);
      if(list == null){
         list = new LinkedList();         
      }
      return list;
   }

   
//   public static void main( String[] args){
//      String newWord = "     qwe = 44444 44 44444     ";
//      String key = getKeyFromLine(newWord);
//      String val = getValueFromLine(newWord);
//      Map masterCache = new HashMap();
//      List values = getValueListByKey(key, masterCache);
//      values.add(val);
//      masterCache.put(key,values);
//      System.out.println("[" + key + "=" + val + "]");
//   }
   
}
