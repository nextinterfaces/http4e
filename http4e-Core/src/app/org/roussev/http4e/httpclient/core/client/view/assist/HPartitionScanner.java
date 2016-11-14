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

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HPartitionScanner extends RuleBasedPartitionScanner {

   public final static String   PROPERTIES_PARTITIONING = "___pf_partitioning";
   public final static String   COMMENT                 = "__pf_comment";
   public final static String   PROPERTY_VALUE          = "__pf_property_value";
   // public final static String PROPERTY_KEY = "__pf_property_key";
   
   public final static String[] PARTITIONS              = new String[] { 
      COMMENT, PROPERTY_VALUE, /*PROPERTY_KEY*/
   };


   public HPartitionScanner() {
      // IToken key= new Token(IDocument.DEFAULT_CONTENT_TYPE);
      IToken comment = new Token(COMMENT);
      IToken propertyValue = new Token(PROPERTY_VALUE);

      setPredicateRules(new IPredicateRule[] { 
            new SingleLineRule(AssistConstants.PARAM_DELIM_EQ, null, propertyValue, '\\', true, true), 
            new SingleLineRule("#", null, comment, (char) 0, true, true), });
   }
}
