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
package org.roussev.http4e.editor.xml.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.roussev.http4e.editor.xml.ColorManager;
import org.roussev.http4e.editor.xml.IXMLColorConstants;
import org.roussev.http4e.editor.xml.rules.CDataRule;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLTextScanner extends RuleBasedScanner {

   public IToken ESCAPED_CHAR;
   public IToken CDATA_START;
   public IToken CDATA_END;
   public IToken CDATA_TEXT;

   IToken        currentToken;


   public XMLTextScanner( ColorManager colorManager) {

      ESCAPED_CHAR = new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.ESCAPED_CHAR)));
      CDATA_START = new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.CDATA)));
      CDATA_END = new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.CDATA)));
      CDATA_TEXT = new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.CDATA_TEXT)));
      IRule[] rules = new IRule[2];

      // Add rule to pick up escaped chars
      // Add rule to pick up start of CDATA section
      rules[0] = new CDataRule(CDATA_START, true);
      // Add a rule to pick up end of CDATA sections
      rules[1] = new CDataRule(CDATA_END, false);
      setRules(rules);

   }


   public IToken nextToken(){
      IToken token = super.nextToken();
      if (currentToken == CDATA_START || currentToken == CDATA_TEXT && token != CDATA_END) {
         this.currentToken = CDATA_TEXT;
         return CDATA_TEXT;
      }
      this.currentToken = token;
      return token;
   }
}