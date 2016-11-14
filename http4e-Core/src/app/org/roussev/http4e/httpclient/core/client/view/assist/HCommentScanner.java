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
public class HCommentScanner extends RuleBasedScanner {

    public HCommentScanner() {
//       IToken comment = new Token(HPartitionScanner.COMMENT);         
       setRules(new IRule[] {
//             new SingleLineRule("#", null, comment, '\\', true, true),
//             new WhitespaceRule(new WhitespaceDetector())
       });
    }
}


