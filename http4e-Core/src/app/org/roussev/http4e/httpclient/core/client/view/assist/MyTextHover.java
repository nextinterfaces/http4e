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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;
import org.roussev.http4e.httpclient.core.misc.LazyObjects;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class MyTextHover implements ITextHover {

   public IRegion getHoverRegion( ITextViewer textViewer, int offset){
      Point selection = textViewer.getSelectedRange();
      if (selection.x <= offset && offset < selection.x + selection.y)
         return new Region(selection.x, selection.y);
      return new Region(offset, 0);
   }

   public String getHoverInfo( ITextViewer textViewer, IRegion hoverRegion){
      int offset = hoverRegion.getOffset();
      if (hoverRegion != null) {
         try {
            if (hoverRegion.getLength() > -1) {
               IDocument doc = textViewer.getDocument();
               // String key = textViewer.getDocument().get(offset, hoverRegion.getLength());
               // ITypedRegion region = doc.getPartition(offset);
               ITypedRegion partitionType = textViewer.getDocument().getPartition(offset);
               
               IRegion reg2 = doc.getLineInformationOfOffset(offset);
               String lineText = doc.get(reg2.getOffset(), reg2.getLength());
               // if(BaseUtils.isEmpty(key)){
               // key = BaseUtils.getKeyFromLine(lineText);
               // return HAssistInfoMap.getInfo(key);
               // }
               String key = DocumentUtils.getKeyFromLine(lineText);
               return LazyObjects.getInfoMap("Headers").getInfo(key);
            }
         } catch (BadLocationException x) {
         }
      }

      return "JavaEditorMessages.getString(MyTextHover.emptySelection)";
   }

   
   
//   public static void main( String[] args){
////      String txt = "q w             e   =   [aaa]";
////      System.out.println("1: '" + DocumentUtils.getKeyFromLine(txt) + "'");
////
////      int posBraket = txt.indexOf(AssistConstants.L_BRACKET);
////      int posEq = txt.indexOf(AssistConstants.EQUAL);
//   }
   
   
}
