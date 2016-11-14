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
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.roussev.http4e.editor.xml.ColorManager;
import org.roussev.http4e.editor.xml.IXMLColorConstants;
import org.roussev.http4e.editor.xml.XMLWhitespaceDetector;


/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLScanner extends RuleBasedScanner {

   public XMLScanner( ColorManager manager) {
      IToken procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
      IToken docType = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.DOCTYPE)));

      IRule[] rules = new IRule[3];
      // Add rule for processing instructions and doctype
      rules[0] = new MultiLineRule("<?", "?>", procInstr);
      rules[1] = new MultiLineRule("<!DOCTYPE", ">", docType);
      // Add generic whitespace rule.
      rules[2] = new WhitespaceRule(new XMLWhitespaceDetector());

      setRules(rules);
   }
}
