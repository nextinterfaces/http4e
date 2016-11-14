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
package org.roussev.http4e.editor.xml.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 * @author Phil Zoio
 */
public class CDataRule implements IRule {

   IToken                      fToken;
   StringBuilder               buffer             = new StringBuilder();
   int                         charsRead          = 0;

   private String              matchString;
   private static final String START_MATCH_STRING = "<![CDATA[";
   private static final String END_MATCH_STRING   = "]]>";


   public CDataRule( IToken token, boolean start) {
      super();
      this.fToken = token;
      this.matchString = start ? START_MATCH_STRING : END_MATCH_STRING;
   }


   /*
    * @see IRule#evaluate(ICharacterScanner)
    */
   public IToken evaluate( ICharacterScanner scanner){

      buffer.setLength(0);

      charsRead = 0;
      int c = read(scanner);

      if (c == matchString.charAt(0)) {
         do {
            c = read(scanner);
         } while (isOK((char) c));

         if (charsRead == matchString.length()) {
            return fToken;
         } else {
            rewind(scanner);
            return Token.UNDEFINED;
         }

      }

      scanner.unread();
      return Token.UNDEFINED;
   }


   private void rewind( ICharacterScanner scanner){
      int rewindLength = charsRead;
      while (rewindLength > 0) {
         scanner.unread();
         rewindLength--;
      }
   }


   private int read( ICharacterScanner scanner){
      int c = scanner.read();
      buffer.append((char) c);
      charsRead++;
      return c;
   }


   private boolean isOK( char c){
      if (charsRead >= matchString.length())
         return false;
      if (matchString.charAt(charsRead - 1) == c)
         return true;
      else
         return false;
   }
}