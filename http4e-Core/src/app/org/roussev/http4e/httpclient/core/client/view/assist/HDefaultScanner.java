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

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HDefaultScanner extends RuleBasedScanner {

   public HDefaultScanner() {
//      IToken procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
//      IRule[] rules = new IRule[2];
//      // Add rule for processing instructions
//      rules[0] = new SingleLineRule("<?", "?>", procInstr);
//      // Add generic whitespace rule.
//      rules[1] = new WhitespaceRule(new WhitespaceDetector());
//      setRules(rules);      

//      WordRule keywordRule = new WordRuleInsensitive(new IWordDetector() {
//         public boolean isWordStart( char c){
//            return Character.isJavaIdentifierStart(Character.toLowerCase(c));
//         }
//         public boolean isWordPart( char c){
//            return Character.isJavaIdentifierPart(Character.toLowerCase(c)) || c == '-';
//         }
//      });

//      IToken tokKeyword = new Token(new TextAttribute(ResourceUtils.getColor(Styles.PROC_INSTR), null, SWT.BOLD));
////      IToken tokComment = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.XML_COMMENT)));
////      IToken tokString = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.STRING)));
////      IToken tokSchema = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
//      
//      String[] KEYWORDS = new String[] { "Contenttype", "User-agent", "a-a" , "Cc" };
//      // add tokens for each reserved word
//      for (int n = 0; n < KEYWORDS.length; n++) {
//         keywordRule.addWord(KEYWORDS[n].toUpperCase(), tokKeyword);
//      }

      setRules( new IRule[] { 
//            keywordRule, 
//            new SingleLineRule("<?", "?>", tokSchema),
//            new MultiLineRule("[", "]", tokComment, '\\'), 
//            new SingleLineRule("'", "'", tokString, '\\'), 
//            new WhitespaceRule(new WhitespaceDetector())
            });
      
   }
}
