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
package org.roussev.http4e.editor.xml;

import org.eclipse.swt.graphics.RGB;

/**
 *  
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public interface IXMLColorConstants {

   RGB XML_COMMENT  = new RGB(128, 0, 0);
   RGB PROC_INSTR   = new RGB(200, 20, 200);
   RGB DOCTYPE      = new RGB(0, 150, 150);
   RGB STRING       = new RGB(0, 128, 0);
   RGB DEFAULT      = new RGB(0, 0, 0);
   RGB TAG          = new RGB(0, 0, 128);

   // enhancements
   RGB ESCAPED_CHAR = new RGB(128, 128, 0);
   RGB CDATA        = new RGB(0, 128, 128);
   RGB CDATA_TEXT   = new RGB(255, 0, 0);
}