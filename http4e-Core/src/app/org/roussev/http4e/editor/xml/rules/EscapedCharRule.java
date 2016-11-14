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

public class EscapedCharRule implements IRule {

   IToken       fToken;
   StringBuilder buffer = new StringBuilder();


   public EscapedCharRule( IToken token) {
      super();
      this.fToken = token;
   }


   /*
    * @see IRule#evaluate(ICharacterScanner)
    */
   public IToken evaluate( ICharacterScanner scanner){

      buffer.setLength(0);

      int c = read(scanner);
      if (c == '&') {

         int i = 0;
         do {
            c = read(scanner);
            i++;

            if (c == '<' || c == ']') {
               for (int j = i - 1; j > 0; j--)
                  scanner.unread();
               return Token.UNDEFINED;
            }
         } while (c != ';');
         return fToken;
      }

      scanner.unread();
      return Token.UNDEFINED;
   }


   private int read( ICharacterScanner scanner){
      int c = scanner.read();
      buffer.append((char) c);
      return c;
   }

}